package dev.houssein.FlooringMastery.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderNumber;
    private String customerName;
    @Temporal(TemporalType.DATE)
    @Column(name = "order_date")
    private LocalDate orderDate;
    private String stateAbbreviation;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;

    public Order(int orderNumber, String customerName, LocalDate orderDate, String stateAbbreviation, BigDecimal taxRate, String productType, BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot, BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.stateAbbreviation = stateAbbreviation;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    public Order(String customerName, LocalDate orderDate, String stateAbbreviation, String productType, BigDecimal area) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.stateAbbreviation = stateAbbreviation;
        this.productType = productType;
        this.area = area;
    }

    public Order() {

    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber:" + orderNumber +
                ", customerName:'" + customerName + '\'' +
                ", stateAbbreviation:'" + stateAbbreviation + '\'' +
                ", taxRate: " + taxRate +
                ", productType: '" + productType + '\'' +
                ", area: " + area +
                ", costPerSquareFoot: " + costPerSquareFoot +
                ", laborCostPerSquareFoot: " + laborCostPerSquareFoot +
                ", materialCost: " + materialCost +
                ", laborCost: " + laborCost +
                ", tax: " + tax +
                ", total=" + total +
                '}';
    }
}
