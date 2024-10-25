package dev.houssein.FlooringMastery.dao;

import dev.houssein.FlooringMastery.dto.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    // Method name is derived from the entity field name
    List<Order> findByOrderDate(LocalDate date);

}
