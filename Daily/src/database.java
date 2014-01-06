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
    ResultSet rowrs;
    Statement rowstmt;
    ResultSet emprs;
    Statement empstmt;
    ArrayList tickets = new ArrayList();
    phonecallTicket currentticket;
   database(){  

         try {

             
  //Attempt connection to the database
             Class.forName("org.apache.derby.jdbc.ClientDriver");
             con = DriverManager.getConnection("jdbc:derby://localhost:1527/dial", "jeremy", "jeremy");
             stmt = con.createStatement();
            Class.forName("org.apache.derby.jdbc.ClientDriver");
           
            
  //set put the view Result Set to be the first record in set          
            this.viewstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            this.viewrs= viewstmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET");  
           this.viewrs.beforeFirst();
           this.viewrs.next();
 
//Set up the row Result Set to be able to go to any row you want
           this.rowstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            this.rowrs= rowstmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET");  
           this.rowrs.beforeFirst();
           this.rowrs.next();
           
            this.empstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            this.emprs= empstmt.executeQuery             
         ("SELECT * FROM JEREMY.EMPLOYEE");  
           this.emprs.beforeFirst();
           this.emprs.next();
 
           
           
  //set up the first default ticket ticket for view Result Set         
       currentticket =makeTicket(viewrs.getInt("ID"), viewrs.getString("NAME"),
                                 viewrs.getString("PHONE"),viewrs.getString("TAG"), 
                                 viewrs.getString("DATE"), viewrs.getString("PROBLEM"),
                                 viewrs.getString("NOTES"));

        } catch (Exception e) {
            System.out.println("SQL constructor problem " + e);
        } 
        
  }// end of contructor
   
   public phonecallTicket getTicket(){
   return currentticket;
   }
 
   public phonecallTicket getRow(int row){
       System.out.println("ROW IS!!! " + row );
       try{
       if(row > total() || row < 0)
           System.out.println("invalid row");
   else {rowrs.absolute(row);
        int id_col = rowrs.getInt("ID");
        String first_name = rowrs.getString("NAME");
        String phone = rowrs.getString("PHONE");
        String tag = rowrs.getString("TAG");
        String date = rowrs.getString("DATE");
        String prob = rowrs.getString("PROBLEM");
        String notes = rowrs.getString("NOTES");
              
       currentticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes);
       }//else
   } catch (Exception e){
       System.out.println("SQL problem at getRow()");}
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
          p = loopDBInfo(rs);
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        } 
  return p;
  
  }
  public String displayNameTickets(){
  String p = " ";
   try {
      
            rs = stmt.executeQuery             
         ("select * from JEREMY.TICKET ORDER BY NAME");
      p = loopDBInfo(rs);
        
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        } 
  return p;
  
  }//end displayNameTickets
  
  public String loopDBInfo(ResultSet rs){
      String p = "";
      try{
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
 
  } return p;
  }//end loopDBInfo


    public void removeTicket(int num){
    if (!tickets.isEmpty() && num < tickets.size() && num > 0){
       tickets.remove(num);
    }
    }//end removeTicket
    
    public void addEmployee(employee emp){
     try {
            
            
            String insert ="INSERT INTO JEREMY.EMPLOYEE (NAME, USERNAME, PASSWORD) VALUES "
                + "('"+emp.name+"','"
                    +emp.username+"','"
                    +emp.password+"')";
           
            System.out.println(insert);
            
          empstmt.executeUpdate(insert);

           emprs = stmt.executeQuery("SELECT * FROM JEREMY.EMPLOYEE");
            total();
        } catch (Exception e) {
            System.out.println("SQL problem dbEmployee Addmployee()" + e);
        }
    
    }
    
     public void delEmployee(String person){
            System.out.println("Attempt to delete " + person);
        try {
          String delete ="DELETE FROM JEREMY.EMPLOYEE WHERE NAME='" + person+"'"; 
          stmt.executeUpdate(delete);
         
            
        } catch (Exception e) {
            System.out.println(person +" may not exist" + e);
        }
      
    }
    
      public String displayAllEmployees(){
          String p = " ";
      
   try {
      
            emprs = stmt.executeQuery             
         ("SELECT * FROM JEREMY.EMPLOYEE");
          p = loopDBEMPInfo(emprs);
           emprs.close();
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        }
  
  return p;
}//end displayallemployees
    
      public String[] getArrayAllEmployees(){
        
      String[] employees = new String[7];
          try {
    
          emprs.beforeFirst();
          int i = 0;
   while (emprs.next()) {
 
          String name = emprs.getString("NAME");
          employees[i] = name;
          System.out.println("testing" + name);
          System.out.println("array " + employees[i]);
                    i++;      }
   emprs.close();
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        }
   
  return employees;
      
      }
      
      
      
      
      public String loopDBEMPInfo(ResultSet rs){
      String p = "     USERNAME    |    NAME     \n";
          
      
      try{
      while (rs.next()) {
        
        String username = rs.getString("USERNAME");
        String name = rs.getString("NAME");
         String space = "                           ";   
        
          System.out.println(space.substring(0, 15));
         p = p + ("        "+username + space.substring(0,space.length()-(username.length()*2)) + name + "\n");        
          
        }
      } catch (Exception e) {
            System.out.println("SQL problem " + e);
 
  } return p;
  }//end loopDBInfo
      
      
    }
