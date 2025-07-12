package org.hsh.games.aoe.domain.exceptions;

/**
 * Exception thrown when there are insufficient resources for an operation
 * 
 * @author devTASE
 */
public class InsufficientResourcesException extends DomainException {
    
    public InsufficientResourcesException(String message) {
        super(message);
    }
    
    public InsufficientResourcesException(String message, Throwable cause) {
        super(message, cause);
    }
}
