package Server;

import CommandPlugins.Command;
import CommandPlugins.CommandExecutor;
import CommandPlugins.CommandManager;
import Discovery.BroadcastClient;
import Discovery.Discovery;
import Main.Monitor;
import Pusher.ServerPusherThread;
import Pusher.Suscriber;
import Utilities.Configuration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the protocol communications of monitoring system
 *
 * @author Jorge Antonio Pérez Espinoza
 */
public class Protocol {

    private final Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Monitor monitor;

    public Protocol(Socket socket, Monitor monitor) {

        this.socket = socket;
        this.monitor = monitor;
    }

    /**
     * This method attends the incoming clients
     *
     * @return
     */
    public String initProcol() {
        try {

            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            String request = dataInputStream.readUTF();

            String[] type = request.split(":");
            System.out.println("Request from " + socket.getInetAddress() + ":" + request);

            //Para la ejecución de comandos
            switch (type[0]) {
                case "command": {
                    String response;
                    //Test for find more commands
                    if (type[1].contains(",")) {
                        String[] commands = type[1].split(",");
                       // response = commandManager.executeCommand(commands);
                        response = monitor.getCommandManager().executeCommand(commands);
                    } else {
                        //Test for one command
                       // response = commandManager.executeCommand(type[1]);
                        response = monitor.getCommandManager().executeCommand(type[1]);
                    }
                    System.out.println("Response to " + socket.getInetAddress() + " : " + response);
                    dataOutputStream.writeUTF(response);
                    dataOutputStream.close();
                    dataInputStream.close();
                    break;
                }

                //Para la suscripcion
                case "control": {
                    String[] list = type[1].split("%");
                    String message;

                    //Mensaje de suscripcion
                    if (list[0].equals("suscribe")) { //for suscribting
                        //suscribe_commands_
                                        //check_cpu_ls,check_mem,check_sto,check_dio,check_net,check_procs,check_load
                        String ip = this.socket.getInetAddress().toString().substring(1);
                        String command = list[1].replaceAll(";", ",");
                        String [] commands = command.split(",");
                        
                        int interval = Integer.parseInt(list[2]);
                        System.out.println("interval: " + interval);
                        Suscriber ns = new Suscriber(ip,command,interval);
                        //String IPAdress, String command,int interval
                       // if(!this.suscribers.containsKey(ip)){
                        if(!this.monitor.getSuscribers().containsKey(ip)){
                        this.monitor.getSuscribers().put(ip, ns);
                        dataOutputStream.writeUTF("suscription OK");
                        System.out.println("New suscripter : " + this.socket.getInetAddress().toString());
                        ServerPusherThread spt = new ServerPusherThread(ns,monitor);
                        spt.start();
                        }
                        else{
                        
                            System.out.println("This machine is monitored already");
                        }

                    } //Mensaje de desuscripción
                    else if (list[0].equals("unsuscribe")) { // for suscriber
                        System.out.println("Tamaño antes de unsuscribe: " + this.monitor.getSuscribers().size());
                        if (this.monitor.getSuscribers().containsKey(this.socket.getInetAddress().toString().substring(1))) {                                                       
                            this.monitor.getSuscribers().remove(this.socket.getInetAddress().toString().substring(1));                          
                            System.out.println("Unsuscription: " + this.socket.getInetAddress().toString().substring(1));
                            System.out.println("Tamaño despues de unsuscribe: " + this.monitor.getSuscribers().size());
                            dataOutputStream.writeUTF("unsuscription OK");
                        }
                    } //Para modificar el intervalo de tiempo
                    else if (list[0].equals("interval")) {
                        System.out.println("Interval time modification ");
                        String ip = this.socket.getInetAddress().toString().substring(1);

                        if (this.monitor.getSuscribers().containsKey(ip)) {
                            this.monitor.getSuscribers().get(ip).setInterval(Integer.parseInt(list[1]));
                            dataOutputStream.writeUTF("interval OK");
                        }

                    } else if (list[0].equals("benchmark")) {
                        String benchCommand = list[1];
                        
                        CommandExecutor ce = new CommandExecutor();
                        String r = ce.executeCommand(new Command("benchmark",list[1],"executes bench"));
                        // r returns the output
                      
                            dataOutputStream.writeUTF("OK");
                        
                        //Here we go with thge benchamarks
                    }
                    else if (list[0].equals("restartSystem")) {
                        this.monitor.getSuscribers().clear();
                        this.monitor = new Monitor();
                        System.out.println("Restarting system");
                        dataOutputStream.writeUTF("Restarting OK");
                        Thread.sleep(Configuration.WAIT_BROADCAST);
                        Discovery discovery = new Discovery();
                        discovery.discoveryDistributor();                        
                    }
                    
                    else { // Unreconized command

                        dataOutputStream.writeUTF("Control message arent implemented yet");
                    }

                    dataOutputStream.close();
                    dataInputStream.close();
                    break;

                }
 
                default:

                    System.out.println("Bad request ");
                    dataOutputStream.writeUTF("Bad request");
                    dataOutputStream.close();
                    dataInputStream.close();
            }

        } catch (IOException ex) {
            try {
                dataOutputStream.close();
                dataInputStream.close();
                return "Error 24";
            } catch (IOException ex1) {
                Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Init protocol sucessfull";
    }

}
