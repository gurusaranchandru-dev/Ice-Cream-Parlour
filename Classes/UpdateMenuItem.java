package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UpdateMenuItem {
    public void updateMenuItem(Scanner sc){
        int id=readInt(sc,"Item ID: ");
        try(Connection con=DBConnecton.getConnection()){
            String find="SELECT item_id,item_name,flavour,price,quantity,description,origin,raw_materials,status FROM menu_items WHERE item_id=?";
            try(PreparedStatement ps=con.prepareStatement(find)){ps.setInt(1,id);try(ResultSet rs=ps.executeQuery()){if(!rs.next()){System.out.println("Menu item not found.");return;}
                System.out.printf("Current: %s | ₹%.2f | stock %d | %s%n",rs.getString(2),rs.getDouble(4),rs.getInt(5),rs.getString(9));}}
            int categoryId=AddIceCream.selectCategory(con,sc,true); if(categoryId==0)return;
            String name=AddIceCream.readRequired(sc,"New name: "); String flavour=AddIceCream.readRequired(sc,"New flavour: ");
            double price=AddIceCream.readDouble(sc,"New price: "); int qty=AddIceCream.readNonNegativeInt(sc,"New quantity: ");
            String desc=AddIceCream.readRequired(sc,"New description: "); String origin=AddIceCream.readRequired(sc,"New origin: "); String raw=AddIceCream.readRequired(sc,"New raw materials: ");
            String sql="UPDATE menu_items SET category_id=?,item_name=?,flavour=?,price=?,quantity=?,description=?,origin=?,raw_materials=?,status=? WHERE item_id=?";
            try(PreparedStatement ps=con.prepareStatement(sql)){ps.setInt(1,categoryId);ps.setString(2,name);ps.setString(3,flavour);ps.setDouble(4,price);ps.setInt(5,qty);ps.setString(6,desc);ps.setString(7,origin);ps.setString(8,raw);ps.setString(9,qty>0?"AVAILABLE":"OUT_OF_STOCK");ps.setInt(10,id);ps.executeUpdate();System.out.println("Menu item updated successfully.");}
        }catch(Exception e){System.out.println("Unable to update item: "+e.getMessage());}
    }
    private int readInt(Scanner sc,String p){while(true){System.out.print(p);try{return Integer.parseInt(sc.nextLine().trim());}catch(Exception e){System.out.println("Enter a valid integer.");}}}
}
