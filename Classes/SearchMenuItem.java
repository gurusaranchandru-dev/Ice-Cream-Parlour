package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class SearchMenuItem {
    public void searchMenuItem(Scanner sc) {
        System.out.print("Enter item ID or name/flavour keyword: ");
        String value=sc.nextLine().trim();
        String sql="SELECT m.item_id,c.category_name,m.item_name,m.flavour,m.price,m.quantity,m.description,m.origin,m.raw_materials,m.status " +
                   "FROM menu_items m JOIN menu_categories c ON m.category_id=c.category_id " +
                   "WHERE CAST(m.item_id AS CHAR)=? OR m.item_name LIKE ? OR m.flavour LIKE ? ORDER BY m.item_name";
        try(Connection con=DBConnecton.getConnection(); PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,value); ps.setString(2,"%"+value+"%"); ps.setString(3,"%"+value+"%");
            try(ResultSet rs=ps.executeQuery()){
                boolean found=false;
                while(rs.next()){ found=true; printItem(rs); }
                if(!found) System.out.println("No matching menu item found.");
            }
        }catch(Exception e){System.out.println("Search failed: "+e.getMessage());}
    }
    static void printItem(ResultSet rs) throws Exception {
        System.out.println("\n========================================");
        System.out.println("ID            : "+rs.getInt(1));
        System.out.println("Category      : "+rs.getString(2));
        System.out.println("Name          : "+rs.getString(3));
        System.out.println("Flavour       : "+rs.getString(4));
        System.out.printf("Price         : ₹%.2f%n",rs.getDouble(5));
        System.out.println("Quantity      : "+rs.getInt(6));
        System.out.println("Status        : "+rs.getString(10));
        System.out.println("Description   : "+rs.getString(7));
        System.out.println("Origin        : "+rs.getString(8));
        System.out.println("Raw Materials : "+rs.getString(9));
        System.out.println("========================================");
    }
}
