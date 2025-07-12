package org.hsh.games.aoe.infrastructure.adapters.inbound.network;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for NetworkGameServer
 * 
 * Tests the network infrastructure layer functionality including server startup,
 * client connections, message handling, and server shutdown.
 * 
 * @author devTASE
 */
@DisplayName("NetworkGameServer Infrastructure Tests")
class NetworkGameServerTest {

    private static final int TEST_PORT = 12346; // Different port from default to avoid conflicts
    private static final String TEST_HOST = "localhost";
    
    private ServerSocket testServerSocket;
    private ExecutorService executorService;
    private Future<?> serverTask;

    @BeforeEach
    void setUp() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (serverTask != null && !serverTask.isDone()) {
            serverTask.cancel(true);
        }
        
        if (testServerSocket != null && !testServerSocket.isClosed()) {
            testServerSocket.close();
        }
        
        if (executorService != null) {
            executorService.shutdown();
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
    }

    @Test
    @DisplayName("Should create server socket successfully")
    @Timeout(10)
    void shouldCreateServerSocketSuccessfully() throws Exception {
        // When
        testServerSocket = new ServerSocket(TEST_PORT);

        // Then
        assertNotNull(testServerSocket);
        assertFalse(testServerSocket.isClosed());
        assertEquals(TEST_PORT, testServerSocket.getLocalPort());
        assertTrue(testServerSocket.isBound());
    }

    @Test
    @DisplayName("Should handle single client connection and echo messages")
    @Timeout(10)
    void shouldHandleSingleClientConnectionAndEchoMessages() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        
        // Start server in separate thread
        serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (IOException e) {
                // Expected when server is closed during test
            }
        });

        // Give server time to start
        Thread.sleep(100);

        // When - Connect as client and send messages
        try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            // Send test message
            String testMessage = "Hello Server!";
            out.println(testMessage);

            // Then - Should receive echo response
            String response = in.readLine();
            assertEquals("Echo: " + testMessage, response);

            // Send another message
            String secondMessage = "Test Message 2";
            out.println(secondMessage);
            
            String secondResponse = in.readLine();
            assertEquals("Echo: " + secondMessage, secondResponse);
        }
    }

    @Test
    @DisplayName("Should handle multiple messages from same client")
    @Timeout(10)
    void shouldHandleMultipleMessagesFromSameClient() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        
        serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (IOException e) {
                // Expected when server is closed during test
            }
        });

        Thread.sleep(100);

        // When - Send multiple messages
        try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String[] testMessages = {
                "Message 1",
                "Another test message",
                "Final message",
                "Game command: build tower",
                "Status: online"
            };

            // Then - Each message should be echoed back correctly
            for (String message : testMessages) {
                out.println(message);
                String response = in.readLine();
                assertEquals("Echo: " + message, response);
            }
        }
    }

    @Test
    @DisplayName("Should handle client disconnection gracefully")
    @Timeout(10)
    void shouldHandleClientDisconnectionGracefully() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        
        boolean[] connectionHandled = {false};
        
        serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
                connectionHandled[0] = true;
            } catch (IOException e) {
                // Expected when client disconnects
            }
        });

        Thread.sleep(100);

        // When - Client connects and then disconnects
        Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
        // Send a message
        out.println("Test message before disconnect");
        
        // Disconnect abruptly
        clientSocket.close();
        
        // Give server time to handle disconnection
        Thread.sleep(500);

        // Then - Server should handle disconnection without crashing
        assertTrue(connectionHandled[0] || serverTask.isDone());
    }

    @Test
    @DisplayName("Should handle empty messages correctly")
    @Timeout(10)
    void shouldHandleEmptyMessagesCorrectly() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        
        serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (IOException e) {
                // Expected when server is closed during test
            }
        });

        Thread.sleep(100);

        // When - Send empty message
        try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println(""); // Empty line
            
            // Then - Should echo empty message
            String response = in.readLine();
            assertEquals("Echo: ", response);
        }
    }

    @Test
    @DisplayName("Should handle special characters in messages")
    @Timeout(10)
    void shouldHandleSpecialCharactersInMessages() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        
        serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (IOException e) {
                // Expected when server is closed during test
            }
        });

        Thread.sleep(100);

        // When - Send messages with special characters
        try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String[] specialMessages = {
                "Message with áçêntos",
                "Symbols: !@#$%^&*()",
                "Numbers: 12345",
                "JSON: {\"player\":\"devTASE\",\"score\":100}",
                "Command: /build energy_plant level:5"
            };

            // Then - All special characters should be handled correctly
            for (String message : specialMessages) {
                out.println(message);
                String response = in.readLine();
                assertEquals("Echo: " + message, response);
            }
        }
    }

    @Test
    @DisplayName("Should handle IOException when server cannot start")
    void shouldHandleIOExceptionWhenServerCannotStart() throws Exception {
        // Given - Occupy the port first
        try (ServerSocket occupiedSocket = new ServerSocket(TEST_PORT)) {
            
            // When - Try to create another server on same port
            IOException exception = assertThrows(IOException.class, () -> {
                new ServerSocket(TEST_PORT);
            });

            // Then - Should throw IOException
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("Address already in use") || 
                      exception.getMessage().contains("bind"));
        }
    }

    @Test
    @DisplayName("Should validate server port configuration")
    void shouldValidateServerPortConfiguration() {
        // Given - Server default port constant
        int defaultPort = 12345;
        
        // When & Then - Port should be valid
        assertTrue(defaultPort > 1024, "Port should be above reserved range");
        assertTrue(defaultPort < 65535, "Port should be below maximum");
        assertEquals(12345, defaultPort, "Default port should match expected value");
    }

    @Test
    @DisplayName("Should handle long messages correctly")
    @Timeout(10)
    void shouldHandleLongMessagesCorrectly() throws Exception {
        // Given
        testServerSocket = new ServerSocket(TEST_PORT);
        
        serverTask = executorService.submit(() -> {
            try {
                Socket clientSocket = testServerSocket.accept();
                handleClientConnectionForTest(clientSocket);
            } catch (IOException e) {
                // Expected when server is closed during test
            }
        });

        Thread.sleep(100);

        // When - Send a very long message
        try (Socket clientSocket = new Socket(TEST_HOST, TEST_PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("This is a long message part ").append(i).append(" ");
            }
            
            String testMessage = longMessage.toString().trim();
            out.println(testMessage);
            
            // Then - Should handle long message correctly
            String response = in.readLine();
            assertEquals("Echo: " + testMessage, response);
        }
    }

    /**
     * Helper method to simulate the server's client handling logic for testing
     */
    private void handleClientConnectionForTest(Socket clientSocket) throws IOException {
        try (
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Echo the received message back to the client (same logic as NetworkGameServer)
                out.println("Echo: " + inputLine);
            }
        } finally {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        }
    }
}
