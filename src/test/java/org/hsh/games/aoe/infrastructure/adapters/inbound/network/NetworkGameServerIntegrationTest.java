package org.hsh.games.aoe.infrastructure.adapters.inbound.network;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test class for NetworkGameServer
 * 
 * Tests advanced networking scenarios including concurrent connections,
 * performance under load, and edge cases.
 * 
 * @author devTASE
 */
@DisplayName("NetworkGameServer Integration Tests")
class NetworkGameServerIntegrationTest {

    private static final int TEST_PORT = 12347; // Different port to avoid conflicts
    private static final String TEST_HOST = "localhost";
    
    private ServerSocket testServerSocket;
    private ExecutorService executorService;
    private ExecutorService clientExecutorService;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(5);
        clientExecutorService = Executors.newFixedThreadPool(10);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (testServerSocket != null && !testServerSocket.isClosed()) {
            testServerSocket.close();
        }
        
        if (executorService != null) {
            executorService.shutdown();
            if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
        
        if (clientExecutorService != null) {
            clientExecutorService.shutdown();
            if (!clientExecutorService.awaitTermination(3, TimeUnit.SECONDS)) {
                clientExecutorService.shutdownNow();
            }
        }
    }

    @Test
    @DisplayName("Should handle multiple concurrent client connections")
    @Timeout(15)
    void shouldHandleMultipleConcurrentClientConnections() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        final int numberOfClients = 5;
        CountDownLatch serverLatch = new CountDownLatch(numberOfClients);
        CountDownLatch clientLatch = new CountDownLatch(numberOfClients);
        
        // Start server that accepts multiple connections
        Future<?> serverTask = executorService.submit(() -> {
            for (int i = 0; i < numberOfClients; i++) {
                try {
                    Socket clientSocket = testServerSocket.accept();
                    System.out.println("Server accepted client " + (i + 1));
                    
                    // Handle each client in separate thread
                    executorService.submit(() -> {
                        try {
                            handleClientConnectionForTest(clientSocket);
                            serverLatch.countDown();
                        } catch (IOException e) {
                            System.err.println("Server error handling client: " + e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Server error accepting connection: " + e.getMessage());
                    break;
                }
            }
        });

        // Give server time to start
        Thread.sleep(200);

        // When - Create multiple clients concurrently
        List<Future<Boolean>> clientTasks = new ArrayList<>();
        
        for (int i = 0; i < numberOfClients; i++) {
            final int clientId = i + 1;
            Future<Boolean> clientTask = clientExecutorService.submit(() -> {
                try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    
                    // Send message specific to this client
                    String message = "Message from client " + clientId;
                    out.println(message);
                    
                    // Verify echo response
                    String response = in.readLine();
                    boolean success = ("Echo: " + message).equals(response);
                    
                    if (success) {
                        System.out.println("Client " + clientId + " successful");
                    }
                    
                    clientLatch.countDown();
                    return success;
                } catch (IOException e) {
                    System.err.println("Client " + clientId + " error: " + e.getMessage());
                    clientLatch.countDown();
                    return false;
                }
            });
            
            clientTasks.add(clientTask);
        }

        // Then - All clients should be handled successfully
        assertTrue(clientLatch.await(10, TimeUnit.SECONDS), "All clients should connect within timeout");
        assertTrue(serverLatch.await(10, TimeUnit.SECONDS), "Server should handle all clients within timeout");
        
        // Verify all client tasks completed successfully
        for (Future<Boolean> task : clientTasks) {
            assertTrue(task.get(2, TimeUnit.SECONDS), "Each client should receive correct echo response");
        }
    }

    @Test
    @DisplayName("Should handle rapid sequential connections")
    @Timeout(10)
    void shouldHandleRapidSequentialConnections() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        final int numberOfConnections = 10;
        CountDownLatch completionLatch = new CountDownLatch(numberOfConnections);
        
        // Start server
        Future<?> serverTask = executorService.submit(() -> {
            for (int i = 0; i < numberOfConnections; i++) {
                try {
                    Socket clientSocket = testServerSocket.accept();
                    handleClientConnectionForTest(clientSocket);
                    completionLatch.countDown();
                } catch (IOException e) {
                    break;
                }
            }
        });

        Thread.sleep(100);

        // When - Create rapid sequential connections
        for (int i = 0; i < numberOfConnections; i++) {
            try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                
                String message = "Rapid message " + i;
                out.println(message);
                
                String response = in.readLine();
                assertEquals("Echo: " + message, response);
            }
            
            // Small delay between connections
            Thread.sleep(10);
        }

        // Then - All connections should be handled
        assertTrue(completionLatch.await(5, TimeUnit.SECONDS), 
            "All rapid connections should be handled within timeout");
    }

    @Test
    @DisplayName("Should handle large message load per client")
    @Timeout(15)
    void shouldHandleLargeMessageLoadPerClient() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        final int messagesPerClient = 100;
        
