package org.hsh.games.aoe.infrastructure.adapters.outbound.notification;

import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.infrastructure.adapters.inbound.console.ConsoleDisplayUtils;

/**
 * Console-based implementation of NotificationPort
 * 
 * @author devTASE
 */
public class ConsoleNotificationAdapter implements NotificationPort {
    
    @Override
    public void notifySuccess(String message) {
        ConsoleDisplayUtils.printSuccessMessage(message);
    }
    
    @Override
    public void notifyError(String message) {
        ConsoleDisplayUtils.printErrorMessage(message);
    }
    
    @Override
    public void notifyWarning(String message) {
        ConsoleDisplayUtils.printWarningMessage(message);
    }
    
    @Override
    public void notifyInfo(String message) {
        ConsoleDisplayUtils.printInfoMessage(message);
    }
}
