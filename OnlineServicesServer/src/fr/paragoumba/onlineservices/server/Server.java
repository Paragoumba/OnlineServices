package fr.paragoumba.onlineservices.server;

import java.io.IOException;
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

        } catch (Exception e){

            System.out.println("Error while parsing port, default port 1519 will be used.");

        }

        System.out.println("Listening on localhost:" + port);

        Socket socket = null;

        try(ServerSocket serverSocket = new ServerSocket(port)){

            do {

                socket = serverSocket.accept();
                ConnectionManagement connectionManagement = new ConnectionManagement(socket);

                connectionManagement.start();

            } while (socket != null && !hasToShutdown);

        } catch (IOException e) {

            e.printStackTrace();

        }

        if (socket != null) {

            try {

                socket.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }
}
