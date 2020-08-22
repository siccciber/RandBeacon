package br.gov.inmetro.beacon.input.randomness.domain;

import br.gov.inmetro.beacon.input.randomness.infra.Entropy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class EntropyDto implements Serializable {

    @JsonIgnore
    private final Long id;

    private final String rawData;

    private final int period;

    private final String noiseSource;

    private final String timeStamp;

    @JsonIgnore
    private final String description;

    public EntropyDto(String timeStamp, String rawData,
                      int period, String noiseSource, String description) {
        this.id = null;
        this.timeStamp = timeStamp;
        this.rawData = rawData;
        this.period = period;
        this.noiseSource = noiseSource;
        this.description = description;
    }

    public EntropyDto(Entropy entropy){
        this.id = entropy.getId();
        this.timeStamp = entropy.getTimeStamp().toString();
        this.period = entropy.getPeriod();
        this.noiseSource = entropy.getNoiseSource();
        this.rawData = entropy.getRawData();
        this.description = entropy.getDeviceDescription();
    }

    @Override
    public String toString() {
        return "EntropyDto{" +
                "id=" + id +
                ", rawData='" + rawData + '\'' +
                ", period=" + period +
                ", noiseSource=" + noiseSource +
                ", timeStamp='" + timeStamp + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
