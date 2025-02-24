package dev.houssein.FlooringMastery.service;

import dev.houssein.FlooringMastery.dao.OrderDao;
import dev.houssein.FlooringMastery.dao.ProductDao;
import dev.houssein.FlooringMastery.dao.TaxDao;
import dev.houssein.FlooringMastery.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

}
