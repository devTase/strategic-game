package org.hsh.games.aoe.application.ports.outbound;

/**
 * Outbound port for notification operations
 * 
 * @author devTASE
 */
public interface NotificationPort {
    
    /**
     * Sends a success notification
     */
    void notifySuccess(String message);
    
    /**
     * Sends an error notification
     */
    void notifyError(String message);
    
    /**
     * Sends a warning notification
     */
    void notifyWarning(String message);
    
    /**
     * Sends an info notification
     */
    void notifyInfo(String message);
}
