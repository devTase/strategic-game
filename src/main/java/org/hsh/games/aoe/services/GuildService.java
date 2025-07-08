package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.rebelcell.*;
import org.hsh.games.aoe.entities.ResourceType;

import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service class for managing guild operations including creation, member management,
 * vault operations, and event broadcasting.
 * Uses thread-safe collections for concurrent access.
 * 
 * @author devTASE
 */
public class GuildService {
    
    private final Map<String, Guild> guilds;
    private final Map<String, String> playerGuildMapping; // playerId -> guildId
    
    /**
     * Constructor injection for dependencies
     */
    public GuildService() {
        this.guilds = new ConcurrentHashMap<>();
        this.playerGuildMapping = new ConcurrentHashMap<>();
    }
    
    /**
     * Creates a new guild with the specified founder
     * @param guildName Name of the guild
     * @param founderPlayerId ID of the player founding the guild
     * @param initialVaultCapacity Initial capacity of the guild vault
     * @return The created Guild
     * @throws IllegalArgumentException if parameters are invalid or founder is already in a guild
     */
    public Guild createGuild(String guildName, String founderPlayerId, int initialVaultCapacity) {
        Objects.requireNonNull(guildName, "Guild name cannot be null");
        Objects.requireNonNull(founderPlayerId, "Founder player ID cannot be null");
        
        if (guildName.isBlank()) {
            throw new IllegalArgumentException("Guild name cannot be blank");
        }
        
        if (founderPlayerId.isBlank()) {
            throw new IllegalArgumentException("Founder player ID cannot be blank");
        }
        
        if (initialVaultCapacity <= 0) {
            throw new IllegalArgumentException("Initial vault capacity must be positive");
        }
        
        // Check if founder is already in a guild
        if (playerGuildMapping.containsKey(founderPlayerId)) {
            throw new IllegalArgumentException("Player is already a member of a guild");
        }
        
        // Check if guild name is already taken
        boolean nameExists = guilds.values().stream()
                .anyMatch(guild -> guild.name().equalsIgnoreCase(guildName));
        
        if (nameExists) {
            throw new IllegalArgumentException("Guild name is already taken");
        }
        
        String guildId = generateGuildId();
        Guild newGuild = Guild.createNew(guildId, guildName, founderPlayerId, initialVaultCapacity);
        
        guilds.put(guildId, newGuild);
        playerGuildMapping.put(founderPlayerId, guildId);
        
        broadcastEvent(guildId, String.format("Guild '%s' has been founded by %s", guildName, founderPlayerId));
        
        return newGuild;
    }

