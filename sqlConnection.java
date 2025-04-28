import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class sqlConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mcdonaldstest";
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
                ArrayList<Integer> IDs = new ArrayList<>();
    
    
                if (choice == 1) {
                    // prints all data from the products table
                    printAllProducts(connection);
                }
                else if (choice == 2) {
                    // prints all ingredients for a chosen product

                    // prints all products
                    printAllProducts(connection);   
                    // get the product id from the user
                    System.out.println("Enter the product id that you would like to see the ingredients for: ");
                    int productId = scanner.nextInt();
                    // get all ingredient ids for the product id
                    getIngredientIDs(connection, productId, IDs);
                    // get all ingredient names for the ingredient ids
                    printIngredients(connection, IDs);
                }
                else if (choice == 3) {
                    //prints a chosen ingredients research data

                    // prints all ingredients
                    printAllIngredients(connection);
                    // get the ingredient id from the user
                    System.out.println("Enter the ingredient id that you would like to see the research data for: ");
                    int ingredientId = scanner.nextInt();
                    // get all research data for the ingredient id
                    printResearchData(connection, ingredientId);
                }
                else if (choice == 4) {
                    // insert a new product - provide name and generate id

                    // insert a new product - provide name and generate id
                    System.out.println("Enter the product name: ");
                    String productName = scanner.next();
                    int newProdID = insertProduct(connection, productName);
                    // select ingredients - check if ingredient exists, if not, add it to the database
                    printAllIngredients(connection);
                    boolean continueLoop = true;
                    while (continueLoop) { 
                        System.out.println("Enter the ingredient id that you would like to add to the product: ");
                        System.out.println("50. Create new ingredient");
                        System.out.println("100. Exit");
                        int ingredientId = scanner.nextInt();
                        if (ingredientId == 50){
                            System.out.println("Enter the new ingredient name: ");
                            String ingredientName = scanner.next();

                            insertIngredient(connection, ingredientName);
                        }
                        else if (ingredientId == 100) {
                            continueLoop = false;
                        }
                        else {
                            boolean exists = ingredientExists(connection, ingredientId);
                            while (!exists)
                            {
                                System.out.println("Ingredient does not exist");
                                ingredientId = scanner.nextInt();
                                exists = ingredientExists(connection, ingredientId);
                            }
                            IDs.add(ingredientId);
                        }
                    }

                    // add the ingredients to the product_ingredients table
                    insertProductIngredient(connection, newProdID, IDs);
                }
                else if (choice == 5) {
                    // update a product - provide name and id

                    // update a product - provide name and id
                    printAllProducts(connection);
                    System.out.println("What is the id of the product you want to update?");
                    int id = scanner.nextInt();
                    boolean exists = productIDExists(connection, id);
                    while (!exists) {
                        productIDExists(connection, id);
                        System.out.println("Product doesn't exist, enter a new ID");
                        id = scanner.nextInt();
                    }
                    
                    // get all ingredient ids for the product id
                    getIngredientIDs(connection, id, IDs);
                    // get all ingredient names for the ingredient ids
                    System.out.println("Current Ingredients: ");
                    printIngredients(connection, IDs);

                    boolean continueLoop = true;
                    while (continueLoop) {
                        System.out.println("Enter an ID, if it exists it will be removed if not it will be added: ");
                        System.out.println("50. Print current ingredients");
                        System.out.println("75. Print all possible ingredients");
                        System.out.println("100. exit");
                    }



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
                    System.out.println("Connection Closed!");
                    break;
                }
                scanner.next();
            }
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            e.getMessage();
        }
    }

    public static void printAllProducts(Connection c) throws SQLException {
        String selectQuery = "SELECT * FROM products";
        Statement statement = c.createStatement();

        ResultSet rs = statement.executeQuery(selectQuery);
        while (rs.next()) {
            int id = rs.getInt("ProductID");
            String name = rs.getString("ProductName");
            System.out.println("ID: " + id + ", Name: " + name);
        }
    }

    public static int insertProduct(Connection c, String name) throws SQLException {
        String insertQuery = "INSERT INTO products (ProductID, ProductName) VALUES (?, ?)";
        PreparedStatement preparedStatement = c.prepareStatement(insertQuery);

        Random rand = new Random();
        int id = rand.nextInt(1000, 10000);
        boolean exists = productIDExists(c, id);
        while (exists) {
            id = rand.nextInt(1000, 10000);
            exists = productIDExists(c, id);
        }

        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.executeUpdate();

        return id;
    }

    public static void insertIngredient(Connection c, String name) throws SQLException {
        String insertQuery = "INSERT INTO ingredients (IngredientID, IngredientName) VALUES (?, ?)";
        PreparedStatement preparedStatement = c.prepareStatement(insertQuery);

        Random rand = new Random();
        int id = rand.nextInt(1000, 10000);
        boolean exists = productIDExists(c, id);
        while (exists) {
            id = rand.nextInt(1000, 10000);
            exists = ingredientExists(c, id);
        }

        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.executeUpdate();
    }

    public static void insertProductIngredient(Connection c, int productID, ArrayList ingredientID) throws SQLException {
        String insertQuery = "INSERT INTO product_ingredients (ProductID, IngredientID) VALUES (?, ?)";
        PreparedStatement preparedStatement = c.prepareStatement(insertQuery);

        for (int i = 0; i < ingredientID.size(); i++) {
            int inID = (int) ingredientID.get(i);

            preparedStatement.setInt(1, productID);
            preparedStatement.setInt(2, inID);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean productIDExists(Connection c, int id) throws SQLException {
        String selectQuery = "SELECT * FROM products WHERE ProductID = ?";
        PreparedStatement preparedStatement = c.prepareStatement(selectQuery);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public static void printAllIngredients(Connection c) throws SQLException {
        String selectQuery = "SELECT * FROM ingredients";
        Statement statement = c.createStatement();

        ResultSet rs = statement.executeQuery(selectQuery);
        while (rs.next()) {
            int id = rs.getInt("IngredientID");
            String name = rs.getString("IngredientName");
            System.out.println("ID: " + id + ", Name: " + name);
        }
    }

    public static void getIngredientIDs(Connection c, int id, ArrayList IDs) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String selectQuery = "SELECT * FROM product_ingredients WHERE ProductID = ?";
        PreparedStatement preparedStatement = c.prepareStatement(selectQuery);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            String ingredientID = rs.getString("IngredientID");
            int ingredientIDInt = Integer.parseInt(ingredientID);
            IDs.add(ingredientIDInt);
        }
    }

    public static void printIngredients(Connection c, ArrayList IDs) throws SQLException {
        String selectQuery = "SELECT * FROM ingredients WHERE IngredientID = ?";
        PreparedStatement preparedStatement = c.prepareStatement(selectQuery);
        for (int i = 0; i < IDs.size(); i++) {
            int id = (int) IDs.get(i);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("IngredientName");
                System.out.println("ID: " + id + ", Name: " + name);
            }
        }
    }

    public static void printResearchData(Connection c, int id) throws SQLException {
        String selectQuery = "SELECT * FROM ingredientpapers WHERE IngredientID = ?";
        PreparedStatement preparedStatement = c.prepareStatement(selectQuery);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            String paperOne = rs.getString("PaperOne");
            String paperTwo = rs.getString("PaperTwo");
            String paperThree = rs.getString("PaperThree");

            System.out.println("Research data for ingredient ID " + id + ":");
            System.out.println("Paper One: " + paperOne);
            System.out.println("Paper Two: " + paperTwo);
            System.out.println("Paper Three: " + paperThree);
        }
    }

    public static boolean ingredientExists(Connection c, int id) throws SQLException {
        String selectQuery = "SELECT * FROM ingredients WHERE IngredientID = ?";
        PreparedStatement preparedStatement = c.prepareStatement(selectQuery);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public static void updateProduct(Connection c, int id) throws SQLException {

    }
}