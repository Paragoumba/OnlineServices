package fr.paragoumba.onlineservices.api;

public abstract class Command {

    public Command(){}

    public Command(String description){

        this.description = description;

    }

    private String description;

    public abstract Response execute(String[] strings);

    public String getDescription() {

        return description;

    }
}
