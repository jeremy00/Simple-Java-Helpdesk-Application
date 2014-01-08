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
/**
 *
 * @author mayj
 */
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
    
    /**
     *
     */
    public enum Status{
        /**
         *
         */
        NEW,
        /**
         *
         */
        IN_PROGRESS,
        /**
         *
         */
        ON_HOLD,
        /**
         *
         */
        COMPLETED }
    
   database(){  

         try {

             
 //Attempt connection to the database
             Class.forName("org.apache.derby.jdbc.ClientDriver");
             con = DriverManager.getConnection("jdbc:derby://localhost:1527/dial", "jeremy", "jeremy");
             stmt = con.createStatement();
            Class.forName("org.apache.derby.jdbc.ClientDriver");
           
            
//set put the view Result Set to be the first record in set   (next/prev/console?)       
            this.viewstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            this.viewrs= viewstmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET ORDER BY ID");  
           this.viewrs.beforeFirst();
           this.viewrs.next();
 
//Set up the row Result Set to be able to go to any row you want (Row function)
           this.rowstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            this.rowrs= rowstmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET ORDER BY ID");  
           this.rowrs.beforeFirst();
           this.rowrs.next();
           
//RS and stmt for the employee table.           
            this.empstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            this.emprs= empstmt.executeQuery             
         ("SELECT * FROM JEREMY.EMPLOYEE");  
           this.emprs.beforeFirst();
           this.emprs.next();
 
           
           
  //set up the first default ticket ticket for view Result Set         
       currentticket =makeTicket(viewrs.getInt("ID"), viewrs.getString("NAME"),
                                 viewrs.getString("PHONE"),viewrs.getString("TAG"), 
                                 viewrs.getString("DATE"), viewrs.getString("PROBLEM"),
                                 viewrs.getString("NOTES"), viewrs.getString("STATUS"));

        } catch (Exception e) {
            System.out.println("SQL constructor problem " + e);
        } 
        
  }// end of contructor
   
    /**
     * gives the current phonecallTicket object in the database
     * @return phonecallTicket 
     */
    public phonecallTicket getTicket(){
   return currentticket;
   }
 
    /**
     * Takes in a row, uses ROWRS to query which db item is in which row
     * and then it sets that item as the current displayed item.
     * @param row
     * @return phonecallTicket object
     */
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
        String status = rowrs.getString("STATUS");
       currentticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes, status);
       }//else
   } catch (Exception e){
       System.out.println("SQL problem at getRow()");}
   return currentticket;
   }
   
    /**
     * Returns total number of rows in table with STMT and RS
     * @return int rows
     */
    public int total(){
        int rows = 0;
       
        try {
            rs = stmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET");
          while (rs.next()) {rows++;}
         System.out.println("There are "+ rows + " record in the table"); 
        
        } catch (Exception e) {}
    
    return rows;
      }
    
    
    
    /**
     *Used by main to update view rs to show current selection after an adddition
     */
    public void updateViewRs(){
    try{
     this.viewrs= viewstmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET ORDER BY ID"); 
//      viewrs.beforeFirst();
//           viewrs.next();
    }catch (Exception e ){
        System.out.println("sql exception at updateViewRs" + e);
    }
    }
    
    
    /**
     * takes in a phonecallTicket obj, puts it in a sql statement
     * and then executes the sql statement to add a RECORD of TICKET in to TICKET DB
     * @param ticket
     */
    public void addTicket(phonecallTicket ticket){
        try {
            
            System.out.println("Number of tickets? " + (total()+1));
            String insert ="INSERT INTO JEREMY.TICKET "
                    + "(ID,NAME,PHONE,TAG,DATE,PROBLEM,NOTES,STATUS) "
                    + "VALUES "
                    
                + "(" + (total() +1) +",'"
                    +ticket.who+"','"
                    +ticket.phone+"','"
                    +ticket.tag+"',' "
                    +ticket.date+"',' "
                    +ticket.problem+"',' "
                    +ticket.notes+"','"
                    +"NEW"+"')";
           
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
    /**
     *Helper method to create a ticket, this .java uses it
     * @param id
     * @param who
     * @param phone
     * @param tag
     * @param date
     * @param problem
     * @param notes
     * @param status
     * @return
     */
    public phonecallTicket makeTicket(int id,  String who,  String phone,String tag, String date,String problem, String notes, String status){
        
        phonecallTicket ticket = new phonecallTicket(id,  who,  phone, tag, date, problem, notes, status);
    currentticket = ticket;
        return ticket;
    }
    
    
    
    
    /**
     * used in ViewTicket to do next function. Takes in/returns a ticket
     * so that if there is no next ticket, it will return same object
     * uses VIEWRS to query next record in DB
     * @param ticket
     * @return
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
        String status = viewrs.getString("STATUS");
        
       ticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes, status);
       }
        } catch (Exception e) {System.out.println("SQL nextTicket() problem " + e);}
    
        return ticket;
    
    }
    
     /**
     * used in ViewTicket to do previous function. Takes in/returns a ticket
     * so that if there is no previous ticket, it will return same object
     * uses VIEWRS to query next record in DB
     * @param ticket
     * @return
     */
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
        String status = viewrs.getString("STATUS");
        
       ticket = makeTicket(id_col, first_name, phone, tag, date, prob, notes, status);
            }
        } catch (Exception e) {System.out.println("SQL nextTicket() problem " + e);}
    
        return ticket;
    
    }

    /** 
     * Displays all tickets sorted by ID with a string. Uses LoopDBInfo to consolidate process
     * @return a string with all of the ticket records
     */
    public String displayAllTickets(){
  String p = " ";
   try {
      
            rs = stmt.executeQuery             
         ("SELECT * FROM JEREMY.TICKET ORDER BY ID");
          p = loopDBInfo(rs);
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        } 
  return p;
  
  }
    /**
     * Displays all tickets sorted by Name with a string. Uses LoopDBInfo to consolidate process
     * @return a string with all of the ticket records
     */
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
  
    /**
     * Takes in the standard rs and sifts through the set
     * To get all of the information and puts it in a string.
     * @param rs
     * @return a string with ID/NAME/PHONE/STATUS/PROBLEM info
     */
    public String loopDBInfo(ResultSet rs){
      String p = "";
      try{
      while (rs.next()) {
        int id_col = rs.getInt("ID");
        String first_name = rs.getString("NAME");
        String phone = rs.getString("PHONE");
        String status = rs.getString("STATUS");
        String prob = rs.getString("PROBLEM");
       
        
         p = p + (id_col + " " + first_name + " " + phone + " "+ status + " "+ prob + "\n");        
           // System.out.println(p);
        }
      } catch (Exception e) {
            System.out.println("SQL problem " + e);
 
  } return p;
  }//end loopDBInfo


    /**
     * Not really used right now.
     * @param num
     */
    public void removeTicket(int num){
    if (!tickets.isEmpty() && num < tickets.size() && num > 0){
       tickets.remove(num);
    }
    }//end removeTicket
    
    /**
     *Adds an employee with the use of employee object
     * Uses empStmt/emprs to insert and updates before it leaves
     * @param emp
     */
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
    
     /**
     * Takes a name string from user, goes into employee DB and deletes the person
     * @param person (User supplies this)
     */
    public void delEmployee(String person){
            System.out.println("Attempt to delete " + person);
        try {
          String delete ="DELETE FROM JEREMY.EMPLOYEE WHERE NAME='" + person+"'"; 
          stmt.executeUpdate(delete);
         
            
        } catch (Exception e) {
            System.out.println(person +" may not exist" + e);
        }
      
    }
    
      /**
     * Displays all employees, uses loopDBEMPinfo to sort info
     * @return a String with all the employees
     */
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
    
      /**
     *
     * @return
     */
    public String[] getArrayAllEmployees(){
        
      String[] employees = new String[7];
          try {
    
          emprs.beforeFirst();
          int i = 0;
   while (emprs.next()) {
 
          String name = emprs.getString("NAME");
          employees[i] = name;
          
                    i++;      }
   emprs.close();
        } catch (Exception e) {
            System.out.println("SQL problem " + e);
        }
   
  return employees;
      
      }
      
      
      
      
      /**
     *
     * @param rs
     * @return
     */
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
      
     /**
     *
     * @param ID
     * @param numstatus
     */
    public void updateEmployeeStatus(int ID, int numstatus){
  try{
       String status = "";
      
      
       
       switch(numstatus){
           case 0: status = Status.NEW.toString(); break;
           case 1: status = Status.IN_PROGRESS.toString(); break;
           case 2: status = Status.ON_HOLD.toString(); break;
           case 3: status = Status.COMPLETED.toString(); break;
           default: status = Status.NEW.toString();break;
       }
        String update = "UPDATE JEREMY.TICKET SET STATUS='" +  status + "' WHERE ID=" + ID;
      System.out.println(status);
      System.out.println(update); 
      currentticket.status=status;
       stmt.executeUpdate(update);
//    viewrs = viewstmt. ("UPDATE JEREMY.TICKET SET STATUS='" +  status + "' WHERE ID=" + ID);
  }catch(Exception e ){
      System.out.println("sql issue in updateEmployeeStatus " + e);}
        
     
     }
    }
