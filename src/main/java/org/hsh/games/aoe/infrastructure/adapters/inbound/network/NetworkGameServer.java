package org.hsh.games.aoe.infrastructure.adapters.inbound.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple network server allowing connections from external clients
 * 
 * This server listens for connections on a specified port and 
 * echoes received messages back to the client. The architecture 
 * supports expansion to a more complex protocol if needed.
 * 
 * @author devTASE
 */
public class NetworkGameServer {

    private static final int PORT = 12345; // Default port for server

    public static void main(String[] args) {
        System.out.println("Starting Network Game Server...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT + ". Waiting for clients...");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    handleClientConnection(clientSocket);
                } catch (IOException e) {
                    System.out.println("Failed to connect to client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start server: " + e.getMessage());
        }
    }

    private static void handleClientConnection(Socket clientSocket) throws IOException {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Echo the received message back to the client
                out.println("Echo: " + inputLine);
            }
        } finally {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            clientSocket.close();
        }
    }
}
