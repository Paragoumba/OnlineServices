package fr.paragoumba.onlineservices.api;

public class Response {

    /**
     * Represents a Response provided by an executed Command.
     */

    public Response(){}

    /**
     * A Response without message
     * @param status returned code (Status)
     */
    public Response(int status){

        this.status = status;

    }

    /**
     * A Response with message
     * @param status returned code (Status)
     * @param response returned message
     */
    public Response(int status, String response){

        this.status = status;
        this.response = response;

    }

    private int status;
    private String response;

    public int getStatus() {

        return status;

    }

    public String getResponse() {

        return response;

    }

    public void setStatus(int status) {

        this.status = status;

    }

    public void setResponse(String response) {

        this.response = response;

    }

    @Override
    public String toString() {

        return "{status=" + status + (response != null ? ", response=" + response : "") + "}";

    }
}
