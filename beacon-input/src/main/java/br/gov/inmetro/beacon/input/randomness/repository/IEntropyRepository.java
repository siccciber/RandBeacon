package br.gov.inmetro.beacon.input.randomness.repository;

import br.gov.inmetro.beacon.input.randomness.infra.Entropy;
import br.gov.inmetro.beacon.input.randomness.domain.EntropyDto;

import java.util.List;

public interface IEntropyRepository {

    Entropy save(EntropyDto noiseDto);

    void sent(Long id, boolean value);

    void sentDto(List<EntropyDto> notSent);

//    void sent(List<Entropy> notSent);
//
//    List<Entropy> getNotSent();

    List<EntropyDto> getNotSentDto();

}
