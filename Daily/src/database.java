/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author w032jwm
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
public class database {
    
    Connection con;
    Statement stmt;
    
   database(){  
       
       try{
           Class.forName("org.apache.derby.jdbc.ClientDriver");
        }
        catch (ClassNotFoundException e){
            System.out.println("Class not found " + e);
        }
       
  
        
        try {
             con = DriverManager.getConnection("jdbc:derby://localhost:1527/dial", "jeremy", "jeremy");
             stmt = con.createStatement();
        
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            
          //ResultSet rs = stmt.executeQuery
        // ("SELECT * FROM employee");
      
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        }
   }// end of contructor
   
   
    ArrayList tickets = new ArrayList();
    public int total(){
        int rows = 0;
        try {
            Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery
         ("SELECT * FROM JEREMY.TICKET");
         while (rs.next()) {
          rows++;
         }
         System.out.println("There are "+ rows 
         + " record in the table"); 
        } catch (Exception e) {
        }
    return rows;
      }
    
    public void addTicket(phonecallTicket ticket){
        try {
            
            
            String insert ="INSERT INTO JEREMY.TICKET VALUES "
                + "(" + (total() +1) +",'"+ticket.who+"','"+ticket.phone+"','"+ticket.tag+"','"+ticket.problem+"','"+ticket.notes+"')";
           
            System.out.println(insert);
            
          stmt.executeUpdate(insert);
            ResultSet rs = stmt.executeQuery("SELECT * FROM JEREMY.TICKET");
            total();
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        }
        
   // INSERT INTO JEREMY.TICKET VALUES (2,'jeer','9372255112','w056511','It s broke','Fix it please')
    tickets.add(ticket);
    }
    
    public void removeTicket(int num){
    if (!tickets.isEmpty() && num < tickets.size() && num > 0){
       tickets.remove(num);
    }
    }//end removeTicket
    
}
