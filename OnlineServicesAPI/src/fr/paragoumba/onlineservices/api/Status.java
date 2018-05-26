package fr.paragoumba.onlineservices.api;

public class Status {

    /**
     * This represents the status codes returned by executed commands.
     */

    private Status(){}

    /**
     * The response could not be parsed
     */
    public static final int RESPONSE_CORRUPTED = -1;

    /**
     *  Command executed. No problem :)
     */
    public static final int OK = 0;

    /**
     * Unknown command
     */
    public static final int NOT_FOUND = 1;

    /**
     * Wrong usage of command. It can be good to send a usage
     */
    public static final int USAGE_ERROR = 2;

    /**
     * Unknown problem
     */
    public static final int UNDEFINED = 3;

}
