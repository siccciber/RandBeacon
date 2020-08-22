package br.gov.inmetro.beacon.input.randomness.domain.entropy;

import lombok.Getter;

@Getter
public class EntropySourceDto {

    private final String rawData;

    private final String deviceDescription;

    public EntropySourceDto(String rawData, String deviceDescription) {
        this.rawData = rawData;
        this.deviceDescription = deviceDescription;
    }
}
