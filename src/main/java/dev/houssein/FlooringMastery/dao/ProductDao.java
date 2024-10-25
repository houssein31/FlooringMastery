package dev.houssein.FlooringMastery.dao;

import dev.houssein.FlooringMastery.dto.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, String> {

    Optional<Product> findByProductType(String productType);
}
