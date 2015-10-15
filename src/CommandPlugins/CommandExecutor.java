/*
 * Clase encargada de ejecutar los objetos de tipo Command
 */
package CommandPlugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import Utilities.Configuration;
/**
 *
 * @author Jorge Antonio Perez Espinoza
 */
public class CommandExecutor {
    
    private Calendar calendar;
    public CommandExecutor() {

    }        
    public String executeCommand(Command command) {

        StringBuilder output = new StringBuilder();

        Process p;

        try {
            System.out.println("test"+command.getCommand());
            if(command.getCommand().contains("./")){
            p = Runtime.getRuntime().exec(Configuration.PLUGINS_DIRECTORY+command.getCommand());
            }
            else{
                p = Runtime.getRuntime().exec(command.getCommand());
            }
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            } 
            
            if(command.getName().equals("check_cpu")){
                System.out.println("mpstat requested");
                String data = output.toString().substring(output.indexOf("%usr"));              
                    calendar = Calendar.getInstance();
                    

            java.util.Date now = calendar.getTime();
            
             return command.getName()+"->"+new java.sql.Timestamp(now.getTime())+"->"+data;
            
            }
            
            else{
                                calendar = Calendar.getInstance();
                    

            java.util.Date now = calendar.getTime();
            
             return command.getName()+"->"+new java.sql.Timestamp(now.getTime())+"->"+output.toString();
            
            }            
            
        } catch (InterruptedException | IOException e) {
            System.out.println("Error : " + e.getMessage());
            return command.getName() + " -> not found" + e.getMessage();
        }
           }   
}
