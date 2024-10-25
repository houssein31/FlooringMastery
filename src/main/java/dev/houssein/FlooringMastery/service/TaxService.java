package dev.houssein.FlooringMastery.service;

import dev.houssein.FlooringMastery.dao.TaxDao;
import dev.houssein.FlooringMastery.dto.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaxService {

    private final TaxDao taxDao;

    @Autowired
    public TaxService(TaxDao taxDao) {
        this.taxDao = taxDao;
    }

    public Optional<Tax> findStateByStateAbbreviation(String stateAbbreviation) {
        return taxDao.findByStateAbbreviation(stateAbbreviation);
    }
}
