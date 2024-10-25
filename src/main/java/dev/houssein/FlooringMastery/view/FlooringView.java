package dev.houssein.FlooringMastery.view;

import dev.houssein.FlooringMastery.dto.Order;
import dev.houssein.FlooringMastery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
public class FlooringView {

    private final Scanner scanner = new Scanner(System.in);
    private final UserIO userIO;
    private final OrderService orderService;

    @Autowired
    public FlooringView(UserIO userIO, OrderService orderService) {
        this.userIO = userIO;
        this.orderService = orderService;
    }

    public void displayMenu() {
        userIO.print("* * * * * * * * * * * * * * * * *");
        userIO.print("* << Flooring Program >>");
        userIO.print("* 1. Display Orders");
        userIO.print("* 2. Add an Order");
        userIO.print("* 3. Edit an Order");
        userIO.print("* 4. Remove an Order");
        userIO.print("* 5. Quit");
        userIO.print("* * * * * * * * * * * * * * * * *");
    }

    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Please select an option: ");
            switch (choice) {
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    userIO.print("Invalid selection. Please try again.");
            }
        }
        quitApplication();
    }

    private void displayOrders() {
        LocalDate date = getDateInput("Enter the date for the orders (yyyy-MM-dd): ");
        List<Order> orders = orderService.getOrdersByDate(date);
        for (Order order : orders) {
            userIO.print(order.toString());
        }
    }

    private void addOrder() {
        String customerName = getStringInput("Enter customer name: ");
        LocalDate orderDate = getDateInput("Enter order date (yyyy-MM-dd): ");
        String stateAbbreviation = getStringInput("Enter state abbreviation (e.g., OH, TX): ");
        String productType = getStringInput("Enter product type: ");
        BigDecimal area = getBigDecimalInput("Enter area in square feet: "); // Single-argument version

        try {
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setOrderDate(orderDate);
            order.setStateAbbreviation(stateAbbreviation);
            order.setProductType(productType);
            order.setArea(area);

            Order createdOrder = orderService.addOrder(order);
            userIO.print("Order successfully created:");
            userIO.print(createdOrder.toString());
        } catch (IllegalArgumentException e) {
            userIO.print("Error creating order: " + e.getMessage());
        }
    }


    private void editOrder() {
        Integer orderId = getIntInput("Enter order ID to edit: ");
        Order existingOrder = orderService.getOrderById(orderId);
        if (existingOrder == null) {
            userIO.print("Order with ID " + orderId + " not found.");
            return;
        }

        // If the new customer name is blank, keep the existing name
        String customerName = getStringInput("Enter new customer name (current: " + existingOrder.getCustomerName() + "): ");
        if (customerName.isBlank()) {
            customerName = existingOrder.getCustomerName();
        }

        // If the new date is blank, keep the existing date
        LocalDate orderDate = getDateInput("Enter new order date (yyyy-MM-dd) (current: " + existingOrder.getOrderDate() + "): ", existingOrder.getOrderDate());

        // If the new state abbreviation is blank, keep the existing state abbreviation
        String stateAbbreviation = getStringInput("Enter new state abbreviation (current: " + existingOrder.getStateAbbreviation() + "): ");
        if (stateAbbreviation.isBlank()) {
            stateAbbreviation = existingOrder.getStateAbbreviation();
        }

        // If the new product type is blank, keep the existing product type
        String productType = getStringInput("Enter new product type (current: " + existingOrder.getProductType() + "): ");
        if (productType.isBlank()) {
            productType = existingOrder.getProductType();
        }

        // If the new area is blank, keep the existing area
        BigDecimal area = getBigDecimalInput("Enter new area in square feet (current: " + existingOrder.getArea() + "): ", existingOrder.getArea());

        // Set the updated values on the existing order
        existingOrder.setCustomerName(customerName);
        existingOrder.setOrderDate(orderDate);
        existingOrder.setStateAbbreviation(stateAbbreviation);
        existingOrder.setProductType(productType);
        existingOrder.setArea(area);

        try {
            Order updatedOrder = orderService.updateOrder(existingOrder);
            userIO.print("Order successfully updated:");
            userIO.print(updatedOrder.toString());
        } catch (IllegalArgumentException e) {
            userIO.print("Error updating order: " + e.getMessage());
        }
    }

    private void removeOrder() {
        Integer orderId = getIntInput("Enter order ID to remove: ");
        try {
            orderService.removeOrder(orderId);
            userIO.print("Order successfully removed.");
        } catch (IllegalArgumentException e) {
            userIO.print("Error removing order: " + e.getMessage());
        }
    }

    private void quitApplication() {
        userIO.print("Exiting application. Goodbye!");
    }

    private String getStringInput(String prompt) {
        userIO.print(prompt);
        return scanner.nextLine();
    }

    // Overload for cases where there is no current value
    private LocalDate getDateInput(String prompt) {
        userIO.print(prompt);
        String input = scanner.nextLine();
        if (input.isBlank()) {
            throw new IllegalArgumentException("Date input cannot be empty.");
        }
        return LocalDate.parse(input);
    }

    // Overload for cases where a current value is provided
    private LocalDate getDateInput(String prompt, LocalDate currentValue) {
        userIO.print(prompt);
        String input = scanner.nextLine();
        if (input.isBlank()) {
            return currentValue; // Return the current value if the input is empty
        }
        return LocalDate.parse(input);
    }

    private int getIntInput(String prompt) {
        userIO.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }

    private BigDecimal getBigDecimalInput(String prompt) {
        userIO.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            userIO.print("Invalid input. Please enter a valid decimal number.");
            return getBigDecimalInput(prompt); // Retry the input if the parsing fails
        }
    }


    private BigDecimal getBigDecimalInput(String prompt, BigDecimal currentValue) {
        userIO.print(prompt + " (current: " + currentValue + "): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            userIO.print("Using current value: " + currentValue); // Debugging purpose
            return currentValue;
        }

        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            userIO.print("Invalid input: '" + input + "'. Please enter a valid decimal number.");
            // Retry the input
            return getBigDecimalInput(prompt, currentValue);
        }
    }

}