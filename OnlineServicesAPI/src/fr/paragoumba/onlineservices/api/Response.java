package fr.paragoumba.onlineservices.api;

public class Response {

    public Response(){}

    public Response(int status){

        this.status = status;

    }

    public Response(String response){

        this.status = Status.UNDEFINED;
        this.response = response;

    }

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

        return "{status=" + status + (response != null ? ", reponse=" + response : "") + "}";

    }
}
