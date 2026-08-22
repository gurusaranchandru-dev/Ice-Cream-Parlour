package IceCream;

import java.util.Scanner;

public class AdminDashboard {
    public void showDashboard(Scanner sc){
        AddIceCream add=new AddIceCream(); ViewIceCream view=new ViewIceCream(); UpdateMenuItem update=new UpdateMenuItem(); DeleteMenuItem delete=new DeleteMenuItem(); SearchMenuItem search=new SearchMenuItem();
        while(true){
            System.out.println("\n================ ADMIN DASHBOARD ================");
            System.out.println("1. View Menu");
            System.out.println("2. Add Category");
            System.out.println("3. Add Menu Item");
            System.out.println("4. Update Menu Item");
            System.out.println("5. Delete Menu Item");
            System.out.println("6. Search Menu Item");
            System.out.println("7. View Orders");
            System.out.println("8. View Bills");
            System.out.println("0. Logout");
            System.out.print("Select option: ");
            String choice=sc.nextLine().trim();
            try{
                switch(choice){
                    case "1" -> view.viewIceCream();
                    case "2" -> add.addCategory(sc);
                    case "3" -> add.addIceCream(sc);
                    case "4" -> update.updateMenuItem(sc);
                    case "5" -> delete.deleteMenuItem(sc);
                    case "6" -> search.searchMenuItem(sc);
                    case "7" -> viewOrders();
                    case "8" -> viewBills();
                    case "0" -> {System.out.println("Logging out...");return;}
                    default -> System.out.println("Invalid option.");
                }
            }catch(Exception e){System.out.println("Operation failed: "+e.getMessage());}
        }
    }
    private void viewOrders(){
        String sql="SELECT order_id,order_date,total_amount,order_status FROM orders ORDER BY order_id DESC";
        try(var con=DBConnecton.getConnection();var ps=con.prepareStatement(sql);var rs=ps.executeQuery()){System.out.println("\n================ ORDERS ================");System.out.printf("%-8s %-22s %-12s %-12s%n","ID","DATE","TOTAL","STATUS");while(rs.next())System.out.printf("%-8d %-22s ₹%-11.2f %-12s%n",rs.getInt(1),rs.getTimestamp(2),rs.getDouble(3),rs.getString(4));}catch(Exception e){System.out.println("Unable to view orders: "+e.getMessage());}
    }
    private void viewBills(){
        String sql="SELECT bill_id,order_id,bill_date,grand_total FROM bills ORDER BY bill_id DESC";
        try(var con=DBConnecton.getConnection();var ps=con.prepareStatement(sql);var rs=ps.executeQuery()){System.out.println("\n================ BILLS ================");while(rs.next())System.out.printf("Bill %d | Order %d | %s | ₹%.2f%n",rs.getInt(1),rs.getInt(2),rs.getTimestamp(3),rs.getDouble(4));}catch(Exception e){System.out.println("Unable to view bills: "+e.getMessage());}
    }
}
