package fr.paragoumba.onlineservices.server;

import fr.paragoumba.onlineservices.api.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static fr.paragoumba.onlineservices.server.Server.hasToShutdown;

public class ConnectionManagement extends Thread {

    private Socket socket;

    ConnectionManagement(Socket socket){

        this.socket = socket;

    }

    @Override
    public void run() {

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Client " + socket.getInetAddress() + ":" + socket.getLocalPort() + " connected !");

            String command;

            while ((command = in.readLine()) != null) {

                if (hasToShutdown) break;

                System.out.println("C:" + command);

                Response response = CommandInterpreter.interpret(command);

                System.out.println("S:" + response.getStatus() + (response.getResponse() != null ? "\nR:" + response.getResponse() : ""));
                out.println(response);

            }

            socket.close();
            System.out.println("Client " + socket.getInetAddress() + ":" + socket.getLocalPort() + " disconnected !");

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
