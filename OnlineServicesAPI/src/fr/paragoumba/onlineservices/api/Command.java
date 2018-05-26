package fr.paragoumba.onlineservices.api;

public abstract class Command {

    public Command(String command){

        this.command = command;
        this.description = "";
        this.usage = "";

    }

    public Command(String command, String description, String usage){

        this.command = command;
        this.description = description;
        this.usage = usage;

    }

    private final String command;
    private final String description;
    private final String usage;

    public abstract Response execute(String[] strings);

    public String getCommand() {

        return command;

    }

    public String getDescription() {

        return description;

    }

    public String getUsage() {

        return usage;

    }
}
