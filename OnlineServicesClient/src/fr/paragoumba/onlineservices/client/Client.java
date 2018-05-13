package fr.paragoumba.onlineservices.client;

import fr.paragoumba.onlineservices.api.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try(Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){

            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            String fromServer;
            long start = 0;

            System.out.println("C:" + command);
            out.println(command);

            while ((fromServer = in.readLine()) != null){

                System.out.println("R:" + fromServer);

                if (start != 0 && fromServer.equals(Integer.toString(Status.PING))) System.out.println("Pong ! " + ((double) (System.currentTimeMillis() - start) / 1E3) + "s");

                command = scanner.nextLine();

                System.out.println("C:" + command);

                if (command.equalsIgnoreCase("ping")) start = System.currentTimeMillis();
                else if (command.equalsIgnoreCase("exit")) break;

                out.println(command);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}
