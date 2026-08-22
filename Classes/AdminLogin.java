package IceCream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AdminLogin {
    public boolean login(Scanner sc){
        System.out.println("\n================ ADMIN LOGIN ================");
        System.out.print("Username: "); String user=sc.nextLine().trim();
        System.out.print("Password: "); String pass=sc.nextLine();
        String sql="SELECT admin_id FROM admin WHERE username=? AND password=? AND status='ACTIVE'";
        try(Connection con=DBConnecton.getConnection();PreparedStatement ps=con.prepareStatement(sql)){ps.setString(1,user);ps.setString(2,pass);try(ResultSet rs=ps.executeQuery()){if(rs.next()){System.out.println("Login successful. Welcome, "+user+".");return true;}}System.out.println("Invalid username or password.");}
        catch(Exception e){System.out.println("Login failed: "+e.getMessage());} return false;
    }
}
