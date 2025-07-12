package org.hsh.games.aoe.domain.exceptions;

/**
 * Base exception for domain-related errors
 * 
 * @author devTASE
 */
public abstract class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
