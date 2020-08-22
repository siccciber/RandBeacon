package br.gov.inmetro.beacon.input.randomness.domain.repository;

import br.gov.inmetro.beacon.input.randomness.infra.Entropy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Entropies extends  JpaRepository<Entropy, Long>{
    List<Entropy> findBySentOrderById(boolean sent);
}
