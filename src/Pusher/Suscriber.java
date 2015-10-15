/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pusher;

import java.util.ArrayList;
import Utilities.Configuration;
import java.util.Objects;

/** 
 *
 * @author Jorge Antonio Pérez Espinoza
 */
public class Suscriber {
    
    private String IPAdress;
    private int    interval;
    private String command;
    private int max_intent;

    /**
     * 
     * @param IPAdress Suscriber IP address
     * @param interval Time interval
     * @param command  Command
     */
    public Suscriber(String IPAdress, String command,int interval) {
        this.IPAdress = IPAdress;
        this.interval = interval;
        this.command = command;
        this.max_intent = Configuration.MAX_INTENT_PUSH;
    }

   
    public synchronized String getIPAdress() {
        return IPAdress;
    }

    public synchronized void setIPAdress(String IPAdress) {
        this.IPAdress = IPAdress;
    }

    public synchronized int getInterval() {
        return interval;
    }

    public synchronized void setInterval(int interval) {
        this.interval = interval;
    }

    public  synchronized String getCommand() {
        return command;
    }

    public synchronized void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Suscriber{" + "IPAdress=" + IPAdress + ", interval=" + interval + ", command=" + command + '}';
    }

    public synchronized int getMax_intent() {
        return max_intent;
    }

    public synchronized void setMax_intent(int max_intent) {
        this.max_intent = max_intent;
    }
    
    
public synchronized void refreshIntents(){
        this.max_intent = Configuration.MAX_INTENT_PUSH;

}

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Suscriber other = (Suscriber) obj;
        if (!Objects.equals(this.IPAdress, other.IPAdress)) {
            return false;
        }
        return true;
    }

    
    
    
}
