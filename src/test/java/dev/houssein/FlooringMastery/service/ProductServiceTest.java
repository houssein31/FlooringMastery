package dev.houssein.FlooringMastery.service;

import dev.houssein.FlooringMastery.dao.ProductDao;
import dev.houssein.FlooringMastery.dto.Product;
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
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
    }

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // Initialize mocks
//    }

    @Test
    void testFindByProductType_Found() {

        // Arrange
        when(productDao.findByProductType("Tile")).thenReturn(Optional.of(product));

        // Act
        Optional<Product> result = productService.findByProductType("Tile");

        // Assert
        assertTrue(result.isPresent(), "Expected product to be found");
        assertEquals("Tile", result.get().getProductType());
        assertEquals(new BigDecimal("3.50"), result.get().getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.15"), result.get().getLaborCostPerSquareFoot());

        // Verify Interaction
        verify(productDao, times(1)).findByProductType("Tile");
    }

    @Test
    void testFindByProductType_NotFound() {

        // Arrange
        when(productDao.findByProductType("Vinyl")).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.findByProductType("Vinyl");

        // Assert
        assertFalse(result.isPresent(), "Expected product to be found");

        // Verify Interaction
        verify(productDao, times(1)).findByProductType("Vinyl");
    }
}
