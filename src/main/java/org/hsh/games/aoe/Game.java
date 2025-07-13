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

// Cyber operatives count of my village is inexistent. So do i have unlimited workers ? I should only have like 3 or 5 workers when starting, and then i should have to search for more as i do for normal resources
// Only Resources gathering is working, yet still needs some improvements for UI presentation
// "Construction Guild" name does it refer to normal constructions ?
// What is the 'usecases' package for ?