package com.example.beacon.vdf.application.combination;

import br.gov.inmetro.beacon.library.ciphersuite.suite0.CipherSuiteBuilder;
import br.gov.inmetro.beacon.library.ciphersuite.suite0.CriptoUtilService;
import br.gov.inmetro.beacon.library.ciphersuite.suite0.ICipherSuite;
import com.example.beacon.interfac.infra.ExternalEntity;
import com.example.beacon.shared.ByteSerializationFields;
import com.example.beacon.vdf.VdfSloth;
import com.example.beacon.vdf.application.combination.dto.SeedUnicordCombinationVo;
import com.example.beacon.vdf.application.vdfunicorn.VdfUnicornService;
import com.example.beacon.vdf.infra.entity.CombinationEntity;
import com.example.beacon.vdf.infra.entity.CombinationSeedEntity;
import com.example.beacon.vdf.repository.CombinationRepository;
import com.example.beacon.vdf.scheduling.CombinationResultDto;
import com.example.beacon.vdf.sources.SeedBuilder;
import com.example.beacon.vdf.sources.SeedCombinationResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CombinationServiceCalcAndPersist {
    private final CombinationRepository combinationRepository;
    private final String certificateId = "04c5dc3b40d25294c55f9bc2496fd4fe9340c1308cd073900014e6c214933c7f7737227fc5e4527298b9e95a67ad92e0310b37a77557a10518ced0ce1743e132";
    private final ICipherSuite cipherSuite;
    private final SeedBuilder seedBuilder;
    private final VdfUnicornService vdfUnicornService;
    private final Environment env;
    private final SeedCombinationResult seedCombinationResult;

    @Autowired
    public CombinationServiceCalcAndPersist(CombinationRepository combinationRepository,
                                            SeedBuilder seedBuilder, VdfUnicornService vdfUnicornService,
                                            Environment env, SeedCombinationResult seedCombinationResult) {
        this.combinationRepository = combinationRepository;
        this.cipherSuite = CipherSuiteBuilder.build(0);
        this.seedBuilder = seedBuilder;
        this.vdfUnicornService = vdfUnicornService;
        this.env = env;
        this.seedCombinationResult = seedCombinationResult;
    }

    @Async("threadPoolTaskExecutor")
    public void run(String timeStamp, List<SeedUnicordCombinationVo> seedUnicordCombinationVos, BigInteger x) throws Exception {
        int iterations = Integer.parseInt(env.getProperty("beacon.combination.iterations"));

        log.warn("Start combination sloth");
        BigInteger y = VdfSloth.mod_op(x, iterations);
        log.warn("End combination sloth");

        persist(timeStamp, seedUnicordCombinationVos, iterations, x, y);
    }

    @Transactional
    protected void persist(String timeStamp, List<SeedUnicordCombinationVo> seedUnicordCombinationVos, int iterations, BigInteger x, BigInteger y) throws Exception {
        Long maxPulseIndex = combinationRepository.findMaxPulseIndex();
        PrivateKey privateKey = CriptoUtilService.loadPrivateKeyPkcs1(env.getProperty("beacon.x509.privatekey"));

        if (maxPulseIndex==null){
            maxPulseIndex = 1L;
        } else {
            maxPulseIndex = maxPulseIndex + 1L ;
        }

        String uri = env.getProperty("beacon.url") +  "/beacon/2.0/combination/pulse/" + maxPulseIndex;

        CombinationEntity combinationEntity = new CombinationEntity();
        combinationEntity.setUri(uri);
        combinationEntity.setVersion("Version 1.0");
        combinationEntity.setPulseIndex(maxPulseIndex);
        combinationEntity.setTimeStamp(ZonedDateTime.parse(timeStamp, DateTimeFormatter.ISO_DATE_TIME));
        combinationEntity.setCertificateId(this.certificateId);
        combinationEntity.setCipherSuite(0);

        combinationEntity.setExternal(ExternalEntity.newExternalEntity());

        combinationEntity.setCombination(env.getProperty("vdf.combination").toUpperCase());
        combinationEntity.setPeriod(Integer.parseInt(env.getProperty("beacon.combination.period")));

        seedUnicordCombinationVos.forEach(dto ->
                combinationEntity.addSeed(new CombinationSeedEntity(dto, combinationEntity)));

        combinationEntity.setP("9325099249067051137110237972241325094526304716592954055103859972916682236180445434121127711536890366634971622095209473411013065021251467835799907856202363");
        combinationEntity.setX(x.toString());
        combinationEntity.setY(y.toString());
        combinationEntity.setIterations(iterations);

        combinationEntity.setStatusCode(getStatusCode(combinationEntity));

        //sign
        ByteSerializationFields serialization = new ByteSerializationFields(combinationEntity);
        ByteArrayOutputStream baos = serialization.getBaos();

        String signature = cipherSuite.signPkcs15(privateKey, serialization.getBaos().toByteArray());
        combinationEntity.setSignatureValue(signature);

        //outputvalue
        baos.write(serialization.byteSerializeSig(signature).toByteArray());
        String output = cipherSuite.getDigest(baos.toByteArray());
        combinationEntity.setOutputValue(output);

        combinationRepository.saveAndFlush(combinationEntity);

        CombinationResultDto combinationResultDto = new CombinationResultDto(combinationEntity.getTimeStamp().toString(), combinationEntity.getOutputValue(), combinationEntity.getUri());
        sendToUnicorn(combinationResultDto);

        log.warn("Stop run:");
    }

    protected void sendToUnicorn(CombinationResultDto combinationResultDto) throws Exception {
        if (!vdfUnicornService.isOpen()){
            return;
        }

        log.warn(combinationResultDto.toString());
        seedCombinationResult.setCombinationResultDto(combinationResultDto);
        vdfUnicornService.endTimeSlot();
    }

    private int getStatusCode(CombinationEntity currentEntity){
        int statusCode;

        Long maxId = combinationRepository.findMaxId();
        Optional<CombinationEntity> previewsPulse = combinationRepository.findById(maxId);

        if (previewsPulse.isPresent()){
            ZonedDateTime previewPulseTimeStamp = previewsPulse.get().getTimeStamp();
            ZonedDateTime currentPulseTimesptamp = currentEntity.getTimeStamp();

            long betweenInMinutes = ChronoUnit.MINUTES.between(previewPulseTimeStamp, currentPulseTimesptamp);
            if (betweenInMinutes != 10L){
                statusCode = 2;
            } else {
                statusCode = 0;
            }
        } else {
            statusCode = 1;
        }
        return statusCode;
    }

}
