package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AddIceCream {
    public void addIceCream(Scanner sc) {
        try(Connection con=DBConnecton.getConnection()){
            System.out.println("\n================ ADD MENU ITEM ================");
            int categoryId=selectCategory(con,sc,true);
            if(categoryId==0) return;
            String name=readRequired(sc,"Item name: ");
            String flavour=readRequired(sc,"Flavour: ");
            double price=readDouble(sc,"Price: ");
            int quantity=readNonNegativeInt(sc,"Initial quantity: ");
            String description=readRequired(sc,"Description: ");
            String origin=readRequired(sc,"Origin: ");
            String raw=readRequired(sc,"Raw materials: ");
            String status=quantity>0?"AVAILABLE":"OUT_OF_STOCK";
            String sql="INSERT INTO menu_items(category_id,item_name,flavour,price,quantity,description,origin,raw_materials,status) VALUES(?,?,?,?,?,?,?,?,?)";
            try(PreparedStatement ps=con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,categoryId); ps.setString(2,name); ps.setString(3,flavour); ps.setDouble(4,price); ps.setInt(5,quantity);
                ps.setString(6,description); ps.setString(7,origin); ps.setString(8,raw); ps.setString(9,status); ps.executeUpdate();
                try(ResultSet keys=ps.getGeneratedKeys()){ if(keys.next()) System.out.println("Menu item added with ID: "+keys.getInt(1)); }
            }
        }catch(Exception e){System.out.println("Unable to add item: "+e.getMessage());}
    }

    public void addCategory(Scanner sc){
        String name=readRequired(sc,"Category name: "); String desc=readRequired(sc,"Description: ");
        String sql="INSERT INTO menu_categories(category_name,description,status) VALUES(?,?,'ACTIVE')";
        try(Connection con=DBConnecton.getConnection();PreparedStatement ps=con.prepareStatement(sql)){ps.setString(1,name);ps.setString(2,desc);ps.executeUpdate();System.out.println("Category added successfully.");}
        catch(Exception e){System.out.println("Unable to add category: "+e.getMessage());}
    }

    static int selectCategory(Connection con,Scanner sc,boolean allowAdd){
        try(PreparedStatement ps=con.prepareStatement("SELECT category_id,category_name FROM menu_categories WHERE status='ACTIVE' ORDER BY category_name");ResultSet rs=ps.executeQuery()){
            System.out.println("\nAvailable categories:"); while(rs.next()) System.out.printf("%d. %s%n",rs.getInt(1),rs.getString(2));
            System.out.println("0. Cancel"); System.out.print("Category ID: "); return sc.nextInt();
        }catch(Exception e){System.out.println("Unable to load categories: "+e.getMessage());return 0;}
    }
    static String readRequired(Scanner sc,String prompt){while(true){System.out.print(prompt);String s=sc.nextLine().trim();if(!s.isEmpty())return s;System.out.println("Value cannot be empty.");}}
    static double readDouble(Scanner sc,String prompt){while(true){System.out.print(prompt);try{double v=Double.parseDouble(sc.nextLine().trim());if(v>=0)return v;}catch(Exception ignored){}System.out.println("Enter a valid non-negative number.");}}
    static int readNonNegativeInt(Scanner sc,String prompt){while(true){System.out.print(prompt);try{int v=Integer.parseInt(sc.nextLine().trim());if(v>=0)return v;}catch(Exception ignored){}System.out.println("Enter a valid non-negative integer.");}}
}
