/*
 * Esta clase administrar los commandos disponibles, los inicializa y los carga
 * de los archivos de configuración.
 */
package CommandPlugins;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Jorge Antonio Pérez Espinoza
 */
public class CommandManager {

    private HashMap<String, Command> commands;
    private final CommandLoader commandLoader;
    private final CommandExecutor commandExecutor ;
    
    
    public CommandManager() {
       commandLoader = new CommandLoader(); 
       commands = commandLoader.getCommands();
        commandExecutor = new CommandExecutor();

    }

    public String loadCommands() {

        return null;
    }

    public HashMap<String, Command> getAvailableCommands() {
        return commands;
    }

    public void setCommands(HashMap<String, Command> commands) {
        this.commands = commands;
    }

    public String executeCommand(String command){
        if(command.contains(",")){
        
            String comm [] = command.split(",");
            return executeCommand(comm);
        }else{
        if(!commands.containsKey(command) && !command.equals("check_test"))
            return "Command not found";
        else{
            if(command.equals("check_test"))
                return "Test successfull";
            else if (command.equals("check_all"))
                return executeAllCommands();
            else{
            return commandExecutor.executeCommand(commands.get(command));}
            
        }}}
    
    
    public String executeCommand(String [] commands){
        String output="";
        for(String command:commands){
            if(this.commands.containsKey(command));
                output += commandExecutor.executeCommand(this.commands.get(command));
        }
        return output;
    }
    
    public String executeAllCommands(){
    
        String output = "";
        Iterator it = commands.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
             if(!pairs.getKey().equals("check_test")
                    && !pairs.getKey().equals("check_all")){
             output += commandExecutor.executeCommand((Command) pairs.getValue());
             }
             
        }

        return output;
    }
    
}
