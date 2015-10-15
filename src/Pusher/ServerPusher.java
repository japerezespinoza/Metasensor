/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pusher;

import CommandPlugins.CommandManager;
import Main.Monitor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Jorge Antonio Perez Espinoza
 */
public class ServerPusher  {
    

    Monitor monitor;
    int port;
    public ServerPusher(int port, Monitor monitor){
        this.port = port;
        this.monitor = monitor;
    }
    
    public void start() {
        Iterator it = monitor.getSuscribers().entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pairs = (Map.Entry) it.next();
            Suscriber s = (Suscriber) pairs.getValue();
            ServerPusherThread server = new ServerPusherThread(s,monitor);
            server.start();
            it.remove();
        }
    }


    public int getPort() {
        return port;
    }

}
