import java.sql.*;
import java.util.Scanner;

public class sqlConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "";



        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");
            Scanner scanner = new Scanner(System.in);
            
            while(true) {
                System.out.println("What would you like to do?");
                System.out.println("1. Print all products");
                System.out.println("2. Print all ingredients for a chosen product");
                System.out.println("3. Print a chosen ingredients research data");
                System.out.println("4. Insert a new product");
                System.out.println("5. Update a product");
                System.out.println("6. Delete a product");
                System.out.println("100. Exit");
                int choice = scanner.nextInt();
    
                String selectQuery;
                ResultSet resultSet;
                Statement statement;
                PreparedStatement preparedStatement;
    
    
                if (choice == 1) {
                    // prints all data from the products table
                    selectQuery = "SELECT * FROM products";
                    statement = connection.createStatement();
        
                    resultSet = statement.executeQuery(selectQuery);
                    printAllData(resultSet);
                }
                else if (choice == 2) {
                    // prints all ingredients for a chosen product

                    // prints all products
                    // get the product id from the user
                    // get all ingredient ids for the product id
                    // get all ingredient names for the ingredient ids
                }
                else if (choice == 3) {
                    //prints a chosen ingredients research data

                    // prints all ingredients
                    // get the ingredient id from the user
                    // get all research data for the ingredient id
                }
                else if (choice == 4) {
                    // insert a new product - provide name and generate id

                    // insert a new product - provide name and generate id
                    // select ingredients - check if ingredient exists, if not, add it to the database
                    // add the product to the database
                    // add the ingredients to the product_ingredients table
                }
                else if (choice == 5) {
                    // update a product - provide name and id

                    // update a product - provide name and id
                    // select ingredients - check if ingredient exists, if not, add it to the database
                    // update the product in the database
                    // update the ingredients in the product_ingredients table
                }
                else if (choice == 6) {
                    // delete a product - provide id

                    // delete a product - provide id
                    // delete the product from the database
                    // delete the ingredients from the product_ingredients table
                }
                else {
                    System.out.println("Exiting...");
                    break;
                }
                scanner.nextLine();
    
    
    
    
                // prints data from products table based on the id
                // selectQuery = "SELECT * FROM products WHERE ProductID = ?";
                // preparedStatement = connection.prepareStatement(selectQuery);
                // System.out.println("Enter the ProductID you want to search for: ");
                // int id = scanner.nextInt();
                // preparedStatement.setInt(1, id);
                // resultSet = preparedStatement.executeQuery();
                // if (resultSet.next()) {
                //     String name = resultSet.getString("ProductName");
                //     System.out.println("ID: " + id + ", Name: " + name);
                // } else {
                //     System.out.println("No product found with ID: " + id);
                // }
    
            }
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static void printAllData(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("ProductID");
            String name = rs.getString("ProductName");
            System.out.println("ID: " + id + ", Name: " + name);
        }
    }
}