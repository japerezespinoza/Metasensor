package Main;

import Discovery.BroadcastClient;
import Discovery.Discovery;
import Server.Server;
import Pusher.ServerPusher;
import Utilities.Configuration;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge Antonio Pérez Espinoza
 */
public class MonitorStarter {

    public static void main(String args[]) {
    
        Monitor monitor = new Monitor();

        try {
            Server server = new Server(monitor);
            server.start();
            
        } catch (Exception ex) {
            System.out.println("Error starting server " + ex.getMessage());
        }

                Configuration.loadConfiguration();
        
        //ystem.out.println(MonitorStarter.funcion());
        Discovery discovery = new Discovery();
       discovery.discoveryDistributor();
    }
    
    

}
