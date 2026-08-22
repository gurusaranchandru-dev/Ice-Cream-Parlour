package IceCream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnecton {

    private static final String URL = "jdbc:mysql://localhost:3306/IceCreamParlour?useSSL=false&serverTimezone=Asia/Kolkata";
    private static final String USER = "root";
    private static final String PASSWORD = "chandru@003";

    private DBConnecton() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Connector/J is not on the Eclipse Build Path.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
