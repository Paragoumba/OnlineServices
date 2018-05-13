package fr.paragoumba.onlineservices.server;

import fr.paragoumba.onlineservices.api.Command;
import fr.paragoumba.onlineservices.api.Status;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class CommandInterpreter {

    private CommandInterpreter(){}

    private static HashMap<String, Map.Entry<Command, String>> commands = new HashMap<>();

    static void init(){

        loadCommands();
        commands.put("test", new SimpleEntry<>(args -> {

            System.out.println("test");
            return Status.OK;

        }, "test"));
        commands.put("ping", new SimpleEntry<>(args -> Status.PING, "desc"));
        commands.put("shutdown", new SimpleEntry<>(args -> {

            System.out.println("Shutting down.");
            Server.hasToShutdown = true;

            return Status.OK;

        }, "Shutdowns the server."));

    }

    static int interpret(String command){

        String[] cmdArgs = command.split(" ");

        if (cmdArgs.length > 0 && !cmdArgs[0].equals("")) {

            command = cmdArgs[0];
            String[] justArgs = new String[cmdArgs.length - 1];

            System.arraycopy(cmdArgs, 1, justArgs, 0, cmdArgs.length - 1);

            cmdArgs = justArgs;
            Map.Entry<Command, String> commandEntry = commands.get(command.toLowerCase());

            System.out.println(command + ", " + Arrays.toString(cmdArgs));

            if (commandEntry != null) return commandEntry.getKey().execute(cmdArgs);
            else return Status.NOT_FOUND;

        } else return Status.NOT_FOUND;

    }

    private static void loadCommands(){

        commands.put("list", new AbstractMap.SimpleEntry<>(args -> {

            StringBuilder listBuilder = new StringBuilder();

            for (Map.Entry<String, Map.Entry<Command, String>> cmd : commands.entrySet()){

                listBuilder.append(cmd.getKey()).append("\n");

            }

            listBuilder.append(commands.keySet().size()).append(" commands.");
            System.out.println(listBuilder);

            return Status.OK;

        }, "Displays a list of available commands."));
        commands.put("help", new AbstractMap.SimpleEntry<>(args -> {

            System.out.println("Help :");

            StringBuilder helpBuilder = new StringBuilder();

            for (Map.Entry<String, Map.Entry<Command, String>> entry : commands.entrySet()){

                helpBuilder.append(entry.getKey()).append("\n").append("  ").append(entry.getValue().getValue()).append("\n");

            }

            helpBuilder.append(commands.keySet().size()).append(" commands.");
            System.out.println(helpBuilder);

            return Status.OK;

        }, "Displays a list of available commands with their description. To just see a list of command type \"list\"."));
        commands.put("reload", new AbstractMap.SimpleEntry<>(args -> {

            loadCommands();
            return Status.OK;

        }, "Reloads all commands."));

        try {

            File dir = new File("");
            File commandsDir = new File(dir.getAbsolutePath(), "commands");

            if (!commandsDir.exists()){

                commandsDir.mkdirs();
                System.out.println("Commands directory created.");

            } else {

                System.out.println("Commands' directory found.");

            }

            File[] files = commandsDir.listFiles();
            Yaml yaml = new Yaml();

            if (files != null){

                for (File file : files){

                    System.out.println(file.getName());

                    if (file.getName().endsWith(".jar")){

                        JarFile jarFile = new JarFile(file);
                        JarEntry commandYMLFile = jarFile.getJarEntry("command.yml");

                        if (commandYMLFile != null){

                            HashMap<String, String> props = (HashMap<String, String>) yaml.load(jarFile.getInputStream(commandYMLFile));
                            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());

                            String command = props.get("command");

                            if (command == null || command.equals("") || command.split(" ").length < 1) continue;

                            commands.put(command, new AbstractMap.SimpleEntry<>((Command) Class.forName(props.get("package"), true, classLoader).newInstance(), props.get("description")));

                        }
                    }
                }
            }

        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | IOException e){

            e.printStackTrace();

        }
    }
}
