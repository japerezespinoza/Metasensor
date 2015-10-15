/*
 * @(#)BroadcastClient.java 		1.0 23/04/2014
 * 
 * Copyrigth (C) 2013 Jorge Antonio Perez Espinoza.
 *
 * Centro de Investigación y de Estudios Avanzados 
 * del Instituto Politécnico Nacional - Tamaulipas 
 * jperez@tamps.cinvestav.mx
 */
package Discovery;

import Utilities.Configuration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the class for implementing the broadcast client and register it to
 * the distributor node
 * @author Jorge Antonio Pérez Espinoza
 */
public class BroadcastClient{

    private DatagramSocket c;
    //We modified the message for include the instance public ip
    private String MESSAGE_RECEIVED = "V_REGISTER:";
    private final String MESSAGE_SENT = "REGISTER_OK";

    /**
     * Request a collecetor with a broadcast message
     *
     * @return True registration was successfull, false in other way
     */
    public boolean requestCollector() {
        boolean result = false;

        String publicIP = BroadcastClient.getPublicIP();
        // We have a valid IP
        if (publicIP != null) {
            this.MESSAGE_RECEIVED = this.MESSAGE_RECEIVED + publicIP;
        }
        try {

            c = new DatagramSocket(Configuration.DISTRIBUTOR_BROADCAST_PORT_CLIENT);
            c.setBroadcast(true);
            c.setSoTimeout(Configuration.DISTRIBUTOR_BROADCAST_TIMEOUT);
            byte[] sendData = MESSAGE_RECEIVED.getBytes();
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), Configuration.DISTRIBUTOR_BROADCAST_PORT_SERVER);
                c.send(sendPacket);
                System.out.println("Broadcasting for search Distributor to: 255.255.255.255-> " + "V_REGISTER");
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();

                    if (broadcast == null) {
                        continue;
                    }
                    System.out.println("Broadcast : " + broadcast);
                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, Configuration.DISTRIBUTOR_BROADCAST_PORT_SERVER);
                        c.send(sendPacket);
                    } catch (IOException e) {
                        System.out.println("Exception: " + e.getMessage());

                    }

                }
            }
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);
            System.out.println("Broadcast response from: " + receivePacket.getAddress().getHostAddress() + "-> " + MESSAGE_SENT);

            String message = new String(receivePacket.getData()).trim();
            if (message.equals(MESSAGE_SENT)) {
                String IPAddress = receivePacket.getAddress().toString().substring(1);
                result = true;

            }
            c.close();
        } catch (IOException ex) {

            result = false;
        }
        c.close();
        return result;
    }   
    /**
     * This methods returns the ip public of the node where is executed
     * @return The IP public is exists, other way return null
     */
    public static String getPublicIP(){
        
        try {
            String ip ="";
            URL url = new URL("http://checkip.amazonaws.com");
            URLConnection con = url.openConnection();
            con.setConnectTimeout(Configuration.TIMEOUT_IP_PUBLIC_DISCOVER);
            con.setReadTimeout(5*1000);
            InputStreamReader in = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(in);
            ip =   br.readLine();
            if(BroadcastClient.validate(ip)){
                return ip;
                
            }
            else{
                return "0.0.0.0";
            }
        } catch (MalformedURLException ex) {
            return "0.0.0.0";
        } catch (IOException ex) {
            return "0.0.0.0";
        }
    }

    public boolean requestCollectorStatic() {
        boolean result = false;

        String publicIP = BroadcastClient.getPublicIP();
        // We have a valid IP
           
        if (publicIP != null) {
            this.MESSAGE_RECEIVED = this.MESSAGE_RECEIVED + publicIP;
        }
        try {

            c = new DatagramSocket(Configuration.DISTRIBUTOR_BROADCAST_PORT_CLIENT);
           // c.setBroadcast(true);
            c.setSoTimeout(Configuration.DISTRIBUTOR_BROADCAST_TIMEOUT);
            byte[] sendData = MESSAGE_RECEIVED.getBytes();
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(Configuration.DISTRIBUTOR_BROADCAST_STATIC_ADDRESS), Configuration.DISTRIBUTOR_BROADCAST_PORT_SERVER);
                c.send(sendPacket);
                System.out.println("Broadcasting for search Distributor to:"+Configuration.DISTRIBUTOR_BROADCAST_STATIC_ADDRESS +"-> " + "V_REGISTER");
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();

                    if (broadcast == null) {
                        continue;
                    }
                    System.out.println("Broadcast : " + broadcast);
                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, Configuration.DISTRIBUTOR_BROADCAST_PORT_SERVER);
                        c.send(sendPacket);
                    } catch (IOException e) {
                        System.out.println("Exception: " + e.getMessage());

                    }

                }
            }
            byte[] recvBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
            c.receive(receivePacket);
            System.out.println("Broadcast response from: " + receivePacket.getAddress().getHostAddress() + "-> " + MESSAGE_SENT);

            String message = new String(receivePacket.getData()).trim();
            if (message.equals(MESSAGE_SENT)) {
                String IPAddress = receivePacket.getAddress().toString().substring(1);
                result = true;

            }
            c.close();
        } catch (IOException ex) {

            result = false;
        }
        c.close();
        return result;
    }    
    
    
    
    /**
     * This method verifies if a String is a IP Address valid
     * @param ip The presumed IP
     * @return True if is IP, false other way
     */
public static boolean validate(String ip){          
      String PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
      Pattern pattern = Pattern.compile(PATTERN);
      Matcher matcher = pattern.matcher(ip);
      return matcher.matches();             
}    
      
}
