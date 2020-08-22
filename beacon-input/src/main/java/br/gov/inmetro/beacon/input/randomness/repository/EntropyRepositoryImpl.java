package br.gov.inmetro.beacon.input.randomness.repository;

import br.gov.inmetro.beacon.input.randomness.domain.repository.Entropies;
import br.gov.inmetro.beacon.input.randomness.infra.Entropy;
import br.gov.inmetro.beacon.input.randomness.domain.EntropyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class EntropyRepositoryImpl implements IEntropyRepository {

    private final Entropies entropies;

    private final Environment env;

    @Autowired
    public EntropyRepositoryImpl(Entropies entropies, Environment env) {
        this.entropies = entropies;
        this.env = env;
    }

    @Override
    @Transactional
    public Entropy save(EntropyDto dto) {
        Entropy entropy = new Entropy();

        entropy.setRawData(dto.getRawData());
        entropy.setPeriod(Integer.parseInt(env.getProperty("beacon.period")));
        entropy.setTimeStamp(ZonedDateTime.parse(dto.getTimeStamp(), DateTimeFormatter.ISO_DATE_TIME));
        entropy.setDeviceDescription(dto.getDescription());
        entropy.setNoiseSource(dto.getNoiseSource());

        return entropies.save(entropy);
    }

    @Transactional
    public void sent(Long id, boolean value){
        entropies.save(entropies.findById(id).get().sentRemote(value));
    }

    @Transactional
    public void sentDto(List<EntropyDto> notSent){
        notSent.forEach(entropy -> entropies.findById(entropy.getId()).get().setSent(true));
    }

    @Transactional(readOnly = true)
    public List<EntropyDto> getNotSentDto(){
        List<EntropyDto> list = new ArrayList<>();
        entropies.findBySentOrderById(false).forEach(entropy -> list.add(new EntropyDto(entropy)));
        return Collections.unmodifiableList(list);
    }

}