    /**
     * Sends a spy from one guild to another and returns a SpyReport after a delay.
     * Success chance is influenced by guild level (average era of members).
     * @param playerId ID of the player sending the spy
     * @param targetGuildId ID of the target guild
     * @return CompletableFuture of SpyReport
     */
    public CompletableFuture<SpyReport> sendSpy(String playerId, String targetGuildId) {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        Objects.requireNonNull(targetGuildId, "Target guild ID cannot be null");

        Guild fromGuild = getPlayerGuild(playerId);
        if (fromGuild == null) {
            throw new IllegalArgumentException("Player is not in a guild");
        }

        Guild targetGuild = getGuildById(targetGuildId);
        if (targetGuild == null) {
            throw new IllegalArgumentException("Target guild not found");
        }

        if (fromGuild.id().equals(targetGuildId)) {
            throw new IllegalArgumentException("A guild cannot spy on itself");
        }

        Random random = ThreadLocalRandom.current();

        // Simulate delayed espionage operation
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(random.nextInt(2000, 4000)); // Random delay between 2-4 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Calculate success chance based on guild level (average era of members)
            double avgGuildLevel = calculateAverageGuildLevel(fromGuild);
            double successChance = Math.min(0.9, 0.3 + (avgGuildLevel * 0.1)); // Base 30%, up to 90%
            boolean success = random.nextDouble() < successChance;

            String reportId = UUID.randomUUID().toString();
            SpyReport report;

            if (success) {
                // Successful spy mission - detailed intelligence
                int targetResourceCount = calculateTotalResources(targetGuild);
                report = SpyReport.createResourceReport(reportId, fromGuild.id(), targetGuildId, targetResourceCount);
                broadcastEvent(fromGuild.id(), String.format("🕵️ Spy mission to %s successful! Intelligence gathered.", targetGuild.name()));
            } else {
                // Failed spy mission - limited or no intelligence
                String content = "🔍 Spy mission partially failed. Limited intelligence gathered.";
                report = SpyReport.createNew(reportId, fromGuild.id(), targetGuildId, content);
                broadcastEvent(fromGuild.id(), String.format("🚨 Spy mission to %s detected! Limited intelligence obtained.", targetGuild.name()));
            }

            return report;
        });
    }

    /**
     * Initiates a black market trade between guilds.
     * Success influenced by guild level and applies fee.
     * @param guildId ID of the guild initiating the trade
     * @param offer ResourceType offered by the guild
     * @param demand ResourceType demanded by the guild
     * @return updated Guild
     */
    public Guild startBlackMarketTrade(String guildId, ResourceType offer, ResourceType demand) {
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(offer, "ResourceType offer cannot be null");
        Objects.requireNonNull(demand, "ResourceType demand cannot be null");

        if (offer.equals(demand)) {
            throw new IllegalArgumentException("Cannot trade the same resource type");
        }

        Guild guild = getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }

        Random random = ThreadLocalRandom.current();
        
        // Calculate trade amounts based on resource values
        int baseOfferAmount = Math.max(10, offer.getPricePerUnit() * 2);
        int baseDemandAmount = Math.max(5, demand.getPricePerUnit());
        
        // Calculate success chance based on guild level
        double avgGuildLevel = calculateAverageGuildLevel(guild);
        double successChance = Math.min(0.95, 0.5 + (avgGuildLevel * 0.05)); // Base 50%, up to 95%
        
        // Black market fee (higher level guilds get better rates)
        double feeRate = Math.max(0.05, 0.15 - (avgGuildLevel * 0.01)); // 15% down to 5%
        
        boolean tradeSuccess = random.nextDouble() < successChance;
        String leaderPlayerId = guild.leaderPlayerId();
        
        if (!tradeSuccess) {
            // Failed trade - lose some resources as "broker fees"
            int lostAmount = Math.min(baseOfferAmount / 4, 5);
            Guild updatedGuild = guild.withdrawResource(offer, lostAmount, leaderPlayerId);
            guilds.put(guildId, updatedGuild);
            
            broadcastEvent(guildId, String.format("💸 Black market trade failed! Lost %d %s in broker fees.", 
                lostAmount, offer.getDescription()));
            
            return updatedGuild;
        }
        
        // Successful trade
        int finalDemandAmount = (int) (baseDemandAmount * (1 - feeRate));
        
        Guild updatedGuild = guild.withdrawResource(offer, baseOfferAmount, leaderPlayerId);
        updatedGuild = updatedGuild.depositResource(demand, finalDemandAmount, leaderPlayerId);
        
        guilds.put(guildId, updatedGuild);
        
        broadcastEvent(guildId, String.format("🛒 Black market trade successful! Traded %d %s for %d %s (%.0f%% fee applied).", 
            baseOfferAmount, offer.getDescription(), finalDemandAmount, demand.getDescription(), feeRate * 100));
        
        return updatedGuild;
    }

    /**
     * Invites a player to join a guild
     * @param guildId ID of the guild
     * @param playerId ID of the player to invite
     * @param inviterPlayerId ID of the player sending the invitation
     * @param rank Rank to assign to the new member
     * @return Updated Guild with the new member
     * @throws IllegalArgumentException if invitation is invalid
     */
    public Guild invitePlayer(String guildId, String playerId, String inviterPlayerId, RebelCellRank rank) {
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        Objects.requireNonNull(inviterPlayerId, "Inviter player ID cannot be null");
        Objects.requireNonNull(rank, "Rank cannot be null");
        
        Guild guild = getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }
        
        // Check if inviter has permissions
        GuildMember inviter = guild.getMember(inviterPlayerId);
        if (inviter == null) {
            throw new IllegalArgumentException("Inviter is not a member of the guild");
        }
        
        if (!inviter.canRecruit()) {
            throw new IllegalArgumentException("Inviter does not have permission to recruit members");
        }
        
        // Check if player is already in a guild
        if (playerGuildMapping.containsKey(playerId)) {
            throw new IllegalArgumentException("Player is already a member of a guild");
        }
        
        Guild updatedGuild = guild.addMember(playerId, rank);
        guilds.put(guildId, updatedGuild);
        playerGuildMapping.put(playerId, guildId);
        
        broadcastEvent(guildId, String.format("Player %s has been invited to the guild by %s", playerId, inviterPlayerId));
        
        return updatedGuild;
    }
    
    /**
     * Changes the rank of a guild member
     * @param guildId ID of the guild
     * @param playerId ID of the player whose rank to change
     * @param newRank New rank to assign
     * @param authorityPlayerId ID of the player making the change
     * @return Updated Guild with the changed rank
     * @throws IllegalArgumentException if rank change is invalid
     */
    public Guild changeRank(String guildId, String playerId, RebelCellRank newRank, String authorityPlayerId) {
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        Objects.requireNonNull(newRank, "New rank cannot be null");
        Objects.requireNonNull(authorityPlayerId, "Authority player ID cannot be null");
        
        Guild guild = getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }
        
        // Check if authority has permissions
        GuildMember authority = guild.getMember(authorityPlayerId);
        if (authority == null) {
            throw new IllegalArgumentException("Authority player is not a member of the guild");
        }
        
        if (!authority.hasAdminPrivileges()) {
            throw new IllegalArgumentException("Authority player does not have permission to change ranks");
        }
        
        // Check if target player exists in guild
        GuildMember targetMember = guild.getMember(playerId);
        if (targetMember == null) {
            throw new IllegalArgumentException("Target player is not a member of the guild");
        }
        
        // Cannot change leader rank
        if (targetMember.isLeader()) {
            throw new IllegalArgumentException("Cannot change the rank of the guild leader");
        }
        
        Guild updatedGuild = guild.promoteMember(playerId, newRank);
        guilds.put(guildId, updatedGuild);
        
        broadcastEvent(guildId, String.format("Player %s rank changed to %s by %s", 
                playerId, newRank.getDisplayName(), authorityPlayerId));
        
        return updatedGuild;
    }
    
    /**
     * Deposits resources to the guild vault
     * Uses ResourceType validation and transfer multipliers
     * @param guildId ID of the guild
     * @param resourceType Type of resource to deposit
     * @param amount Amount to deposit
     * @param depositorPlayerId ID of the player making the deposit
     * @return Updated Guild with resources deposited to vault
     * @throws IllegalArgumentException if deposit is invalid
     */
    public Guild depositToVault(String guildId, ResourceType resourceType, int amount, String depositorPlayerId) {
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(resourceType, "Resource type cannot be null");
        Objects.requireNonNull(depositorPlayerId, "Depositor player ID cannot be null");
        
        // Validate the transfer using ResourceType utilities
        ResourceType.validateGuildVaultTransfer(resourceType, amount);
        
        // Check if resource can be stored in guild vault
        if (!resourceType.isGuildVaultStorable()) {
            throw new IllegalArgumentException("Resource type " + resourceType.getDescription() + " cannot be stored in guild vaults");
        }
        
        Guild guild = getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }
        
        // Check if depositor is a member
        if (!guild.hasMember(depositorPlayerId)) {
            throw new IllegalArgumentException("Only guild members can deposit to the vault");
        }
        
        // Apply transfer multiplier
        double transferMultiplier = resourceType.getGuildVaultTransferMultiplier();
        int actualAmount = (int)(amount * transferMultiplier);
        
        Guild updatedGuild = guild.depositResource(resourceType, actualAmount, depositorPlayerId);
        guilds.put(guildId, updatedGuild);
        
        String message = (transferMultiplier == 1.0) ?
            String.format("Player %s deposited %d %s to the guild vault", 
                depositorPlayerId, actualAmount, resourceType.getDescription()) :
            String.format("Player %s deposited %d %s to the guild vault (%.0f%% transfer efficiency)", 
                depositorPlayerId, actualAmount, resourceType.getDescription(), transferMultiplier * 100);
        
        broadcastEvent(guildId, message);
        
        return updatedGuild;
    }

    /**
     * Withdraws resources from the guild vault
     * Uses ResourceType validation and transfer multipliers
     * @param guildId ID of the guild
     * @param resourceType Type of resource to withdraw
     * @param amount Amount to withdraw
     * @param withdrawerPlayerId ID of the player making the withdrawal
     * @return Updated Guild with resources withdrawn from vault
     * @throws IllegalArgumentException if withdrawal is invalid
     */
    public Guild withdrawFromVault(String guildId, ResourceType resourceType, int amount, String withdrawerPlayerId) {
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(resourceType, "Resource type cannot be null");
        Objects.requireNonNull(withdrawerPlayerId, "Withdrawer player ID cannot be null");
        
        // Validate the transfer using ResourceType utilities
        ResourceType.validateGuildVaultTransfer(resourceType, amount);
        
        Guild guild = getGuildById(guildId);
        if (guild == null) {
            throw new IllegalArgumentException("Guild not found");
        }
        
        // Check if withdrawer is a member with proper permissions
        GuildMember withdrawer = guild.getMember(withdrawerPlayerId);
        if (withdrawer == null) {
            throw new IllegalArgumentException("Only guild members can withdraw from the vault");
        }
        
        if (!withdrawer.hasAdminPrivileges()) {
            throw new IllegalArgumentException("Only officers and leaders can withdraw from the vault");
        }
        
        // Apply transfer multiplier
        double transferMultiplier = resourceType.getGuildVaultTransferMultiplier();
        int actualWithdrawn = amount;
        int playerReceives = (int)(amount * transferMultiplier);
        
        Guild updatedGuild = guild.withdrawResource(resourceType, actualWithdrawn, withdrawerPlayerId);
        guilds.put(guildId, updatedGuild);
        
        String message = (transferMultiplier == 1.0) ?
            String.format("Player %s withdrew %d %s from the guild vault", 
                withdrawerPlayerId, playerReceives, resourceType.getDescription()) :
            String.format("Player %s withdrew %d %s from the guild vault (%.0f%% transfer efficiency)", 
                withdrawerPlayerId, playerReceives, resourceType.getDescription(), transferMultiplier * 100);
        
        broadcastEvent(guildId, message);
        
        return updatedGuild;
    }
    
    /**
     * Broadcasts an event to all guild members
     * @param guildId ID of the guild
     * @param message Message to broadcast
     */
    public void broadcastEvent(String guildId, String message) {
        Objects.requireNonNull(guildId, "Guild ID cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");
        
        if (message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be blank");
        }
        
        Guild guild = getGuildById(guildId);
        if (guild != null) {
            // In a real implementation, this would send notifications to online players
            // For now, we'll just log the event
            System.out.printf("[GUILD %s] %s%n", guild.name(), message);
            
            // You could also add the event to a guild notification system
            // or send push notifications to guild members
        }
    }
    
    /**
     * Removes a player from their guild
     * @param playerId ID of the player to remove
     * @param removerPlayerId ID of the player performing the removal (can be same as playerId for leaving)
     * @return Updated Guild without the player, or null if player wasn't in a guild
     * @throws IllegalArgumentException if removal is invalid
     */
    public Guild removePlayerFromGuild(String playerId, String removerPlayerId) {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        Objects.requireNonNull(removerPlayerId, "Remover player ID cannot be null");
        
        String guildId = playerGuildMapping.get(playerId);
        if (guildId == null) {
            return null; // Player not in a guild
        }
        
        Guild guild = getGuildById(guildId);
        if (guild == null) {
            playerGuildMapping.remove(playerId); // Clean up orphaned mapping
            return null;
        }
        
        // Check permissions if it's not self-removal
        if (!playerId.equals(removerPlayerId)) {
            GuildMember remover = guild.getMember(removerPlayerId);
            if (remover == null || !remover.hasAdminPrivileges()) {
                throw new IllegalArgumentException("Insufficient permissions to remove player from guild");
            }
        }
        
        Guild updatedGuild = guild.removeMember(playerId);
        guilds.put(guildId, updatedGuild);
        playerGuildMapping.remove(playerId);
        
        String action = playerId.equals(removerPlayerId) ? "left" : "was removed from";
        broadcastEvent(guildId, String.format("Player %s %s the guild", playerId, action));
        
        return updatedGuild;
    }
    
    /**
     * Gets a guild by its ID
     * @param guildId ID of the guild
     * @return Guild instance or null if not found
     */
    public Guild getGuildById(String guildId) {
        return guilds.get(guildId);
    }
    
    /**
     * Gets the guild that a player belongs to
     * @param playerId ID of the player
     * @return Guild instance or null if player is not in a guild
     */
    public Guild getPlayerGuild(String playerId) {
        String guildId = playerGuildMapping.get(playerId);
        return guildId != null ? getGuildById(guildId) : null;
    }
    
    /**
     * Gets all guilds
     * @return Set of all guild IDs
     */
    public Set<String> getAllGuildIds() {
        return guilds.keySet();
    }
    
    /**
     * Checks if a player is in a guild
     * @param playerId ID of the player
     * @return true if player is in a guild
     */
    public boolean isPlayerInGuild(String playerId) {
        return playerGuildMapping.containsKey(playerId);
    }
    
    /**
     * Gets the total number of guilds
     * @return Total guild count
     */
    public int getGuildCount() {
        return guilds.size();
    }
    
    /**
     * Calculates the average guild level based on members' era levels
     * @param guild The guild to calculate the level for
     * @return Average guild level (1-9)
     */
    private double calculateAverageGuildLevel(Guild guild) {
        if (guild.members().isEmpty()) {
            return 1.0;
        }
        
        Random random = ThreadLocalRandom.current();
        
        // For now, simulate member eras since PlayerService only manages one player
        // In a real implementation, you'd have a PlayerRepository or similar
        double totalLevels = guild.members().stream()
            .mapToDouble(member -> {
                // Simulate era levels - newer guilds tend to have lower era members
                // This is a placeholder logic that could be replaced with actual player data
                int simulatedLevel = Math.max(1, random.nextInt(1, 5)); // Levels 1-4 for simulation
                return simulatedLevel;
            })
            .sum();
            
        return totalLevels / guild.members().size();
    }
    
    /**
     * Calculates total resources in a guild's vault
     * @param guild The guild to calculate resources for
     * @return Total resource count
     */
    private int calculateTotalResources(Guild guild) {
        // This is a placeholder - in a real implementation you'd access the vault
        // and sum all resource amounts
        return 500 + (guild.members().size() * 100); // Rough estimation
    }
    
    /**
     * Generates a unique guild ID
     * @return Generated guild ID
     */
    private String generateGuildId() {
        return "guild_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
