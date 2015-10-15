/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import CommandPlugins.CommandManager;
import Main.Monitor;
import Pusher.Suscriber;
import Utilities.Configuration;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dishelt
 */
public class Server extends Thread {

    private Socket socket;                  //Socket para conexiones.
    private ServerSocket serverSocket;      //Socket del servidor.
    private int puertoServer;               //Puerto del servidor.
    Monitor monitor;

    
    /**
     * Constructor de la clase Server para incializar las variables;
     *
     * @param puerto Port for server listening
     * @param fileConfig File with the configuration files
     * @throws java.lang.Exception
     */
    public Server(Monitor monitor) throws Exception {
        this.puertoServer = Configuration.PULL_PORT;                //Inicializo variable.
        this.serverSocket = new ServerSocket(puertoServer);    //Escucho en servidor en puerto indicado.
        this.monitor = monitor;
    }
      
    /**
     * Metodo para iniciar el servidor, cuando encuentra una conexion entrante,
     * crea un nuevo hilo para manejarla.
     *
     * @throws Exception
     */
    public void runServer() throws IOException  {

        socket = serverSocket.accept();         //Escucho a traves del puerto especificado
        //Creo un nuevo hilo para manejar la conexion y seguir escuchando.
        ServerThread serverThread = new ServerThread(socket,monitor);
        serverThread.start();                     //Incio el hilo.
        
        
    }

    
    @Override
    public void run() {
        

    InetAddress IP = null;
        try {
            IP = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println(getClass().getName() + "Error : "+ ex.getMessage());
        }
    System.out.println("The server has been started on "+IP+":"+puertoServer +"");

        while (true) {
            try {
                this.runServer();
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

   
}
