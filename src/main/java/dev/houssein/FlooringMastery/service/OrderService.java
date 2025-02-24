package dev.houssein.FlooringMastery.service;

import dev.houssein.FlooringMastery.dao.OrderDao;
import dev.houssein.FlooringMastery.dao.ProductDao;
import dev.houssein.FlooringMastery.dao.TaxDao;
import dev.houssein.FlooringMastery.dto.Order;
import dev.houssein.FlooringMastery.dto.Product;
import dev.houssein.FlooringMastery.dto.Tax;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private final OrderDao orderDao;
    private final TaxDao taxDao;
    private final ProductDao productDao;

    @Autowired
    public OrderService(OrderDao orderDao, TaxDao taxDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
    }

    public List<Order> getOrdersByDate(LocalDate date) {

        if(date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return orderDao.findByOrderDate(date);
    }

    @Transactional
    public Order addOrder(Order order) {
        validateOrder(order);
        calculateOrderCosts(order);
        return orderDao.save(order);
    }

    @Transactional
    public Order updateOrder(Order order) {
        if (!orderDao.existsById(order.getOrderNumber())) {
            throw new IllegalArgumentException("Order does not exist.");
        }
        validateOrder(order);
        calculateOrderCosts(order);
        return orderDao.save(order);
    }

    @Transactional
    public Order getOrderById(Integer orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with ID " + orderId + " not found."));
    }

    @Transactional
    public void removeOrder(Integer orderId) {
        if (!orderDao.existsById(orderId)) {
            throw new IllegalArgumentException("Order does not exist.");
        }
        orderDao.deleteById(orderId);
    }

    private void validateOrder(Order order) {
        if (order.getCustomerName() == null || order.getCustomerName().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (order.getOrderDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Order date cannot be in the past.");
        }
        if (order.getArea().compareTo(BigDecimal.valueOf(100)) < 0) {
            throw new IllegalArgumentException("Order area must be at least 100 sq ft.");
        }
    }

    private void calculateOrderCosts(Order order) {
        Tax tax = taxDao.findByStateAbbreviation(order.getStateAbbreviation())
                .orElseThrow(() -> new IllegalArgumentException("State not found."));
        Product product = productDao.findByProductType(order.getProductType())
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        BigDecimal materialCost = order.getArea().multiply(product.getCostPerSquareFoot());
        BigDecimal laborCost = order.getArea().multiply(product.getLaborCostPerSquareFoot());
        BigDecimal taxAmount = (materialCost.add(laborCost)).multiply(tax.getTaxRate().divide(BigDecimal.valueOf(100)));
        BigDecimal total = materialCost.add(laborCost).add(taxAmount);

        order.setTaxRate(tax.getTaxRate());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(taxAmount);
        order.setTotal(total);
    }
}
