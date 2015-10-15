/*
 * Clase que modela las propiedades de los comandos que se ejecutaran 
 * en la consola para obtener la salida, usando plugins de nagios
 */
package CommandPlugins;

/**
 *
 * @author Jorge Antonio Pérez Espinoza
 */
public class Command {

    private String name;
    private String command;
    private String help;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public Command(String name, String command, String help) {
        this.name = name;
        this.command = command;
        this.help = help;
    }


      public String toStringAdvanced() {
        return "name: " + name + "\ncommand: " + command + "\nhelp: " + help+"\n";
    }
    
    @Override
    public String toString() {
        return "name: " + name  + "\nhelp: " + help+"\n";
    }
    
    
}
