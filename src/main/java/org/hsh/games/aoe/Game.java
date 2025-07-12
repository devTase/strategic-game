package org.hsh.games.aoe;

import org.hsh.games.aoe.infrastructure.configuration.ApplicationBootstrapConfig;

/**
 * Main entry point for the hexagonal architecture version of the game
 * 
 * @author devTASE
 */
public class Game {
    
    public static void main(String[] args) {
        System.out.println("Welcome to Strategic Game");
        
        // Initialize application configuration
        ApplicationBootstrapConfig config = new ApplicationBootstrapConfig();
        
        // Start the console game controller
        config.getConsoleGameController().start();
    }
}
