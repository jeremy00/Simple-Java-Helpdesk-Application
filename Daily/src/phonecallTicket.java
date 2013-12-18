/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mayj
 */
public class phonecallTicket {
int id = 33;
String who;
String phone;
String tag;
String date;
String problem;
String notes;

    phonecallTicket(String who, String phone, String tag, String date, String problem, String notes){
    this.who = who;
    this.phone = phone;
    this.tag = tag;
    this.date = date;
    this.problem = problem;
    this.notes = notes;
    } 
    
    phonecallTicket(){}
    
    
 phonecallTicket(int id, String who, String phone, String tag,  String date, String problem, String notes){
    this.id = id;
    this.who = who;
    this.phone = phone;
    this.tag = tag;
    this.date = date;
    this.problem = problem;
    this.notes = notes;
    }  


}
