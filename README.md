**Flooring Mastery**
Overview
The goal of this project is to create an application for managing flooring orders at Wiley Edge Corp. The application will use PostgreSQL and JPA to store and manage data, following a layered architecture. The project demonstrates skills in:

N-tier/MVC architecture (including the service layer)
Interfaces
Spring Dependency Injection
Unit Testing
Architectural Guidance
N-tier development and MVC principles should be followed to manage products, taxes, and orders.
Use unit and integration tests to ensure coverage of data layers and business logic.
Organize code into reasonable classes following the structure outlined below.
Project Structure
The application should follow these layers:

Model Layer: Contains classes with data members (properties).
DAO Layer: Responsible for persisting data using PostgreSQL.
Service Layer: Contains the business logic.
Controller Layer: Manages program flow.
View Layer: Handles user interactions via console input/output.
UserIO Class: Works with the view to manage console I/O.
Requirements
User Stories
The application will prompt users with a menu containing the following options:

Display Orders: Displays orders for a given date.
Add an Order: Adds a new order with calculated fields.
Edit an Order: Allows the user to modify an existing order.
Remove an Order: Deletes an order based on the date and order number.
Quit: Exits the application.
Data Files
The following data will be stored in a PostgreSQL database:

Orders: Stores order details, including customer name, state, tax rate, product type, area, costs, and totals.
Taxes: Contains state abbreviations, names, and tax rates.
Products: Holds product types, cost per square foot, and labor cost per square foot.
Data Calculations
The following calculations should be performed:

MaterialCost = Area * CostPerSquareFoot
LaborCost = Area * LaborCostPerSquareFoot
Tax = (MaterialCost + LaborCost) * (TaxRate / 100)
Total = MaterialCost + LaborCost + Tax
User Input Rules
Order Date: Must be in the future.
Customer Name: Cannot be blank and must include only alphanumeric characters, periods, and commas.
State: Must match an entry in the tax table.
Product Type: Must be selected from available products.
Area: Must be a positive decimal, with a minimum of 100 sq ft.
Order Management
Display Orders: Users can view orders for a specified date. If no orders exist, display an error.
Add an Order: Query the user for required data, calculate the order's costs, and confirm before adding.
Edit an Order: Allow modifications to customer name, state, product type, and area. Recalculate costs if changes are made.
Remove an Order: Confirm removal of the specified order.
Technical Specifications
Use PostgreSQL as the database.
Implement JPA for data persistence.
Follow the Agile Approach Checklist for Console Applications.
Include unit tests for DAO and Service layers.
Organize code using well-named classes split across packages for different layers.
Planning and Submission
Step 1 - Planning: Create a flowchart and class diagrams outlining program steps and structure.
Step 2 - Coding: Develop the code according to requirements. Submit the code via GitHub and Engage as specified.
Additional Guidance
Use prior examples as inspiration.
Follow best practices for layered application development.
Ask questions where requirements are unclear.
Testing
Include thorough unit tests to ensure data integrity and application stability.