package fr.paragoumba.onlineservices.server;

import fr.paragoumba.onlineservices.api.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Paragoumba on 12/05/2018.
 */

public class Server {

    static boolean hasToShutdown = false;

    public static void main(String[] args) {

        CommandInterpreter.init();

        int port = 1519;

        try {

            port = Integer.parseInt(args[0]);

        } catch (NumberFormatException ignored){

            System.out.println("Error while parsing port, default port 1519 will be used.");

        }

        System.out.println("Listening on localhost:" + port);

        try(ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            System.out.println("Client " + socket.getLocalAddress() + ":" + socket.getLocalPort() + " connected !");

            String command;

            while ((command = in.readLine()) != null){

                if (hasToShutdown) break;

                System.out.println("C:" + command);

                Response response = CommandInterpreter.interpret(command);

                System.out.println("S:" + response.getStatus() + "\nR:" + response.getResponse());
                out.println(response);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
