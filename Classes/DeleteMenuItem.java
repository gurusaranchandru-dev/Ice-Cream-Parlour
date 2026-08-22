package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class DeleteMenuItem {
    public void deleteMenuItem(Scanner sc){
        System.out.print("Enter item ID to delete: "); int id=Integer.parseInt(sc.nextLine().trim());
        if(!confirm(sc,"Delete this item? (Y/N): ")) return;
        try(Connection con=DBConnecton.getConnection();PreparedStatement ps=con.prepareStatement("DELETE FROM menu_items WHERE item_id=?")){ps.setInt(1,id);int n=ps.executeUpdate();System.out.println(n>0?"Menu item deleted successfully.":"Menu item not found.");}
        catch(Exception e){System.out.println("Unable to delete item: "+e.getMessage());}
    }
    private boolean confirm(Scanner sc,String p){System.out.print(p);String s=sc.nextLine().trim();return s.equalsIgnoreCase("y")||s.equalsIgnoreCase("yes");}
}
