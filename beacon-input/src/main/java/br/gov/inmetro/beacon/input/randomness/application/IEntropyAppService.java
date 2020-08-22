package br.gov.inmetro.beacon.input.randomness.application;

import br.gov.inmetro.beacon.input.randomness.domain.EntropyDto;

public interface IEntropyAppService {
    EntropyDto getNoise512Bits() throws Exception;
}
