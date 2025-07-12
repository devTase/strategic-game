package org.hsh.games.aoe.domain.valueobjects;

/**
 * Value object representing a unique player identifier
 * 
 * @author devTASE
 */
public record PlayerId(String value) {
    
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;
    
    public PlayerId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        
        String trimmed = value.trim();
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Player ID must be at least " + MIN_LENGTH + " characters");
        }
        
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Player ID cannot exceed " + MAX_LENGTH + " characters");
        }
        
        value = trimmed;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
