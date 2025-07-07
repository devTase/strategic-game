package org.hsh.games.aoe.entities.guild;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Record representing a guild with all its components and members.
 * This is an immutable data structure that contains all guild information.
 * 
 * @param id             Unique identifier for the guild
 * @param name           Display name of the guild
 * @param leaderPlayerId ID of the player who leads the guild
 * @param members        Set of guild members
 * @param vault          Guild's resource storage
 * @param territories    Set of territory IDs controlled by the guild
 * @param eventLog       List of guild events in chronological order
 * 
 * @author devTASE
 */
public record Guild(
        String id,
        String name,
        String leaderPlayerId,
        Set<GuildMember> members,
        GuildVault vault,
        Set<String> territories,
        List<GuildEvent> eventLog
) {
    
    /**
     * Constructor with validation
     */
    public Guild {
        Objects.requireNonNull(id, "Guild ID cannot be null");
        Objects.requireNonNull(name, "Guild name cannot be null");
        Objects.requireNonNull(leaderPlayerId, "Leader player ID cannot be null");
        Objects.requireNonNull(members, "Members set cannot be null");
        Objects.requireNonNull(vault, "Guild vault cannot be null");
        Objects.requireNonNull(territories, "Territories set cannot be null");
        Objects.requireNonNull(eventLog, "Event log cannot be null");
        
        if (id.isBlank()) {
            throw new IllegalArgumentException("Guild ID cannot be blank");
        }
        
        if (name.isBlank()) {
            throw new IllegalArgumentException("Guild name cannot be blank");
        }
        
        if (leaderPlayerId.isBlank()) {
            throw new IllegalArgumentException("Leader player ID cannot be blank");
        }
        
        // Create defensive copies
        members = new HashSet<>(members);
        territories = new HashSet<>(territories);
        eventLog = new ArrayList<>(eventLog);
        
        // Validate that leader is actually a member with LEADER rank
        boolean hasLeader = members.stream()
                .anyMatch(member -> member.playerId().equals(leaderPlayerId) && member.rank() == GuildRank.LEADER);
        
        if (!members.isEmpty() && !hasLeader) {
            throw new IllegalArgumentException("Leader must be a member with LEADER rank");
        }
        
        // Validate that there's only one leader
        long leaderCount = members.stream()
                .filter(member -> member.rank() == GuildRank.LEADER)
                .count();
        
        if (leaderCount > 1) {
            throw new IllegalArgumentException("Guild can only have one leader");
        }
        
        // Validate territory IDs
        for (String territoryId : territories) {
            if (territoryId == null || territoryId.isBlank()) {
                throw new IllegalArgumentException("Territory ID cannot be null or blank");
            }
        }
    }
    
    /**
     * Creates a new guild with a founder as the leader
     * @param id Guild ID
     * @param name Guild name
     * @param founderPlayerId ID of the founding player
     * @param initialVaultCapacity Initial vault capacity
     * @return A new Guild instance
     */
    public static Guild createNew(String id, String name, String founderPlayerId, int initialVaultCapacity) {
        GuildMember founder = GuildMember.createNew(founderPlayerId, GuildRank.LEADER);
        Set<GuildMember> members = new HashSet<>();
        members.add(founder);
        
        GuildVault vault = GuildVault.createEmpty(initialVaultCapacity);
        
        List<GuildEvent> eventLog = new ArrayList<>();
        eventLog.add(GuildEvent.createGuildCreated(id, founderPlayerId));
        
        return new Guild(id, name, founderPlayerId, members, vault, new HashSet<>(), eventLog);
    }
    
    /**
     * Adds a new member to the guild
     * @param playerId The player ID to add
     * @param rank The rank to assign to the new member
     * @return A new Guild instance with the added member
     * @throws IllegalArgumentException if player is already a member or invalid rank
     */
    public Guild addMember(String playerId, GuildRank rank) {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        Objects.requireNonNull(rank, "Rank cannot be null");
        
        if (playerId.isBlank()) {
            throw new IllegalArgumentException("Player ID cannot be blank");
        }
        
        if (rank == GuildRank.LEADER) {
            throw new IllegalArgumentException("Cannot add another leader to the guild");
        }
        
        // Check if player is already a member
        if (hasMember(playerId)) {
            throw new IllegalArgumentException("Player is already a member of the guild");
        }
        
        GuildMember newMember = GuildMember.createNew(playerId, rank);
        Set<GuildMember> newMembers = new HashSet<>(members);
        newMembers.add(newMember);
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createMemberJoined(id, playerId));
        
        return new Guild(id, name, leaderPlayerId, newMembers, vault, territories, newEventLog);
    }
    
    /**
     * Adds a new recruit to the guild
     * @param playerId The player ID to add as recruit
     * @return A new Guild instance with the added recruit
     */
    public Guild addRecruit(String playerId) {
        return addMember(playerId, GuildRank.RECRUIT);
    }
    
    /**
     * Removes a member from the guild
     * @param playerId The player ID to remove
     * @return A new Guild instance with the member removed
     * @throws IllegalArgumentException if player is not a member or is the leader
     */
    public Guild removeMember(String playerId) {
        if (playerId.equals(leaderPlayerId)) {
            throw new IllegalArgumentException("Cannot remove the guild leader");
        }
        
        if (!hasMember(playerId)) {
            throw new IllegalArgumentException("Player is not a member of the guild");
        }
        
        Set<GuildMember> newMembers = members.stream()
                .filter(member -> !member.playerId().equals(playerId))
                .collect(Collectors.toSet());
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createMemberLeft(id, playerId));
        
        return new Guild(id, name, leaderPlayerId, newMembers, vault, territories, newEventLog);
    }
    
    /**
     * Promotes a member to a higher rank
     * @param playerId The player ID to promote
     * @param newRank The new rank to assign
     * @return A new Guild instance with the promoted member
     * @throws IllegalArgumentException if invalid promotion
     */
    public Guild promoteMember(String playerId, GuildRank newRank) {
        Objects.requireNonNull(newRank, "New rank cannot be null");
        
        if (newRank == GuildRank.LEADER) {
            throw new IllegalArgumentException("Cannot promote to leader rank");
        }
        
        GuildMember currentMember = getMember(playerId);
        if (currentMember == null) {
            throw new IllegalArgumentException("Player is not a member of the guild");
        }
        
        Set<GuildMember> newMembers = members.stream()
                .map(member -> member.playerId().equals(playerId) ? member.withRank(newRank) : member)
                .collect(Collectors.toSet());
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createMemberPromoted(id, playerId, newRank));
        
        return new Guild(id, name, leaderPlayerId, newMembers, vault, territories, newEventLog);
    }
    
    /**
     * Deposits resources into the guild vault
     * @param resourceType The type of resource to deposit
     * @param amount The amount to deposit
     * @param depositorPlayerId The player making the deposit
     * @return A new Guild instance with updated vault
     */
    public Guild depositResource(org.hsh.games.aoe.entities.ResourceType resourceType, int amount, String depositorPlayerId) {
        if (!hasMember(depositorPlayerId)) {
            throw new IllegalArgumentException("Only guild members can deposit resources");
        }
        
        GuildVault newVault = vault.depositResource(resourceType, amount);
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createResourceDeposited(id, depositorPlayerId, resourceType, amount));
        
        return new Guild(id, name, leaderPlayerId, members, newVault, territories, newEventLog);
    }
    
    /**
     * Withdraws resources from the guild vault
     * @param resourceType The type of resource to withdraw
     * @param amount The amount to withdraw
     * @param withdrawerPlayerId The player making the withdrawal
     * @return A new Guild instance with updated vault
     * @throws IllegalArgumentException if player lacks permission or insufficient resources
     */
    public Guild withdrawResource(org.hsh.games.aoe.entities.ResourceType resourceType, int amount, String withdrawerPlayerId) {
        GuildMember withdrawer = getMember(withdrawerPlayerId);
        if (withdrawer == null) {
            throw new IllegalArgumentException("Only guild members can withdraw resources");
        }
        
        if (!withdrawer.hasAdminPrivileges()) {
            throw new IllegalArgumentException("Only officers and leaders can withdraw resources");
        }
        
        GuildVault newVault = vault.withdrawResource(resourceType, amount);
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createResourceWithdrawn(id, withdrawerPlayerId, resourceType, amount));
        
        return new Guild(id, name, leaderPlayerId, members, newVault, territories, newEventLog);
    }
    
    /**
     * Claims a territory for the guild
     * @param territoryId The territory ID to claim
     * @param claimerPlayerId The player claiming the territory
     * @return A new Guild instance with the claimed territory
     */
    public Guild claimTerritory(String territoryId, String claimerPlayerId) {
        Objects.requireNonNull(territoryId, "Territory ID cannot be null");
        
        if (territoryId.isBlank()) {
            throw new IllegalArgumentException("Territory ID cannot be blank");
        }
        
        if (!hasMember(claimerPlayerId)) {
            throw new IllegalArgumentException("Only guild members can claim territories");
        }
        
        if (territories.contains(territoryId)) {
            throw new IllegalArgumentException("Guild already controls this territory");
        }
        
        Set<String> newTerritories = new HashSet<>(territories);
        newTerritories.add(territoryId);
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createTerritoryConquered(id, claimerPlayerId, territoryId));
        
        return new Guild(id, name, leaderPlayerId, members, vault, newTerritories, newEventLog);
    }
    
    /**
     * Releases a territory from guild control
     * @param territoryId The territory ID to release
     * @return A new Guild instance without the territory
     */
    public Guild releaseTerritory(String territoryId) {
        if (!territories.contains(territoryId)) {
            throw new IllegalArgumentException("Guild does not control this territory");
        }
        
        Set<String> newTerritories = new HashSet<>(territories);
        newTerritories.remove(territoryId);
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createTerritoryLost(id, territoryId));
        
        return new Guild(id, name, leaderPlayerId, members, vault, newTerritories, newEventLog);
    }
    
    /**
     * Adds a spy report to the guild's intelligence
     * @param spyReport The spy report to add
     * @return A new Guild instance with the spy report logged
     */
    public Guild addSpyReport(SpyReport spyReport) {
        if (!spyReport.isFrom(id)) {
            throw new IllegalArgumentException("Spy report must be from this guild");
        }
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createSpyReportReceived(id, spyReport.targetGuildId(), spyReport.getIntelligenceType()));
        
        return new Guild(id, name, leaderPlayerId, members, vault, territories, newEventLog);
    }
    
    /**
     * Updates the guild vault capacity
     * @param additionalCapacity Additional capacity to add
     * @return A new Guild instance with increased vault capacity
     */
    public Guild upgradeVault(int additionalCapacity) {
        GuildVault newVault = vault.increaseCapacity(additionalCapacity);
        
        List<GuildEvent> newEventLog = new ArrayList<>(eventLog);
        newEventLog.add(GuildEvent.createVaultUpgraded(id, additionalCapacity));
        
        return new Guild(id, name, leaderPlayerId, members, newVault, territories, newEventLog);
    }
    
    // Query methods
    
    /**
     * Checks if a player is a member of the guild
     * @param playerId The player ID to check
     * @return true if the player is a member
     */
    public boolean hasMember(String playerId) {
        return members.stream().anyMatch(member -> member.playerId().equals(playerId));
    }
    
    /**
     * Gets a member by player ID
     * @param playerId The player ID to find
     * @return The GuildMember or null if not found
     */
    public GuildMember getMember(String playerId) {
        return members.stream()
                .filter(member -> member.playerId().equals(playerId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Gets the guild leader
     * @return The leader GuildMember
     */
    public GuildMember getLeader() {
        return getMember(leaderPlayerId);
    }
    
    /**
     * Gets all members with a specific rank
     * @param rank The rank to filter by
     * @return Set of members with the specified rank
     */
    public Set<GuildMember> getMembersByRank(GuildRank rank) {
        return members.stream()
                .filter(member -> member.rank() == rank)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets the number of members
     * @return The member count
     */
    public int getMemberCount() {
        return members.size();
    }
    
    /**
     * Gets the number of territories controlled
     * @return The territory count
     */
    public int getTerritoryCount() {
        return territories.size();
    }
    
    /**
     * Checks if the guild controls a specific territory
     * @param territoryId The territory ID to check
     * @return true if the guild controls the territory
     */
    public boolean controlsTerritory(String territoryId) {
        return territories.contains(territoryId);
    }
    
    /**
     * Gets recent events from the event log
     * @param maxEvents Maximum number of events to return
     * @return List of recent events
     */
    public List<GuildEvent> getRecentEvents(int maxEvents) {
        if (maxEvents <= 0) {
            return new ArrayList<>();
        }
        
        int startIndex = Math.max(0, eventLog.size() - maxEvents);
        return new ArrayList<>(eventLog.subList(startIndex, eventLog.size()));
    }
    
    /**
     * Gets a formatted string representation of the guild
     * @return Formatted string with guild details
     */
    public String getFormattedInfo() {
        return String.format("🏛️ Guild: %s [ID: %s]\n" +
                           "👑 Leader: %s\n" +
                           "👥 Members: %d | 🏰 Territories: %d\n" +
                           "💰 Vault: %d/%d (%.1f%% full)",
                           name, id, leaderPlayerId,
                           getMemberCount(), getTerritoryCount(),
                           vault.getTotalStoredResources(), vault.capacity(), vault.getCapacityUtilization());
    }
    
    /**
     * Gets a summary string for display purposes
     * @return Short summary of the guild
     */
    public String getSummary() {
        return String.format("%s (%d members, %d territories)", 
                           name, getMemberCount(), getTerritoryCount());
    }
    
    /**
     * Returns defensive copies of the collections
     */
    public Set<GuildMember> getMembersCopy() {
        return new HashSet<>(members);
    }
    
    public Set<String> getTerritoriesCopy() {
        return new HashSet<>(territories);
    }
    
    public List<GuildEvent> getEventLogCopy() {
        return new ArrayList<>(eventLog);
    }
}
