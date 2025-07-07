package org.hsh.games.aoe.entities.guild;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Record representing a guild member with their basic information.
 * This is an immutable data structure that contains member details.
 * 
 * @param playerId   The unique identifier of the player
 * @param rank       The rank/position of the member within the guild
 * @param joinDate   The date and time when the member joined the guild
 * 
 * @author devTASE
 */
public record GuildMember(
        String playerId,
        GuildRank rank,
        LocalDateTime joinDate
) {
    
    /**
     * Constructor with validation
     */
    public GuildMember {
        Objects.requireNonNull(playerId, "Player ID cannot be null");
        Objects.requireNonNull(rank, "Guild rank cannot be null");
        Objects.requireNonNull(joinDate, "Join date cannot be null");
        
        if (playerId.isBlank()) {
            throw new IllegalArgumentException("Player ID cannot be blank");
        }
    }
    
    /**
     * Creates a new GuildMember with the current timestamp as join date
     * @param playerId The player ID
     * @param rank The guild rank
     * @return A new GuildMember instance
     */
    public static GuildMember createNew(String playerId, GuildRank rank) {
        return new GuildMember(playerId, rank, LocalDateTime.now());
    }
    
    /**
     * Creates a new GuildMember with RECRUIT rank and current timestamp
     * @param playerId The player ID
     * @return A new GuildMember instance with RECRUIT rank
     */
    public static GuildMember createNewRecruit(String playerId) {
        return new GuildMember(playerId, GuildRank.RECRUIT, LocalDateTime.now());
    }
    
    /**
     * Creates a copy of this member with a new rank
     * @param newRank The new rank to assign
     * @return A new GuildMember instance with the updated rank
     */
    public GuildMember withRank(GuildRank newRank) {
        return new GuildMember(playerId, newRank, joinDate);
    }
    
    /**
     * Checks if this member has administrative privileges
     * @return true if the member's rank has admin privileges
     */
    public boolean hasAdminPrivileges() {
        return rank.hasAdminPrivileges();
    }
    
    /**
     * Checks if this member can recruit new members
     * @return true if the member's rank allows recruiting
     */
    public boolean canRecruit() {
        return rank.canRecruit();
    }
    
    /**
     * Checks if this member is a leader
     * @return true if the member's rank is LEADER
     */
    public boolean isLeader() {
        return rank == GuildRank.LEADER;
    }
    
    /**
     * Checks if this member is a spy
     * @return true if the member's rank is SPY
     */
    public boolean isSpy() {
        return rank == GuildRank.SPY;
    }
    
    /**
     * Gets the display name with emoji for this member
     * @return The rank display name with emoji
     */
    public String getDisplayNameWithEmoji() {
        return rank.getDisplayNameWithEmoji();
    }
    
    /**
     * Gets a formatted string representation of the member
     * @return Formatted string with player ID and rank
     */
    public String getFormattedInfo() {
        return String.format("%s [%s] - Joined: %s", playerId, rank.getDisplayNameWithEmoji(), joinDate.toLocalDate());
    }
}
