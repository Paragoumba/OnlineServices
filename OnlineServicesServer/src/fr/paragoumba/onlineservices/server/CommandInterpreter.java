package fr.paragoumba.onlineservices.server;

import fr.paragoumba.onlineservices.api.Plugin;
import fr.paragoumba.onlineservices.api.Command;
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
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandInterpreter {

    private CommandInterpreter(){}

    static ArrayList<Plugin> plugins = new ArrayList<>();
    static SortedMap<String, Command> commands = new TreeMap<>();

    static void init(){

        loadPlugins();
        plugins.add(new OnlineServicesCommands("OnlineServices", "1.0", "Paragoumba"));
        enablePlugins();

    }

    public static void registerCommand(Command command){

        commands.put(command.getCommand(), command);

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

            if (command != null) return command.execute(cmdArgs);

        }

        return new Response(Status.NOT_FOUND, "Unknown command.");

    }

    static void loadPlugins(){

        try {

            File dir = new File("");
            File pluginsDir = new File(dir.getAbsolutePath(), "plugins");

            if (!pluginsDir.exists()){

                pluginsDir.mkdirs();
                System.out.println("Plugins' directory created.");

            } else {

                System.out.println("Plugins' directory found.");

            }

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

                            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {

                                System.out.println("Error in loading main class. Verify that it extends Plugin.");

                            } catch (ClassCastException e){

                                System.out.println("Error in loading " + file.getName() + ". The file plugin.yml is missing or corrupted.");

                            }
                        }
                    }
                }
            }

        } catch (IOException e){

            e.printStackTrace();

        }
    }

    static void unloadPlugins(){

        disablePlugins();

        for (int i = 0; i < plugins.size(); ++i) if (!(plugins.get(i) instanceof OnlineServicesCommands)) plugins.remove(i);

        commands.clear();

        for (Plugin plugin : plugins) plugin.onEnable();

    }

    private static void enablePlugins(){

        for (Plugin plugin : plugins) plugin.onEnable();

    }

    private static void disablePlugins(){

        for (Plugin plugin : plugins) plugin.onDisable();

    }
}
