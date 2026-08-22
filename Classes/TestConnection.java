package IceCream;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection con = DBConnecton.getConnection()) {
            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.out.println("Database connection failed.");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
