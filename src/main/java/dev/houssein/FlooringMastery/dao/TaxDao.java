package dev.houssein.FlooringMastery.dao;

import dev.houssein.FlooringMastery.dto.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxDao extends JpaRepository<Tax, String> {

    Optional<Tax> findByStateAbbreviation(String stateAbbreviation);
}
