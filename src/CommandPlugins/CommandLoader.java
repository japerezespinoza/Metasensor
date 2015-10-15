/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommandPlugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Utilities.Configuration;

/**
 *
 * @author Jorge Antonio Pérez Espinoza
 */
public final class CommandLoader {

    private final HashMap<String, Command> commands;

    public CommandLoader() {
        commands = new HashMap<>();
        loadFile();
    }

    public boolean loadFile() {
        System.out.println("Loading avaible commands from " + Configuration.CONFIG_COMMANDS_FILE);
        String text = "";
        try {
            try (BufferedReader newBufferedReader = Files.newBufferedReader(Paths.get(Configuration.CONFIG_COMMANDS_FILE), Charset.defaultCharset())) {
                while (newBufferedReader.ready()) {
                    String line = newBufferedReader.readLine();
                    text += line.trim();
                }
            }
            //Load Commands tags
            Pattern pattern = Pattern.compile("<COMMANDS>(.+?)</COMMANDS>");
            Matcher matcher = pattern.matcher(text);
            matcher.find();
            String commandList = matcher.group(1);

            //Load single commands
            pattern = Pattern.compile("<command>(.+?)</command>");
            matcher = pattern.matcher(commandList);
            while (matcher.find()) {

                // Load individual features of command
                String singleCommand = matcher.group(1);
                Pattern namePattern = Pattern.compile("<name>(.+?)</name>");
                Matcher nameMatcher = namePattern.matcher(singleCommand);
                nameMatcher.find();
                String name = nameMatcher.group(1);
                System.out.println("Command " + name +" OK");
                Pattern instPattern = Pattern.compile("<instruction>(.+?)</instruction>");
                Matcher instMatcher = instPattern.matcher(singleCommand);
                instMatcher.find();
                String instruction = instMatcher.group(1);

                Pattern descPattern = Pattern.compile("<description>(.+?)</description>");
                Matcher descMatcher = descPattern.matcher(singleCommand);
                descMatcher.find();
                String description = descMatcher.group(1);
                commands.put(name, new Command(name, instruction, description));
            }
            // Load command for testing purpose
            
            commands.put("check_test",new Command ("check_test","check_test","Command for test de monitor"));
            System.out.println("Command check_test OK");

            System.out.println(commands.size()+" commands has been loaded");
        System.out.println("");
            return true;
        } catch (IOException ex) {
        }
        return false;

    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    
}
