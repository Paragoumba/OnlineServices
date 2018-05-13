package fr.paragoumba.onlineservices.server;

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

        int PORT = Integer.parseInt(args[0]);

        try(ServerSocket serverSocket = new ServerSocket(PORT);
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            String command;

            while ((command = in.readLine()) != null){

                if (hasToShutdown) break;

                System.out.println("C:" + command);

                int response = CommandInterpreter.interpret(command);

                System.out.println("R:" + response);
                out.println(response);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
