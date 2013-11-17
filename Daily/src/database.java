/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author w032jwm
 */

import java.util.ArrayList;
public class database {
    ArrayList tickets = new ArrayList();
    
    public void addTicket(phonecallTicket ticket){
    tickets.add(ticket);
    }
    
    public void removeTicket(int num){
    if (!tickets.isEmpty() && num < tickets.size() && num > 0){
       tickets.remove(num);
    }
    }//end removeTicket
    
}
