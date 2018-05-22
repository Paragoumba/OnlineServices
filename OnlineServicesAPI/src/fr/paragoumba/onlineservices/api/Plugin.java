package fr.paragoumba.onlineservices.api;

public abstract class Plugin {

    public Plugin(){}

    protected Plugin(String name, String version, String author){

        this.name = name;
        this.version = version;
        this.author = author;

    }

    private String name;
    private String version;
    private String author;

    public void onEnable(){}

    public void onDisable(){}

    public String getName() {

        return name;

    }

    public String getVersion() {

        return version;

    }

    public String getAuthor() {

        return author;

    }
}
