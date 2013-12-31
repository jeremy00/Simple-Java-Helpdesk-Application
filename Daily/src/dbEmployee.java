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
public class dbEmployee {
    
    Connection con;
    Statement stmt;
  ResultSet rs;
  

    
   dbEmployee(){  

         try {
            
  //Attempt connection to the database
             Class.forName("org.apache.derby.jdbc.ClientDriver");
             con = DriverManager.getConnection("jdbc:derby://localhost:1527/dial", "jeremy", "jeremy");
             stmt = con.createStatement();
            Class.forName("org.apache.derby.jdbc.ClientDriver");

  //set put the view Result Set to be the first record in set          
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs= stmt.executeQuery             
         ("SELECT * FROM JEREMY.EMPLOYEE");  
          rs.beforeFirst();
          rs.next();
 


        } catch (Exception e) {
            System.out.println("SQL constructor problem " + e);
        } 
        
  }// end of contructor
   
//   public phonecallTicket getTicket(){
//   return currentticket;
//   }
 
//   public phonecallTicket getRow(int row){
//       System.out.println("ROW IS!!! " + row );
//       try{
//       if(row > total() || row < 0)
//           System.out.println("invalid row");
//   else {rowrs.absolute(row);
//        int id_col = rowrs.getInt("ID");
//        String first_name = rowrs.getString("NAME");
//        String phone = rowrs.getString("PHONE");
//        String tag = rowrs.getString("TAG");
//        String date = rowrs.getString("DATE");
//        String prob = rowrs.getString("PROBLEM");
//        String notes = rowrs.getString("NOTES");
//              
//       currentticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes);
//       }//else
//   } catch (Exception e){
//       System.out.println("SQL problem at getRow()");}
//   return currentticket;
//   }
    /*
     Return number of total rows, PRINTS total rows also
     */
    public int total(){
        int rows = 0;
       
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery             
         ("SELECT * FROM JEREMY.EMPLOYEE");
          while (rs.next()) {rows++;}
         System.out.println("There are "+ rows + " record in the table"); 
        
        } catch (Exception e) {}
    
    return rows;
      }
    
    
    /*
     Add ticket to DB resem the ticket structure.
     * It will aso return the total size with total method
     */
    public void addEmployee(employee emp){
        try {
            
            
            String insert ="INSERT INTO JEREMY.EMPLOYEE VALUES "
                + "(" + (total() +1) +",'"
                    +emp.name+"','"
                    +emp.username+"','"
                    +emp.password+"')";
           
            System.out.println(insert);
            
          stmt.executeUpdate(insert);

           rs = stmt.executeQuery("SELECT * FROM JEREMY.EMPLOYEE");
            total();
        } catch (Exception e) {
            System.out.println("SQL problem dbEmployee Addmployee()" + e);
        }
      
    }
    public void delEmployee(String person){
//        try {
//            
//            
//            String insert ="INSERT INTO JEREMY.EMPLOYEE VALUES "
//                + "(" + (total() +1) +",'"
//                    +emp.name+"','"
//                    +emp.username+"','"
//                    +emp.password+"')";
//           
//            System.out.println(insert);
//            
//          stmt.executeUpdate(insert);
//
//           rs = stmt.executeQuery("SELECT * FROM JEREMY.EMPLOYEE");
//            total();
//        } catch (Exception e) {
//            System.out.println("SQL problem dbEmployee Addmployee()" + e);
//        }
//      
    }
///*
// 
// Makes a ticket with an id number and returns it. That's it.
// */
//    public phonecallTicket makeTicket(int id,  String who,  String phone,String tag, String date,String problem, String notes){
//        
//        phonecallTicket ticket = new phonecallTicket(id,  who,  phone, tag, date, problem, notes);
//    currentticket = ticket;
//        return ticket;
//    }
//    
//    public phonecallTicket getCurrentTicket(){
//    
//    
//    }
    
    
//    /*
//     moves to next record, puts new info in a new ticket obj.
//     */
//    public phonecallTicket nextTicket(phonecallTicket ticket){
//        
//        try {
//           
//            if(!viewrs.isLast()){
//        viewrs.next();
//        int id_col = viewrs.getInt("ID");
//        String first_name = viewrs.getString("NAME");
//        String phone = viewrs.getString("PHONE");
//        String tag = viewrs.getString("TAG");
//        String date = viewrs.getString("DATE");
//        String prob = viewrs.getString("PROBLEM");
//        String notes = viewrs.getString("NOTES");
//              
//       ticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes);
//       }
//        } catch (Exception e) {System.out.println("SQL nextTicket() problem " + e);}
//    
//        return ticket;
//    
//    }
    
//     public phonecallTicket previousTicket(phonecallTicket ticket){
//        
//        
//        try {
//            
//            if(!viewrs.isFirst()){
//        viewrs.previous();
//        int id_col = viewrs.getInt("ID");
//        String first_name = viewrs.getString("NAME");
//        String phone = viewrs.getString("PHONE");
//        String tag = viewrs.getString("TAG");
//         String date = viewrs.getString("DATE");
//        String prob = viewrs.getString("PROBLEM");
//        String notes = viewrs.getString("NOTES");
//        
//       ticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes);
//            }
//        } catch (Exception e) {System.out.println("SQL nextTicket() problem " + e);}
//    
//        return ticket;
//    
//    }
/*
 A memory intensive search of the SQL data returned with an arraylist of ticket
 * objects
 @return A arraylist prepared from the complete set of data
 */
   public String displayAllEmployees(){
  String p = " ";
   try {
      
            rs = stmt.executeQuery             
         ("SELECT * FROM JEREMY.EMPLOYEE");
          p = loopDBInfo(rs);
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        } 
  return p;
  
  }
//  public String displayNameTickets(){
//  String p = " ";
//   try {
//      
//            rs = stmt.executeQuery             
//         ("select * from JEREMY.TICKET ORDER BY NAME");
//      p = loopDBInfo(rs);
//        
//        } catch (Exception e) {
//            System.out.println("SQL problem " + e);
//        } 
//  return p;
//  
//  }//end displayNameTickets
  
  public String loopDBInfo(ResultSet rs){
      String p = "";
      try{
      while (rs.next()) {
        int id_col = rs.getInt("ID");
        String name = rs.getString("NAME");
        String un = rs.getString("USERNAME");
        String pw = rs.getString("PASSWORD");
        
        //gen password as asteriks 
        String newps = "";
        for (int i = 0; i < pw.length(); i++) {newps = newps + "*";}
        
        
         p = p + (id_col + " " + name + " " + un + " " + pw + "\n");        
           // System.out.println(p);
        }
      } catch (Exception e) {
            System.out.println("SQL problem " + e);
 
  } return p;
  }//end loopDBInfo


//    public void removeTicket(int num){
//    if (!tickets.isEmpty() && num < tickets.size() && num > 0){
//       tickets.remove(num);
//    }
//    }//end removeTicket
//    
    }
