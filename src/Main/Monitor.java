/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import CommandPlugins.CommandManager;
import Pusher.Suscriber;
import java.util.HashMap;

/**
 *
 * @author cti210002
 */
public class Monitor {

    private final CommandManager commandManager;
    private final HashMap<String, Suscriber> suscribers;

    public Monitor() {

        this.commandManager = new CommandManager();
        this.suscribers = new HashMap();
    }

    public synchronized CommandManager getCommandManager() {
        return commandManager;
    }

    public synchronized  HashMap<String, Suscriber> getSuscribers() {
        return suscribers;
    }
    
    
}
