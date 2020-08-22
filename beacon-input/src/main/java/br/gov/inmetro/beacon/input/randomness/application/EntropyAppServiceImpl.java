package br.gov.inmetro.beacon.input.randomness.application;

import br.gov.inmetro.beacon.input.randomness.domain.EntropyDto;
import br.gov.inmetro.beacon.input.randomness.domain.entropy.EntropySourceDto;
import br.gov.inmetro.beacon.input.randomness.domain.entropy.IEntropySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class EntropyAppServiceImpl implements IEntropyAppService {

    private final IEntropySource entropySource;

    private final Environment env;

    @Autowired
    public EntropyAppServiceImpl(IEntropySource entropySource, Environment env) {
        this.entropySource = entropySource;
        this.env = env;
    }

    @Override
    public EntropyDto getNoise512Bits() throws Exception {
        final EntropySourceDto entropySourceDto = entropySource.getNoise512Bits();

        return new EntropyDto(getDateTime().toString(),
                entropySourceDto.getRawData(),
                Integer.parseInt(env.getProperty("beacon.period")),
                env.getProperty("beacon.noise-source"),
                entropySourceDto.getDeviceDescription());
    }

    private ZonedDateTime getDateTime(){

        return ZonedDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plus(2, ChronoUnit.MINUTES)
                .withZoneSameInstant((ZoneOffset.UTC).normalized());
    }

}
