package fr.paragoumba.onlineservices.api;

public abstract class Plugin {

    /**
     * Represents a loaded Plugin
     * @param name Name of the Plugin
     * @param version Version of the Plugin
     * @param author Author of the Plugin
     */
    protected Plugin(String name, String version, String author){

        this.name = name;
        this.version = version;
        this.author = author;

    }

    private final String name;
    private final String version;
    private final String author;

    /**
     * Executed after the Plugin has been loaded
     */
    public void onEnable(){}

    /**
     * Executed before the Plugin is unloaded
     */
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
