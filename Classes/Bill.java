package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Bill {
    public void generateBill(int orderId) {
        String sql = "SELECT o.order_id,o.order_date,m.item_name,m.flavour,m.description, "
                   + "oi.quantity,oi.unit_price,oi.subtotal,o.total_amount "
                   + "FROM orders o "
                   + "JOIN order_items oi ON o.order_id=oi.order_id "
                   + "JOIN menu_items m ON oi.item_id=m.item_id "
                   + "WHERE o.order_id=? ORDER BY oi.order_item_id";

        try (Connection con = DBConnecton.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                double total = 0;

                System.out.println("\n============================================");
                System.out.println("              ICE CREAM PARLOUR");
                System.out.println("                    BILL");
                System.out.println("============================================");

                while (rs.next()) {
                    if (!found) {
                        found = true;
                        System.out.println("Order ID : " + rs.getInt(1));
                        System.out.println("Date     : " + rs.getTimestamp(2));
                        System.out.println("--------------------------------------------");
                    }

                    System.out.println("Item        : " + safe(rs.getString(3)));
                    System.out.println("Flavour     : " + safe(rs.getString(4)));
                    System.out.println("Description : " + safe(rs.getString(5)));
                    System.out.println("Quantity    : " + rs.getInt(6));
                    System.out.printf("Unit Price  : ₹%.2f%n", rs.getDouble(7));
                    System.out.printf("Subtotal    : ₹%.2f%n", rs.getDouble(8));
                    System.out.println("--------------------------------------------");
                    total = rs.getDouble(9);
                }

                if (found) {
                    System.out.printf("GRAND TOTAL: ₹%.2f%n", total);
                    System.out.println("============================================");
                    System.out.println("       THANK YOU! VISIT AGAIN!");
                    System.out.println("============================================");
                } else {
                    System.out.println("Order not found.");
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to generate bill: " + e.getMessage());
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }
}
