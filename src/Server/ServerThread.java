/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import CommandPlugins.CommandManager;
import Main.Monitor;
import Server.Protocol;
import Pusher.ServerPusher;
import Pusher.Suscriber;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Jorge Antonio
 */
public class ServerThread extends Thread {

    private final Socket clientSocket;        //Socket de conexion entrante.
    private Monitor monitor;
    /**
     * Constructor de la clase para inicializar las variables.
     *
     * @param clientSocket Socket Socket de la conexion entrante.
     * @param monitor
     */
    public ServerThread(Socket clientSocket, Monitor monitor) {
        this.clientSocket = clientSocket;
        this.monitor = monitor;
        
    }

    @Override
    public void run() {

        Protocol p = new Protocol(this.clientSocket, monitor);
        p.initProcol();
    }
}
