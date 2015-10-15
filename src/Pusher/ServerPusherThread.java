/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pusher;

import CommandPlugins.CommandManager;
import Main.Monitor;
import Utilities.Configuration;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  Jorge Antonio Pérez Espinoza
 */
public class ServerPusherThread extends Thread{
    
    
    private Suscriber suscriber;
    private int port;
    Monitor monitor;
    
    public ServerPusherThread(Suscriber suscriber, Monitor monitor){
        this.suscriber = suscriber;
        this.port = Configuration.PUSH_PORT;
        this.monitor = monitor;
    }


    
    public void run_old() {
        boolean attemps = true;
        while (attemps) {
            // if(suscribers.containsValue(suscriber)) {
            if(monitor.getSuscribers().containsValue(suscriber)) {
            try {
                     
                Socket socket = new Socket(suscriber.getIPAdress(), port);
               String response = this.monitor.getCommandManager().executeCommand(suscriber.getCommand());
                System.out.println(suscriber);
              // String response ="testing";
                System.out.println("Pull mode: " + " sending to" + suscriber.getIPAdress() + " " + suscriber.getCommand());               
                try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                    dos.writeUTF("push:"+response);
                    dos.close();
                    suscriber.refreshIntents();
                } catch (Exception ex) {
                }
            } catch (IOException ex) {

                suscriber.setMax_intent(suscriber.getMax_intent() - 1);
                if (suscriber.getMax_intent() <= 0) {
                    System.out.println("Suscriber " + suscriber.getIPAdress() + " is down");
                    attemps = false;

                } else {
                    this.monitor.getSuscribers().remove(suscriber.getIPAdress());
                    System.out.println("Error pushing to " + suscriber.getIPAdress() + ": " + suscriber.getMax_intent() + " attemps remaining");
                }
                   
        }
            try {
                Thread.sleep(suscriber.getInterval()*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerPusherThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        }
    }
    
   public void run() {
        boolean attemps = true;
        
        while (monitor.getSuscribers().containsValue(suscriber)) {

            System.out.println("PusherThread Suscribers size : " + this.monitor.getSuscribers().size());
            
            try {
                
                Socket socket = new Socket(suscriber.getIPAdress(), port);
                String response = this.monitor.getCommandManager().executeCommand(suscriber.getCommand());
                System.out.println(suscriber);
                // String response ="testing";
                System.out.println("Pull mode: " + " sending to" + suscriber.getIPAdress() + " " + suscriber.getCommand());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("push:" + response);
                dos.close();
                suscriber.setMax_intent(Configuration.MAX_INTENT_PUSH);
                
            } catch (IOException ex) {
                System.out.println("Pushing error to " + suscriber.getIPAdress());
                suscriber.setMax_intent(suscriber.getMax_intent()-1);
                if(monitor.getSuscribers().get(suscriber.getIPAdress()).getMax_intent()<1){
                
                    Suscriber s = monitor.getSuscribers().get(suscriber.getIPAdress());
                    monitor.getSuscribers().remove(s.getIPAdress());
                    System.out.println("Suscriber removed");

                }
            }

            try {
                System.out.println("Sleeping the interval: " + suscriber.getInterval());
                Thread.sleep(suscriber.getInterval() * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerPusherThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
