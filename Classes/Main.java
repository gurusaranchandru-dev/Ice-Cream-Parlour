package IceCream;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        ViewIceCream view=new ViewIceCream(); OrderIceCream order=new OrderIceCream(); AdminLogin login=new AdminLogin(); AdminDashboard dashboard=new AdminDashboard(); SearchMenuItem search=new SearchMenuItem();
        if(!testDatabase())return;
        while(true){
            System.out.println("\n============================================");
            System.out.println("             ICE CREAM PARLOUR");
            System.out.println("============================================");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. System Information");
            System.out.println("0. Exit");
            System.out.print("Select option: ");
            String choice=sc.nextLine().trim();
            try{
                switch(choice){
                    case "1" -> customerMenu(sc,view,order,search);
                    case "2" -> {if(login.login(sc))dashboard.showDashboard(sc);}
                    case "3" -> systemInformation();
                    case "0" -> {System.out.println("Thank you for visiting Ice Cream Parlour!");return;}
                    default -> System.out.println("Invalid option.");
                }
            }catch(Exception e){System.out.println("Operation failed: "+e.getMessage());}
        }
    }
    private static void customerMenu(Scanner sc,ViewIceCream view,OrderIceCream order,SearchMenuItem search){
        while(true){
            System.out.println("\n================ CUSTOMER ================");
            System.out.println("1. View Menu");
            System.out.println("2. Start Ordering");
            System.out.println("3. Search Menu");
            System.out.println("0. Back");
            System.out.print("Select option: ");String c=sc.nextLine().trim();
            switch(c){case "1"->view.viewIceCream();case "2"->order.orderIceCream(sc);case "3"->search.searchMenuItem(sc);case "0"-> {return;}default->System.out.println("Invalid option.");}
        }
    }
    private static boolean testDatabase(){try(var con=DBConnecton.getConnection()){System.out.println("Database connection successful.");return true;}catch(Exception e){System.out.println("Database connection failed: "+e.getMessage());return false;}}
    private static void systemInformation(){System.out.println("\n================ SYSTEM INFORMATION ================");System.out.println("Application : Ice Cream Parlour Management System");System.out.println("Language    : Java");System.out.println("Database    : MySQL");System.out.println("Connectivity: JDBC");System.out.println("Data model  : Categories, Menu Items, Orders, Order Items, Bills");}
}
