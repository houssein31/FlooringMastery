package dev.houssein.FlooringMastery.service;

import dev.houssein.FlooringMastery.dao.OrderDao;
import dev.houssein.FlooringMastery.dao.ProductDao;
import dev.houssein.FlooringMastery.dao.TaxDao;
import dev.houssein.FlooringMastery.dto.Order;
import dev.houssein.FlooringMastery.dto.Product;
import dev.houssein.FlooringMastery.dto.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private TaxDao taxDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**********************************************************************************
     *                               GET ORDER BY DATE TESTS                          *
     **********************************************************************************/

    @Test
    public void testGetOrdersByDate_OrdersFound() {

        LocalDate date = LocalDate.of(2023, 10, 1);
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> expectedOrders = Arrays.asList(order1, order2);

        when(orderDao.findByOrderDate(date)).thenReturn(expectedOrders);

        //Act
        List<Order> actualOrders = orderService.getOrdersByDate(date);

        //Assert
        assertEquals(expectedOrders, actualOrders);
        verify(orderDao, times(1)).findByOrderDate(date);
    }

    @Test
    public void testGetOrdersByDate_NoOrdersFound() {

        LocalDate date = LocalDate.of(2023, 10, 1);

        when(orderDao.findByOrderDate(date)).thenReturn(Collections.emptyList());

        //Act
        List<Order> actualOrders = orderService.getOrdersByDate(date);

        //Assert
        assertTrue(actualOrders.isEmpty());
        verify(orderDao, times(1)).findByOrderDate(date);
    }

    @Test
    public void testGetOrdersByDate_NullDate() {

        assertThrows(IllegalArgumentException.class, () -> orderService.getOrdersByDate(null));
    }

    @Test
    public void testGetOrderByDate_FutureDate() {

        LocalDate futureDate = LocalDate.now().plusDays(1);

        when(orderDao.findByOrderDate(futureDate)).thenReturn(Collections.emptyList());

        //Act
        List<Order> actualOrders = orderService.getOrdersByDate(futureDate);

        //Assert
        assertTrue(actualOrders.isEmpty());
        verify(orderDao, times(1)).findByOrderDate(futureDate);

    }

    /**********************************************************************************
     *                               ADD ORDER TESTS                                  *
     **********************************************************************************/

    @Test
    public void testAddOrder_ValidOrder() {
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setStateAbbreviation("TX");
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(150));

        Tax tax = new Tax("TX", "Texas",
                BigDecimal.valueOf(8.25));
        Product product = new Product("Wood", BigDecimal.valueOf(5.00), BigDecimal.valueOf(2.50));

        when(taxDao.findByStateAbbreviation("TX")).thenReturn(Optional.of(tax));
        when(productDao.findByProductType("Wood")).thenReturn(Optional.of(product));
        when(orderDao.save(order)).thenReturn(order);

        Order savedOrder = orderService.addOrder(order);

        assertNotNull(savedOrder);
        verify(orderDao, times(1)).save(order);
    }

    @Test
    public void testAddOrder_NullOrder() {
        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(null));
    }

    @Test
    public void testAddOrder_NullOrEmptyCustomerName() {
        Order order = new Order();
        order.setCustomerName("");
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setArea(BigDecimal.valueOf(150));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("Customer name is required.", exception.getMessage());
    }

    @Test
    public void testAddOrder_PastOrderDate() {
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now().minusDays(1));
        order.setArea(BigDecimal.valueOf(150));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("Order date cannot be in the past.", exception.getMessage());
    }

    @Test
    public void testAddOrder_OrderAreaBelowMinimum() {
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setArea(BigDecimal.valueOf(99));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("Order area must be at least 100 sq ft.", exception.getMessage());
    }

    @Test
    public void testAddOrder_InvalidStateAbbreviation() {
        Order order = new Order();
        order.setStateAbbreviation("ZZ");
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setCustomerName("John Doe");
        order.setArea(BigDecimal.valueOf(150));

        when(taxDao.findByStateAbbreviation("ZZ")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("State not found.", exception.getMessage());
    }

    @Test
    public void testAddOrder_InvalidProductType() {
        Order order = new Order();
        order.setProductType("Unknown");
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setCustomerName("John Doe");
        order.setArea(BigDecimal.valueOf(150));
        order.setStateAbbreviation("TX"); // Add a valid state abbreviation

        Tax tax = new Tax("TX", "Texas", BigDecimal.valueOf(8.25));
        when(taxDao.findByStateAbbreviation("TX")).thenReturn(Optional.of(tax)); // Mock taxDao to prevent "State not found."
        when(productDao.findByProductType("Unknown")).thenReturn(Optional.empty()); // Keep mocking the product lookup failure

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("Product not found.", exception.getMessage());
    }

    @Test
    public void testAddOrder_DatabaseSaveFailure() {
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setArea(BigDecimal.valueOf(150));

        when(orderDao.save(order)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> orderService.addOrder(order));
    }

    @Test
    public void testUpdateOrder_ValidOrder() {
        Order order = new Order();
        order.setOrderNumber(1);
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setStateAbbreviation("TX");
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(150));

        Tax tax = new Tax("TX", "Texas", BigDecimal.valueOf(8.25)); // Mocked tax entry
        Product product = new Product("Wood", BigDecimal.valueOf(5.00), BigDecimal.valueOf(4.50)); // Mocked product entry

        when(orderDao.existsById(1)).thenReturn(true);
        when(orderDao.save(order)).thenReturn(order);
        when(taxDao.findByStateAbbreviation("TX")).thenReturn(Optional.of(tax)); // Mock taxDao
        when(productDao.findByProductType("Wood")).thenReturn(Optional.of(product)); // Mock productDao

        Order updatedOrder = orderService.updateOrder(order);

        assertNotNull(updatedOrder);
        verify(orderDao, times(1)).save(order);
    }

    @Test
    public void testUpdateOrder_OrderDoesNotExist() {
        Order order = new Order();
        order.setOrderNumber(1);

        when(orderDao.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(order));
        assertEquals("Order does not exist.", exception.getMessage());
    }

    @Test
    public void testGetOrderById_OrderExists() {
        Order order = new Order();
        order.setOrderNumber(1);

        when(orderDao.findById(1)).thenReturn(Optional.of(order));

        Order retrievedOrder = orderService.getOrderById(1);

        assertNotNull(retrievedOrder);
        assertEquals(1, retrievedOrder.getOrderNumber());
    }

    @Test
    public void testGetOrderById_OrderDoesNotExist() {
        when(orderDao.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(1));
        assertEquals("Order with ID 1 not found.", exception.getMessage());
    }

    @Test
    public void testRemoveOrder_OrderExists() {
        when(orderDao.existsById(1)).thenReturn(true);
        doNothing().when(orderDao).deleteById(1);

        orderService.removeOrder(1);

        verify(orderDao, times(1)).deleteById(1);
    }

    @Test
    public void testRemoveOrder_OrderDoesNotExist() {
        when(orderDao.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.removeOrder(1));
        assertEquals("Order does not exist.", exception.getMessage());
    }

    @Test
    public void testValidateOrder_InvalidCustomerName() {
        Order order = new Order();
        order.setCustomerName("");  // Invalid
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setArea(BigDecimal.valueOf(150));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("Customer name is required.", exception.getMessage());
    }

    @Test
    public void testCalculateOrderCosts_InvalidState() {
        Order order = new Order();
        order.setStateAbbreviation("ZZ");  // Invalid state
        order.setOrderDate(LocalDate.now().plusDays(1));
        order.setCustomerName("John Doe");
        order.setArea(BigDecimal.valueOf(150));

        when(taxDao.findByStateAbbreviation("ZZ")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
        assertEquals("State not found.", exception.getMessage());
    }


    @Test
    public void testCalculateOrderCosts_ValidOrder() {
        Order order = new Order();
        order.setStateAbbreviation("TX");
        order.setProductType("Wood");
        order.setArea(BigDecimal.valueOf(150));

        Tax tax = new Tax("TX", "Texas", BigDecimal.valueOf(8.25));
        Product product = new Product("Wood", BigDecimal.valueOf(5.00), BigDecimal.valueOf(2.50));

        when(taxDao.findByStateAbbreviation("TX")).thenReturn(Optional.of(tax));
        when(productDao.findByProductType("Wood")).thenReturn(Optional.of(product));

        orderService.calculateOrderCosts(order);

        assertNotNull(order.getTotal());
        assertNotNull(order.getTax());
        assertNotNull(order.getMaterialCost());
        assertNotNull(order.getLaborCost());
    }

}
