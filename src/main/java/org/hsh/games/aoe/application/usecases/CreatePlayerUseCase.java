package org.hsh.games.aoe.application.usecases;

import org.hsh.games.aoe.application.dto.PlayerDTO;
import org.hsh.games.aoe.application.ports.inbound.PlayerManagementPort;
import org.hsh.games.aoe.application.ports.outbound.PlayerRepositoryPort;
import org.hsh.games.aoe.application.ports.outbound.NotificationPort;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.valueobjects.PlayerId;
import org.hsh.games.aoe.domain.exceptions.InvalidPlayerException;

/**
 * Use case for creating a new player
 * 
 * @author devTASE
 */
public class CreatePlayerUseCase implements PlayerManagementPort {
    
    private final PlayerRepositoryPort playerRepository;
    private final NotificationPort notificationPort;
    
    public CreatePlayerUseCase(PlayerRepositoryPort playerRepository, NotificationPort notificationPort) {
        this.playerRepository = playerRepository;
        this.notificationPort = notificationPort;
    }
    
    @Override
    public PlayerDTO createPlayer(String farmName) {
        try {
            // Check if player with this name already exists
            // Note: In a real implementation, you'd check by name, not PlayerId
            
            Player player = new Player(farmName);
            PlayerId playerId = player.getPlayerId();
            
            if (playerRepository.exists(playerId)) {
                throw new InvalidPlayerException("Player with name '" + farmName + "' already exists");
            }
            playerRepository.save(player);
            
            notificationPort.notifySuccess("Player '" + farmName + "' created successfully!");
            
            return new PlayerDTO(
                player.getPlayerId().value(),
                player.getFarmName(),
                player.getTechPhase(),
                player.getGuildId(),
                player.isInGuild()
            );
            
        } catch (Exception e) {
            notificationPort.notifyError("Failed to create player: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public PlayerDTO getPlayer(PlayerId playerId) {
        return playerRepository.findById(playerId)
            .map(player -> new PlayerDTO(
                player.getPlayerId().value(),
                player.getFarmName(),
                player.getTechPhase(),
                player.getGuildId(),
                player.isInGuild()
            ))
            .orElseThrow(() -> new InvalidPlayerException("Player not found: " + playerId));
    }
    
    @Override
    public java.util.List<org.hsh.games.aoe.application.dto.ResourceDTO> getPlayerResources(PlayerId playerId) {
        // This will be implemented in ResourceManagementUseCase
        return java.util.Collections.emptyList();
    }
    
    @Override
    public void advancePlayerTechPhase(PlayerId playerId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new InvalidPlayerException("Player not found: " + playerId));
        
        if (!player.canAdvanceToNextPhase()) {
            notificationPort.notifyWarning("Cannot advance beyond maximum tech phase");
            return;
        }
        
        player.advanceToNextPhase();
        playerRepository.save(player);
        
        notificationPort.notifySuccess("Advanced to " + player.getTechPhase().getPhaseName());
    }
    
    @Override
    public boolean canAdvanceToNextPhase(PlayerId playerId) {
        return playerRepository.findById(playerId)
            .map(Player::canAdvanceToNextPhase)
            .orElse(false);
    }
    
    @Override
    public void joinGuild(PlayerId playerId, String guildId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new InvalidPlayerException("Player not found: " + playerId));
        
        player.joinGuild(guildId);
        playerRepository.save(player);
        
        notificationPort.notifySuccess("Joined guild: " + guildId);
    }
    
    @Override
    public void leaveGuild(PlayerId playerId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new InvalidPlayerException("Player not found: " + playerId));
        
        player.leaveGuild();
        playerRepository.save(player);
        
        notificationPort.notifySuccess("Left guild successfully");
    }
}
