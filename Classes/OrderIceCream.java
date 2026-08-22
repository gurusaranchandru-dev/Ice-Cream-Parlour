package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderIceCream {
    private final Bill bill = new Bill();

    private static class CartLine {
        int itemId;
        String name;
        String flavour;
        String description;
        double price;
        int quantity;

        CartLine(int id, String n, String f, String d, double p, int q) {
            itemId = id;
            name = n;
            flavour = f;
            description = d;
            price = p;
            quantity = q;
        }

        double subtotal() {
            return price * quantity;
        }
    }

    public void orderIceCream(Scanner sc) {
        List<CartLine> cart = new ArrayList<>();

        try (Connection con = DBConnecton.getConnection()) {
            while (true) {
                System.out.println("\n================ MENU CATEGORIES ================");
                List<Integer> ids = showCategories(con);
                System.out.println("0. Checkout");
                System.out.print("Select category ID: ");

                int categoryId = Integer.parseInt(sc.nextLine().trim());

                if (categoryId == 0) {
                    break;
                }

                if (!ids.contains(categoryId)) {
                    System.out.println("Invalid category.");
                    continue;
                }

                browseCategory(con, sc, categoryId, cart);
            }

            if (cart.isEmpty()) {
                System.out.println("Cart is empty. Order cancelled.");
                return;
            }

            showCart(cart);
            System.out.print("Confirm order? (Y/N): ");
            String confirm = sc.nextLine().trim();

            if (!(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes"))) {
                System.out.println("Order cancelled.");
                return;
            }

            int orderId = saveOrder(con, cart);
            System.out.println("Order confirmed successfully. Order ID: " + orderId);
            bill.generateBill(orderId);

        } catch (Exception e) {
            System.out.println("Ordering failed: " + e.getMessage());
        }
    }

    private List<Integer> showCategories(Connection con) throws Exception {
        List<Integer> ids = new ArrayList<>();

        String sql = "SELECT c.category_id, c.category_name, COUNT(m.item_id) "
                   + "FROM menu_categories c "
                   + "LEFT JOIN menu_items m ON c.category_id=m.category_id "
                   + "AND m.status='AVAILABLE' AND m.quantity>0 "
                   + "WHERE c.status='ACTIVE' "
                   + "GROUP BY c.category_id,c.category_name "
                   + "ORDER BY c.category_name";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(1);
                ids.add(id);
                System.out.printf("%d. %-30s (%d available)%n",
                        id, rs.getString(2), rs.getInt(3));
            }
        }

        return ids;
    }

    private void browseCategory(Connection con, Scanner sc, int categoryId,
                                List<CartLine> cart) throws Exception {

        String categoryName = "";

        try (PreparedStatement cp = con.prepareStatement(
                "SELECT category_name FROM menu_categories WHERE category_id=?")) {
            cp.setInt(1, categoryId);

            try (ResultSet cr = cp.executeQuery()) {
                if (cr.next()) {
                    categoryName = cr.getString(1);
                }
            }
        }

        while (true) {
            System.out.println("\n================ " + categoryName.toUpperCase() + " ================");

            String sql = "SELECT item_id,item_name,flavour,price,quantity,description "
                       + "FROM menu_items "
                       + "WHERE category_id=? AND status='AVAILABLE' AND quantity>0 "
                       + "ORDER BY item_name";

            List<Integer> itemIds = new ArrayList<>();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, categoryId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int itemId = rs.getInt(1);
                        itemIds.add(itemId);

                        System.out.println("--------------------------------------------");
                        System.out.println("ID          : " + itemId);
                        System.out.println("Item        : " + safe(rs.getString(2)));
                        System.out.println("Flavour     : " + safe(rs.getString(3)));
                        System.out.printf("Price       : ₹%.2f%n", rs.getDouble(4));
                        System.out.println("Available   : " + rs.getInt(5));
                        System.out.println("Description : " + safe(rs.getString(6)));
                    }
                    System.out.println("--------------------------------------------");
                }
            }

            System.out.println("0. Back to Categories");
            System.out.print("Item ID: ");
            int itemId = Integer.parseInt(sc.nextLine().trim());

            if (itemId == 0) {
                return;
            }

            if (!itemIds.contains(itemId)) {
                System.out.println("Invalid item for this category.");
                continue;
            }

            addItem(con, sc, itemId, cart);
        }
    }

    private void addItem(Connection con, Scanner sc, int itemId,
                          List<CartLine> cart) throws Exception {

        String sql = "SELECT item_name,flavour,price,quantity,description "
                   + "FROM menu_items WHERE item_id=? AND status='AVAILABLE'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Item unavailable.");
                    return;
                }

                String name = rs.getString(1);
                String flavour = rs.getString(2);
                double price = rs.getDouble(3);
                int stock = rs.getInt(4);
                String description = rs.getString(5);

                System.out.println("\nSelected Item");
                System.out.println("Item        : " + safe(name));
                System.out.println("Flavour     : " + safe(flavour));
                System.out.printf("Price       : ₹%.2f%n", price);
                System.out.println("Description : " + safe(description));

                System.out.print("Quantity: ");
                int q = Integer.parseInt(sc.nextLine().trim());

                if (q <= 0 || q > stock) {
                    System.out.println("Quantity must be between 1 and " + stock + ".");
                    return;
                }

                for (CartLine line : cart) {
                    if (line.itemId == itemId) {
                        if (line.quantity + q > stock) {
                            System.out.println("Total requested exceeds stock.");
                            return;
                        }

                        line.quantity += q;
                        System.out.println("Cart updated: " + name + " x " + line.quantity);
                        return;
                    }
                }

                cart.add(new CartLine(itemId, name, flavour, description, price, q));
                System.out.println("Added to cart: " + name + " x " + q);
            }
        }
    }

    private void showCart(List<CartLine> cart) {
        double total = 0;

        System.out.println("\n================ YOUR CART ================");

        for (CartLine l : cart) {
            total += l.subtotal();
            System.out.println("--------------------------------------------");
            System.out.println("Item        : " + safe(l.name));
            System.out.println("Flavour     : " + safe(l.flavour));
            System.out.println("Description : " + safe(l.description));
            System.out.println("Quantity    : " + l.quantity);
            System.out.printf("Unit Price  : ₹%.2f%n", l.price);
            System.out.printf("Subtotal    : ₹%.2f%n", l.subtotal());
        }

        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL: ₹%.2f%n", total);
    }

    private int saveOrder(Connection con, List<CartLine> cart) throws Exception {
        double total = cart.stream().mapToDouble(CartLine::subtotal).sum();
        boolean old = con.getAutoCommit();
        con.setAutoCommit(false);

        try {
            int orderId;

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO orders(total_amount,order_status) VALUES(?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                ps.setDouble(1, total);
                ps.setString(2, "CONFIRMED");
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new Exception("Could not create order.");
                    }
                    orderId = rs.getInt(1);
                }
            }

            try (PreparedStatement itemPs = con.prepareStatement(
                    "INSERT INTO order_items(order_id,item_id,quantity,unit_price,subtotal) VALUES(?,?,?,?,?)");
                 PreparedStatement stockPs = con.prepareStatement(
                    "UPDATE menu_items SET quantity=quantity-?, "
                  + "status=CASE WHEN quantity-?<=0 THEN 'OUT_OF_STOCK' ELSE 'AVAILABLE' END "
                  + "WHERE item_id=? AND quantity>=?")) {

                for (CartLine l : cart) {
                    itemPs.setInt(1, orderId);
                    itemPs.setInt(2, l.itemId);
                    itemPs.setInt(3, l.quantity);
                    itemPs.setDouble(4, l.price);
                    itemPs.setDouble(5, l.subtotal());
                    itemPs.executeUpdate();

                    stockPs.setInt(1, l.quantity);
                    stockPs.setInt(2, l.quantity);
                    stockPs.setInt(3, l.itemId);
                    stockPs.setInt(4, l.quantity);

                    if (stockPs.executeUpdate() != 1) {
                        throw new Exception("Stock changed for " + l.name + ". Please retry.");
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO bills(order_id,grand_total) VALUES(?,?)")) {
                ps.setInt(1, orderId);
                ps.setDouble(2, total);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(old);
            return orderId;

        } catch (Exception e) {
            con.rollback();
            con.setAutoCommit(old);
            throw e;
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }
}