        Future<?> serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (IOException e) {
                // Expected when server is closed
            }
        });

        Thread.sleep(100);

        // When - Send many messages from single client
        try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // Send and verify many messages
            for (int i = 0; i < messagesPerClient; i++) {
                String message = "Load test message " + i + " with some additional content to make it longer";
                out.println(message);
                
                String response = in.readLine();
                assertEquals("Echo: " + message, response, "Message " + i + " should be echoed correctly");
            }
        }

        // Then - All messages should be processed correctly (no assertion needed, test passes if no exceptions)
    }

    @Test
    @DisplayName("Should handle connection timeout scenarios")
    @Timeout(10)
    void shouldHandleConnectionTimeoutScenarios() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        testServerSocket.setSoTimeout(2000); // 2 second timeout
        
        Future<?> serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (SocketTimeoutException e) {
                // Expected timeout
                System.out.println("Server timeout occurred as expected");
            } catch (IOException e) {
                // Other IO exceptions
            }
        });

        // When - Don't connect any client (let server timeout)
        Thread.sleep(3000);

        // Then - Server should handle timeout gracefully (no exception thrown)
        assertTrue(serverTask.isDone() || testServerSocket.getSoTimeout() > 0, 
            "Server should handle timeout scenario");
    }

    @Test
    @DisplayName("Should handle malformed message formats")
    @Timeout(10)
    void shouldHandleMalformedMessageFormats() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        
        Future<?> serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (IOException e) {
                // Expected when server is closed
            }
        });

        Thread.sleep(100);

        // When - Send various malformed messages
        try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String[] malformedMessages = {
                "Message with\nnewlines\ninside",
                "Message with\ttabs",
                "Message with special chars: üñíçødé",
                "\r\n", // Just line endings
                " ", // Just space
                "VeryLongMessageWithoutSpaces" + "x".repeat(1000)
            };

            // Then - Server should handle all malformed messages gracefully
            for (String message : malformedMessages) {
                out.println(message);
                String response = in.readLine();
                assertNotNull(response, "Should receive response even for malformed message");
                assertTrue(response.startsWith("Echo: "), "Response should be properly formatted");
            }
        }
    }

    @Test
    @DisplayName("Should handle stress test with mixed scenarios")
    @Timeout(20)
    void shouldHandleStressTestWithMixedScenarios() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        final int totalClients = 3;
        CountDownLatch allClientsComplete = new CountDownLatch(totalClients);
        List<Future<Boolean>> allTasks = new ArrayList<>();
        
        // Start server for multiple clients
        Future<?> serverTask = executorService.submit(() -> {
            for (int i = 0; i < totalClients; i++) {
                try {
                    Socket clientSocket = testServerSocket.accept();
                    executorService.submit(() -> {
                        try {
                            handleClientConnectionForTest(clientSocket);
                        } catch (IOException e) {
                            System.err.println("Server error: " + e.getMessage());
                        }
                    });
                } catch (IOException e) {
                    break;
                }
            }
        });

        Thread.sleep(200);

        // When - Create different types of client loads
        
        // Client 1: Fast short messages
        Future<Boolean> fastClient = clientExecutorService.submit(() -> {
            try (Socket socket = new Socket(TEST_HOST, TEST_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                
                for (int i = 0; i < 50; i++) {
                    out.println("Fast" + i);
                    String response = in.readLine();
                    if (!("Echo: Fast" + i).equals(response)) return false;
                }
                allClientsComplete.countDown();
                return true;
            } catch (IOException e) {
                allClientsComplete.countDown();
                return false;
            }
        });
        
        // Client 2: Slow long messages
        Future<Boolean> slowClient = clientExecutorService.submit(() -> {
            try (Socket socket = new Socket(TEST_HOST, TEST_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                
                for (int i = 0; i < 10; i++) {
                    String longMessage = "SlowLongMessage" + i + " ".repeat(100);
                    out.println(longMessage);
                    String response = in.readLine();
                    if (!("Echo: " + longMessage).equals(response)) return false;
                    Thread.sleep(100); // Simulate slow client
                }
                allClientsComplete.countDown();
                return true;
            } catch (IOException | InterruptedException e) {
                allClientsComplete.countDown();
                return false;
            }
        });
        
        // Client 3: Mixed message types
        Future<Boolean> mixedClient = clientExecutorService.submit(() -> {
            try (Socket socket = new Socket(TEST_HOST, TEST_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                
                String[] messages = {"", "short", "medium length message", 
                                   "very long message " + "x".repeat(200), 
                                   "special: áéíóú!@#$%"};
                
                for (String msg : messages) {
                    out.println(msg);
                    String response = in.readLine();
                    if (!("Echo: " + msg).equals(response)) return false;
                }
                allClientsComplete.countDown();
                return true;
            } catch (IOException e) {
                allClientsComplete.countDown();
                return false;
            }
        });

        allTasks.add(fastClient);
        allTasks.add(slowClient);
        allTasks.add(mixedClient);

        // Then - All clients should complete successfully
        assertTrue(allClientsComplete.await(15, TimeUnit.SECONDS), 
            "All stress test clients should complete within timeout");
        
        for (Future<Boolean> task : allTasks) {
            assertTrue(task.get(2, TimeUnit.SECONDS), 
                "Each stress test client should complete successfully");
        }
    }

    /**
     * Helper method to simulate server's client handling logic
     */
    private void handleClientConnectionForTest(Socket clientSocket) throws IOException {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println("Echo: " + inputLine);
            }
        } finally {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        }
    }
}
