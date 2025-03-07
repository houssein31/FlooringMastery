package dev.houssein.FlooringMastery.service;

import dev.houssein.FlooringMastery.dao.TaxDao;
import dev.houssein.FlooringMastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaxServiceTest {

    @Mock
    private TaxDao taxDao;

    @InjectMocks
    private TaxService taxService;

    private Tax tax;

    @BeforeEach
    void setUp() {
        tax = new Tax("TX", "Texas", new BigDecimal("6.25"));
    }

    @Test
    void testFindStateByStateAbbreviation_Found() {

        // Arrange
        when(taxDao.findByStateAbbreviation("TX")).thenReturn(Optional.of(tax));

        // Act
        Optional<Tax> result = taxService.findStateByStateAbbreviation("TX");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TX", result.get().getStateAbbreviation());
        assertEquals("Texas", result.get().getStateName());
        assertEquals(new BigDecimal("6.25"), result.get().getTaxRate());

        // Verify Interaction
        verify(taxDao, times(1)).findByStateAbbreviation("TX");
    }

    @Test
    void testFindStateByStateAbbreviation_NotFound() {

        // Arrange
        when(taxDao.findByStateAbbreviation("NY")).thenReturn(Optional.empty());

        // Act
        Optional<Tax> result = taxService.findStateByStateAbbreviation("NY");

        // Assert
        assertFalse(result.isPresent());

        // Verify Interaction
        verify(taxDao, times(1)).findByStateAbbreviation("NY");
    }
}
