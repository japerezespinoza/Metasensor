/*
 * This class allows to specify the paths for the configuration and plugins
 * files, if you change the location of these components, it must be specified
 * in this class
 */
package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class only keeps the configuration directives
 * @author Jorge Antonio Pérez Espinoza
 */
public class Configuration {

    public static String PLUGINS_DIRECTORY = "Plugins/";
    public static String CONFIG_COMMANDS_FILE = "Configuration/commands.conf";
    public static String CONFIG_SUSCRIBERS_FILE = "Configuration/suscribers.conf";
    public static int MAX_INTENT_PUSH = 1;
    public static int PUSH_PORT = 6777;
    public static int PULL_PORT = 6777;
    public static int DISTRIBUTOR_BROADCAST_TIMEOUT = 5000;
    public static int DISTRIBUTOR_BROADCAST_PORT_SERVER = 6778;
    public static int DISTRIBUTOR_BROADCAST_PORT_CLIENT = 6779; 
    public static String DISTRIBUTOR_BROADCAST_STATIC_ADDRESS = "148.247.201.216";
    public static String DISTRIBUTOR_LOGFILENAME = "log.txt";    
        public static int TIMEOUT_IP_PUBLIC_DISCOVER = 3 * 1000;
    public static int WAIT_BROADCAST = 60*1000;

    
    public static void  loadConfiguration(){
    
        File f =new File ("Configuration/configuration.conf");
        if(!f.exists()){
        
            System.out.println("No config file, deafualt values loaded");
        }
        else{
        
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line;
                line = br.readLine();
                while(line != null){
                    String parameter;
                    String value;
                    String [] descom = line.split("=");                    
                    parameter = descom[0].trim();
                    value = descom[1].trim();
                    switch(parameter){
                    
                        case "DISTRIBUTOR_BROADCAST_STATIC_ADDRESS":
                            Configuration.DISTRIBUTOR_BROADCAST_STATIC_ADDRESS = value;
                            System.out.println("DISTRIBUTOR_BROADCAST_STATIC_ADDRESS " + DISTRIBUTOR_BROADCAST_STATIC_ADDRESS);
                            break;
                            
                        case "TIMEOUT_IP_PUBLIC_DISCOVER":
                            Configuration.TIMEOUT_IP_PUBLIC_DISCOVER = Integer.parseInt(value);
                            System.out.println("TIMEOUT_IP_PUBLIC_DISCOVER " + TIMEOUT_IP_PUBLIC_DISCOVER);
                            break;    
                        default:
                    }
                    
                    line = br.readLine();
                    
                }
                
                System.out.println("Values loaded from file ");   
            } catch (IOException ex) {
             System.out.println("Error in config file, default values loaded");
            }
        
        }
    
    }
}
