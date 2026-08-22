package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewIceCream {
    public void viewIceCream() {
        String sql = "SELECT m.item_id, c.category_name, m.item_name, m.flavour, "
                   + "m.price, m.quantity, m.description, m.status "
                   + "FROM menu_items m JOIN menu_categories c ON m.category_id=c.category_id "
                   + "ORDER BY c.category_name, m.item_name";

        try (Connection con = DBConnecton.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n======================================== MENU ========================================");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("--------------------------------------------");
                System.out.println("ID          : " + rs.getInt(1));
                System.out.println("Category    : " + safe(rs.getString(2)));
                System.out.println("Item        : " + safe(rs.getString(3)));
                System.out.println("Flavour     : " + safe(rs.getString(4)));
                System.out.printf("Price       : ₹%.2f%n", rs.getDouble(5));
                System.out.println("Stock       : " + rs.getInt(6));
                System.out.println("Description : " + safe(rs.getString(7)));
                System.out.println("Status      : " + safe(rs.getString(8)));
            }

            if (!found) {
                System.out.println("No menu items found.");
            }

            System.out.println("=====================================================================================");
        } catch (Exception e) {
            System.out.println("Unable to display menu: " + e.getMessage());
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }
}
