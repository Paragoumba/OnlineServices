package fr.paragoumba.onlineservices.server;

import fr.paragoumba.onlineservices.api.Command;
import fr.paragoumba.onlineservices.api.Plugin;
import fr.paragoumba.onlineservices.api.Response;
import fr.paragoumba.onlineservices.api.Status;

import java.util.Map;

import static fr.paragoumba.onlineservices.server.CommandInterpreter.*;

public class OnlineServicesCommands extends Plugin {

    OnlineServicesCommands(String name, String author, String description){
        
        super(name, author, description);
        
    }
    
    @Override
    public void onEnable() {

        registerCommand(new Command("ping", "Returns \"Pong !\" and response time.", "") {

            @Override
            public Response execute(String[] strings) {

                return new Response(Status.OK);

            }
        });
        registerCommand(new Command("reload", "Reloads all the plugins.", "") {

            @Override
            public Response execute(String[] strings) {

                unloadPlugins();

                String response = "Plugins reloaded.";

                loadPlugins();
                System.out.println(response);

                return new Response(Status.OK, response);

            }
        });
        registerCommand(new Command("unload", "Unloads all the plugins.", "") {

            @Override
            public Response execute(String[] strings) {

                String response;

                if (strings.length < 1) {

                   response = "Plugins unloaded.";

                   unloadPlugins();
                   System.out.println(response);

                } else {

                    response = "Plugin \"" + strings[0] + "\" unloaded.";

                }

                return new Response(Status.OK, response);

            }
        });
        registerCommand(new Command("pl", "Lists all installed plugins.", "") {

            @Override
            public Response execute(String[] strings) {

                StringBuilder pluginList = new StringBuilder();

                for (Plugin plugin : plugins) pluginList.append(plugin.getName()).append(" (v").append(plugin.getVersion()).append("):\\n    ").append("Author: ").append(plugin.getAuthor()).append("\\n");

                pluginList.append(plugins.size()).append(" plugin").append(plugins.size() > 1 ? "s" : "").append(".");

                return new Response(Status.OK, pluginList.toString());

            }
        });
        registerCommand(new Command("cl", "Lists all the plugins and their available commands.", "") {

            @Override
            public Response execute(String[] strings) {

                StringBuilder listBuilder = new StringBuilder();

                for (Map.Entry<String, Command> cmd : commands.entrySet()) listBuilder.append(cmd.getKey()).append("\\n");

                listBuilder.append(commands.keySet().size()).append(" command").append(commands.size() > 1 ? "s" : "").append(".");

                return new Response(Status.OK, listBuilder.toString());

            }
        });
        registerCommand(new Command("help", "Displays the description of the available commands.", "help [command]") {

            @Override
            public Response execute(String[] strings) {

                StringBuilder helpBuilder = new StringBuilder();

                if (strings.length < 1){

                    helpBuilder.append("Usage: ").append(new Command("") {
                        @Override
                        public Response execute(String[] strings) {
                            return null;
                        }
                    }.getUsage());

                } else {

                    helpBuilder.append("Help :\\n");

                    for (Map.Entry<String, Command> entry : commands.entrySet()) helpBuilder.append(entry.getKey()).append("\\n").append("  ").append(entry.getValue().getDescription()).append("\\n");

                    helpBuilder.append(commands.size()).append(" command").append(commands.size() > 1 ? "s" : "").append(".");

                }

                return new Response(Status.OK, helpBuilder.toString());

            }
        });
        registerCommand(new Command("shutdown", "Shutdowns the server.", "") {

            @Override
            public Response execute(String[] strings) {

                String response = "Shutting down.";

                System.out.println(response);
                Server.hasToShutdown = true;

                return new Response(Status.OK, response);

            }
        });
        registerCommand(new Command("exit", "Disconnects client from server.", "") {

            @Override
            public Response execute(String[] strings) {

                return new Response(Status.OK);

            }
        });
    }
}
