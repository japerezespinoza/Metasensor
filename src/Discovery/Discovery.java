/*
 * @(#)Discovery.java 		1.0 23/04/2014
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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used for discover the distributor node in the cloud enviroment
 * @author Jorge Antonio Pérez Espinoza
 */
public class Discovery {
    
    public boolean discoveryDistributor() {

        boolean result = false;

        // We try to discover three times
        for (int i = 0; i < 3; i++) {
            BroadcastClient b = new BroadcastClient();
            result = b.requestCollector();
            if(result)
                break;
        }

        //If broadcast dont works, we try with static IP
        if (!result) {
            System.out.println("Attempting with unicast");
            BroadcastClient b = new BroadcastClient();
            result = b.requestCollectorStatic();
        }
        return result;

    }

}
