package br.gov.inmetro.beacon.input.randomness.domain.entropy;

public interface IEntropySource {
    EntropySourceDto getNoise512Bits() throws Exception;
}
