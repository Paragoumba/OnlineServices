package fr.paragoumba.onlineservices.server;

import fr.paragoumba.onlineservices.api.Command;
import fr.paragoumba.onlineservices.api.Plugin;
import fr.paragoumba.onlineservices.api.Response;
import fr.paragoumba.onlineservices.api.Status;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandInterpreter {

    private CommandInterpreter(){}

    private static ArrayList<Plugin> plugins = new ArrayList<>();
    private static HashMap<String, Command> commands = new HashMap<>();

    static void init(){

        loadPlugins();
        plugins.add(new Plugin("OnlineServices", "1.0", "Paragoumba") {

            @Override
            public void onEnable() {

                commands.put("test", new Command("Test command.") {

                    @Override
                    public Response execute(String[] args) {

                        return new Response(Status.OK, "test");

                    }
                });
                commands.put("ping", new Command("Returns \"Pong !\" and response time.") {

                    @Override
                    public Response execute(String[] args) {

                        return new Response(Status.OK);

                    }
                });
                commands.put("reload", new Command("Reloads all the plugins.") {

                    @Override
                    public Response execute(String[] args) {

                        unloadPlugins();

                        String response = "Plugins reloaded.";

                        loadPlugins();
                        System.out.println(response);

                        return new Response(Status.OK, response);

                    }
                });
                commands.put("unload", new Command("Unloads all the plugins.") {

                    @Override
                    public Response execute(String[] args) {

                        String response = "Plugins unloaded.";

                        disablePlugins();
                        System.out.println(response);

                        return new Response(Status.OK, response);

                    }
                });
                commands.put("pllist", new Command("Lists all installed plugins.") {

                    @Override
                    public Response execute(String[] strings) {

                        StringBuilder pluginList = new StringBuilder();

                        for (Plugin plugin : plugins) pluginList.append(plugin.getName()).append(" (v").append(plugin.getVersion()).append(") :\n    ").append("Author: ").append(plugin.getAuthor()).append("\n");

                        return new Response(Status.OK, pluginList.toString());

                    }
                });
                commands.put("cmdlist", new Command("Lists all the plugins and their available commands.") {

                    @Override
                    public Response execute(String[] args) {

                        StringBuilder listBuilder = new StringBuilder();

                        for (Entry<String, Command> cmd : commands.entrySet()) listBuilder.append(cmd.getKey()).append("\n");

                        listBuilder.append(commands.keySet().size()).append(" commands.");

                        return new Response(Status.OK, listBuilder.toString());

                    }
                });
                commands.put("help", new Command("Displays the description of the available commands.") {

                    @Override
                    public Response execute(String[] args) {

                        StringBuilder helpBuilder = new StringBuilder();

                        helpBuilder.append("Help :\n");

                        for (Entry<String, Command> entry : commands.entrySet()) helpBuilder.append(entry.getKey()).append("\n").append("  ").append(entry.getValue().getDescription()).append("\n");

                        helpBuilder.append(plugins.size()).append(" commands.");

                        return new Response(Status.OK, helpBuilder.toString());

                    }
                });
                commands.put("shutdown", new Command("Shutdowns the server.") {

                    @Override
                    public Response execute(String[] args) {

                        String response = "Shutting down.";

                        System.out.println(response);
                        Server.hasToShutdown = true;

                        return new Response(Status.OK, response);

                    }
                });
            }
        });

        enablePlugins();

    }

    public static void registerCommand(String commandString, Command command){

        commands.put(commandString, command);

    }

    static Response interpret(String cmd){

        String[] cmdArgs = cmd.split(" ");

        if (cmdArgs.length > 0 && !cmdArgs[0].equals("")) {

            cmd = cmdArgs[0];

            String[] justArgs = new String[cmdArgs.length - 1];

            System.arraycopy(cmdArgs, 1, justArgs, 0, cmdArgs.length - 1);

            cmdArgs = justArgs;
            Command command = commands.get(cmd.toLowerCase());

            System.out.println(cmd + ", " + Arrays.toString(cmdArgs));

            System.out.println(commands);

            if (command != null) return command.execute(cmdArgs);

        }

        return new Response(Status.NOT_FOUND);

    }

    private static void loadPlugins(){

        try {

            File dir = new File("");
            File pluginsDir = new File(dir.getAbsolutePath(), "plugins");

            if (!pluginsDir.exists()){

                pluginsDir.mkdirs();
                System.out.println("Commands' directory created.");

            } else System.out.println("Commands' directory found.");

            File[] files = pluginsDir.listFiles();
            Yaml yaml = new Yaml();

            if (files != null){

                for (File file : files){

                    if (file.getName().endsWith(".jar")){

                        System.out.println(file.getName());

                        JarFile jarFile = new JarFile(file);
                        JarEntry pluginYMLFile = jarFile.getJarEntry("plugin.yml");

                        if (pluginYMLFile != null) {

                            try {

                                HashMap<String, String> props = (HashMap<String, String>) yaml.load(jarFile.getInputStream(pluginYMLFile));
                                URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
                                Plugin plugin = (Plugin) Class.forName(props.get("package"), true, classLoader).getConstructor(String.class, String.class, String.class).newInstance(props.get("name"), props.get("version"), props.get("author"));

                                plugins.add(plugin);

                            } catch (InvocationTargetException | NoSuchMethodException e) {

                                System.out.println("Error in loading main class. Verify that it extends Plugin.");

                            } catch (ClassCastException e){

                                System.out.println("Error in loading " + file.getName() + ". The file plugin.yml is missing or corrupted.");

                            }
                        }
                    }
                }
            }

        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | IOException e){

            e.printStackTrace();

        }
    }

    private static void unloadPlugins(){

        disablePlugins();
        plugins.clear();

    }

    private static void enablePlugins(){

        for (Plugin plugin : plugins) plugin.onEnable();

    }

    private static void disablePlugins(){

        for (Plugin plugin : plugins) plugin.onDisable();

    }
}
