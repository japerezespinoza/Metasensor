/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pusher;

import CommandPlugins.Command;
import Utilities.Configuration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author centos
 */
public class SuscribersManager {
    
    private  HashMap<String, Suscriber> suscribers;

    public SuscribersManager() {
        suscribers = new HashMap<>();
    }

    public synchronized  boolean loadFile() {
        System.out.println("Loading suscribers from "+Configuration.CONFIG_SUSCRIBERS_FILE);
        String text = "";
        try {
            try (BufferedReader newBufferedReader = Files.newBufferedReader(Paths.get(Configuration.CONFIG_SUSCRIBERS_FILE), Charset.defaultCharset())) {
                while (newBufferedReader.ready()) {
                    String line = newBufferedReader.readLine();
                    text += line.trim();
                }
            }
            //Load Commands tags
            Pattern pattern = Pattern.compile("<SUSCRIBERS>(.+?)</SUSCRIBERS>");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()){
            
            String commandList = matcher.group(1);

            //Load single commands
            pattern = Pattern.compile("<suscriber>(.+?)</suscriber>");
            matcher = pattern.matcher(commandList);
            while (matcher.find()) {

                // Load individual features of command
                String singleCommand = matcher.group(1);
                Pattern namePattern = Pattern.compile("<address>(.+?)</address>");
                Matcher nameMatcher = namePattern.matcher(singleCommand);
                nameMatcher.find();
                String address = nameMatcher.group(1);
                System.out.println("Suscriber " + address +" loaded");
                Pattern instPattern = Pattern.compile("<interval>(.+?)</interval>");
                Matcher instMatcher = instPattern.matcher(singleCommand);
                instMatcher.find();
                int interval = Integer.parseInt(instMatcher.group(1));

                Pattern descPattern = Pattern.compile("<commands>(.+?)</commands>");
                Matcher descMatcher = descPattern.matcher(singleCommand);
                descMatcher.find();
                String commands = descMatcher.group(1);
                suscribers.put(address, new Suscriber(address,commands,interval));
            }
            System.out.println(suscribers.size()+" has been loaded");
        System.out.println("");
        return true;
            }
            else{
            System.out.println("No suscribers has been loaded");    
            return false;
            }
       
        } catch (IOException ex) {
            return false;
        }
        

    }

    public synchronized  HashMap<String, Suscriber> getSuscribers() {
        loadFile();
        return suscribers;
    }

    
    public synchronized  void saveSuscribers(){
    
       
       Map<String,Suscriber> first = suscribers;
Map<String,Suscriber> second = this.getSuscribers();
Map<String,Suscriber> third = first;

Iterator it1 = second.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry pairs = (Map.Entry) it1.next();
                if(!this.suscribers.containsKey((String)pairs.getKey())){
                
                    third.put((String) pairs.getKey(), (Suscriber) pairs.getValue());
                }
                it1.remove();
            }
        
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Configuration.CONFIG_SUSCRIBERS_FILE, true)))) {
            Iterator it = third.entrySet().iterator();
            while (it.hasNext()) {
                System.out.println("1231231");
                Map.Entry pairs = (Map.Entry) it.next();
                Suscriber sp = (Suscriber) pairs.getValue();
                out.println("<suscriber><address>" + sp.getIPAdress() + "</address><interval>"
                        + sp.getInterval() + "</interval><commands>" + sp.getCommand() + "</commands></suscriber>");
                it.remove();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Cant save suscribers...");
        }
        
        this.suscribers = (HashMap<String, Suscriber>) third;
        
       
    }
    
    

}
