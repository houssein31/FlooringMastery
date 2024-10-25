package dev.houssein.FlooringMastery.service;

import dev.houssein.FlooringMastery.dao.ProductDao;
import dev.houssein.FlooringMastery.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Optional<Product> findByProductType(String productType) {
        return productDao.findByProductType(productType);
    }
}
