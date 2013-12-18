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
  ResultSet rs;
    ResultSet viewrs;
    Statement viewstmt;
    ArrayList tickets = new ArrayList();
    phonecallTicket currentticket;
   database(){  

//Load drives for database
//Attempt connetion to database 
        try {
             Class.forName("org.apache.derby.jdbc.ClientDriver");
             con = DriverManager.getConnection("jdbc:derby://localhost:1527/dial", "jeremy", "jeremy");
             stmt = con.createStatement();
            Class.forName("org.apache.derby.jdbc.ClientDriver");
              
            this.viewstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            this.viewrs= viewstmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET");
          //  viewrs.next();
           this.viewrs.beforeFirst();
            //  viewrs.next();
            System.out.println("first? " + this.viewrs.isFirst());
            System.out.println("last? " + this.viewrs.isLast());
            this.viewrs.next();
            
           
       currentticket =makeTicket(viewrs.getInt("ID"), viewrs.getString("NAME"),
                                 viewrs.getString("PHONE"),viewrs.getString("TAG"), 
                                 viewrs.getString("DATE"), viewrs.getString("PROBLEM"),
                                 viewrs.getString("NOTES"));
            System.out.println("CONSTRUCTUOR: " + viewrs.getInt("ID"));
        } catch (Exception e) {
            System.out.println("SQL constructor problem " + e);
        } 
        
  }// end of contructor
   
   public phonecallTicket getTicket(){
   return currentticket;
   }
 
    /*
     Return number of total rows, PRINTS total rows also
     */
    public int total(){
        int rows = 0;
       
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET");
          while (rs.next()) {rows++;}
         System.out.println("There are "+ rows + " record in the table"); 
        
        } catch (Exception e) {}
    
    return rows;
      }
    
    
    /*
     Add ticket to DB resem the ticket structure.
     * It will aso return the total size with total method
     */
    public void addTicket(phonecallTicket ticket){
        try {
            
            
            String insert ="INSERT INTO JEREMY.TICKET VALUES "
                + "(" + (total() +1) +",'"
                    +ticket.who+"','"
                    +ticket.phone+"','"
                    +ticket.tag+"',' "
                    +ticket.date+"',' "
                    +ticket.problem+"',' "
                    +ticket.notes+"')";
           
            System.out.println(insert);
            
          stmt.executeUpdate(insert);

           rs = stmt.executeQuery("SELECT * FROM JEREMY.TICKET");
            total();
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        }
       tickets.add(ticket);
    }
/*
 
 Makes a ticket with an id number and returns it. That's it.
 */
    public phonecallTicket makeTicket(int id,  String who,  String phone,String tag, String date,String problem, String notes){
        
        phonecallTicket ticket = new phonecallTicket(id,  who,  phone, tag, date, problem, notes);
    currentticket = ticket;
        return ticket;
    }
    
//    public phonecallTicket getCurrentTicket(){
//    
//    
//    }
    
    
    /*
     moves to next record, puts new info in a new ticket obj.
     */
    public phonecallTicket nextTicket(phonecallTicket ticket){
        
        try {
           
            if(!viewrs.isLast()){
        viewrs.next();
        int id_col = viewrs.getInt("ID");
        String first_name = viewrs.getString("NAME");
        String phone = viewrs.getString("PHONE");
        String tag = viewrs.getString("TAG");
        String date = viewrs.getString("DATE");
        String prob = viewrs.getString("PROBLEM");
        String notes = viewrs.getString("NOTES");
              
       ticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes);
       }
        } catch (Exception e) {System.out.println("SQL nextTicket() problem " + e);}
    
        return ticket;
    
    }
    
     public phonecallTicket previousTicket(phonecallTicket ticket){
        
        
        try {
            
            if(!viewrs.isFirst()){
        viewrs.previous();
        int id_col = viewrs.getInt("ID");
        String first_name = viewrs.getString("NAME");
        String phone = viewrs.getString("PHONE");
        String tag = viewrs.getString("TAG");
         String date = viewrs.getString("DATE");
        String prob = viewrs.getString("PROBLEM");
        String notes = viewrs.getString("NOTES");
        
       ticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes);
            }
        } catch (Exception e) {System.out.println("SQL nextTicket() problem " + e);}
    
        return ticket;
    
    }
/*
 A memory intensive search of the SQL data returned with an arraylist of ticket
 * objects
 @return A arraylist prepared from the complete set of data
 */
  public String displayAllTickets(){
  String p = " ";
   try {
      
            rs = stmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET");
         while (rs.next()) {
        int id_col = rs.getInt("ID");
        String first_name = rs.getString("NAME");
        String phone = rs.getString("PHONE");
        String tag = rs.getString("TAG");
        String prob = rs.getString("PROBLEM");
        String notes = rs.getString("NOTES");
        
         p = p + (id_col + " " + first_name + " " + phone + " " + prob + "\n");        
           // System.out.println(p);
        }
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        } 
  return p;
  
  }



    public void removeTicket(int num){
    if (!tickets.isEmpty() && num < tickets.size() && num > 0){
       tickets.remove(num);
    }
    }//end removeTicket
    
    }
