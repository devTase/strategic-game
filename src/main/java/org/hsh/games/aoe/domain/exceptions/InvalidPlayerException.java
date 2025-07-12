package org.hsh.games.aoe.domain.exceptions;

/**
 * Exception thrown when player operations are invalid
 * 
 * @author devTASE
 */
public class InvalidPlayerException extends DomainException {
    
    public InvalidPlayerException(String message) {
        super(message);
    }
    
    public InvalidPlayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
