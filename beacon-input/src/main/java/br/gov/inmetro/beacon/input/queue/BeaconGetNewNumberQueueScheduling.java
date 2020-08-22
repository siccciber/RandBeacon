package br.gov.inmetro.beacon.input.queue;

import br.gov.inmetro.beacon.input.randomness.application.IEntropyAppService;
import br.gov.inmetro.beacon.input.randomness.domain.EntropyDto;
import br.gov.inmetro.beacon.input.randomness.infra.Entropy;
import br.gov.inmetro.beacon.input.randomness.repository.IEntropyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class BeaconGetNewNumberQueueScheduling {

    private final BeaconEntropyQueueSender beaconQueueSender;

    private final IEntropyAppService entropyAppService;

    private final IEntropyRepository entropyRepository;

    private static final Logger logger = LoggerFactory.getLogger(BeaconGetNewNumberQueueScheduling.class);

    @Autowired
    public BeaconGetNewNumberQueueScheduling(BeaconEntropyQueueSender orderQueueSender,
                                             IEntropyAppService entropyAppService,
                                             IEntropyRepository entropyService) {
        this.beaconQueueSender = orderQueueSender;
        this.entropyAppService = entropyAppService;
        this.entropyRepository = entropyService;
    }

    @Scheduled(cron = "50 * * * * *")
    public void runRegular() throws Exception {
        EntropyDto noiseDto = entropyAppService.getNoise512Bits();
        Entropy saved = entropyRepository.save(noiseDto);

        try {
            beaconQueueSender.sendRegular(noiseDto);
            logger.warn(noiseDto.toString());

        } catch (Exception e){
            entropyRepository.sent(saved.getId(), false);
            e.printStackTrace();
        }

    }

    @Scheduled(cron = "51 * * * * *")
    public void runSync() {
        List<EntropyDto> notSentDto = entropyRepository.getNotSentDto();

        if (notSentDto.isEmpty()) return;

        try {
            beaconQueueSender.sendSync(notSentDto);
            entropyRepository.sentDto(notSentDto);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
