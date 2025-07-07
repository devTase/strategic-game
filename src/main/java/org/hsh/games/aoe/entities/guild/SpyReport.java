package org.hsh.games.aoe.entities.guild;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Record representing a spy report containing intelligence gathered about other guilds.
 * This is an immutable data structure that contains espionage information.
 * 
 * @param id           Unique identifier for the spy report
 * @param fromGuildId  ID of the guild that generated this report
 * @param targetGuildId ID of the guild being spied upon
 * @param content      The intelligence information gathered
 * @param createdAt    Timestamp when the report was created
 * 
 * @author devTASE
 */
public record SpyReport(
        String id,
        String fromGuildId,
        String targetGuildId,
        String content,
        LocalDateTime createdAt
) {
    
    /**
     * Constructor with validation
     */
    public SpyReport {
        Objects.requireNonNull(id, "Spy report ID cannot be null");
        Objects.requireNonNull(fromGuildId, "From guild ID cannot be null");
        Objects.requireNonNull(targetGuildId, "Target guild ID cannot be null");
        Objects.requireNonNull(content, "Report content cannot be null");
        Objects.requireNonNull(createdAt, "Created timestamp cannot be null");
        
        if (id.isBlank()) {
            throw new IllegalArgumentException("Spy report ID cannot be blank");
        }
        
        if (fromGuildId.isBlank()) {
            throw new IllegalArgumentException("From guild ID cannot be blank");
        }
        
        if (targetGuildId.isBlank()) {
            throw new IllegalArgumentException("Target guild ID cannot be blank");
        }
        
        if (content.isBlank()) {
            throw new IllegalArgumentException("Report content cannot be blank");
        }
        
        if (fromGuildId.equals(targetGuildId)) {
            throw new IllegalArgumentException("A guild cannot spy on itself");
        }
    }
    
    /**
     * Creates a new spy report with the current timestamp
     * @param id Unique identifier for the report
     * @param fromGuildId ID of the spying guild
     * @param targetGuildId ID of the target guild
     * @param content Intelligence content
     * @return A new SpyReport instance
     */
    public static SpyReport createNew(String id, String fromGuildId, String targetGuildId, String content) {
        return new SpyReport(id, fromGuildId, targetGuildId, content, LocalDateTime.now());
    }
    
    /**
     * Creates a resource intelligence report
     * @param id Unique identifier for the report
     * @param fromGuildId ID of the spying guild
     * @param targetGuildId ID of the target guild
     * @param resourceCount Estimated resource count of the target guild
     * @return A new SpyReport instance with resource intelligence
     */
    public static SpyReport createResourceReport(String id, String fromGuildId, String targetGuildId, int resourceCount) {
        String content = String.format("📊 Resource Intelligence: Target guild has approximately %d total resources in their vault.", resourceCount);
        return createNew(id, fromGuildId, targetGuildId, content);
    }
    
    /**
     * Creates a member intelligence report
     * @param id Unique identifier for the report
     * @param fromGuildId ID of the spying guild
     * @param targetGuildId ID of the target guild
     * @param memberCount Number of members in the target guild
     * @param hasActiveLeader Whether the target guild has an active leader
     * @return A new SpyReport instance with member intelligence
     */
    public static SpyReport createMemberReport(String id, String fromGuildId, String targetGuildId, int memberCount, boolean hasActiveLeader) {
        String leaderStatus = hasActiveLeader ? "with an active leader" : "without active leadership";
        String content = String.format("👥 Member Intelligence: Target guild has %d members %s.", memberCount, leaderStatus);
        return createNew(id, fromGuildId, targetGuildId, content);
    }
    
    /**
     * Creates a territory intelligence report
     * @param id Unique identifier for the report
     * @param fromGuildId ID of the spying guild
     * @param targetGuildId ID of the target guild
     * @param territoryCount Number of territories controlled by the target guild
     * @return A new SpyReport instance with territory intelligence
     */
    public static SpyReport createTerritoryReport(String id, String fromGuildId, String targetGuildId, int territoryCount) {
        String content = String.format("🏰 Territory Intelligence: Target guild controls %d territories.", territoryCount);
        return createNew(id, fromGuildId, targetGuildId, content);
    }
    
    /**
     * Creates a mission activity report
     * @param id Unique identifier for the report
     * @param fromGuildId ID of the spying guild
     * @param targetGuildId ID of the target guild
     * @param activeMissions Number of active missions
     * @param recentActivity Description of recent mission activity
     * @return A new SpyReport instance with mission intelligence
     */
    public static SpyReport createMissionReport(String id, String fromGuildId, String targetGuildId, int activeMissions, String recentActivity) {
        String content = String.format("⚔️ Mission Intelligence: Target guild has %d active missions. Recent activity: %s", activeMissions, recentActivity);
        return createNew(id, fromGuildId, targetGuildId, content);
    }
    
    /**
     * Creates a copy of this report with updated content
     * @param newContent The new content for the report
     * @return A new SpyReport instance with updated content
     */
    public SpyReport withContent(String newContent) {
        return new SpyReport(id, fromGuildId, targetGuildId, newContent, createdAt);
    }
    
    /**
     * Checks if this report is about a specific guild
     * @param guildId The guild ID to check
     * @return true if this report targets the specified guild
     */
    public boolean isAbout(String guildId) {
        return Objects.equals(targetGuildId, guildId);
    }
    
    /**
     * Checks if this report was created by a specific guild
     * @param guildId The guild ID to check
     * @return true if this report was created by the specified guild
     */
    public boolean isFrom(String guildId) {
        return Objects.equals(fromGuildId, guildId);
    }
    
    /**
     * Checks if this report is recent (within the last 24 hours)
     * @return true if the report was created within the last 24 hours
     */
    public boolean isRecent() {
        return createdAt.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    /**
     * Checks if this report is old (older than 7 days)
     * @return true if the report is older than 7 days
     */
    public boolean isOld() {
        return createdAt.isBefore(LocalDateTime.now().minusDays(7));
    }
    
    /**
     * Gets the age of this report in hours
     * @return The number of hours since the report was created
     */
    public long getAgeInHours() {
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
    }
    
    /**
     * Gets the type of intelligence based on content
     * @return The type of intelligence (Resource, Member, Territory, Mission, or General)
     */
    public String getIntelligenceType() {
        if (content.contains("📊") || content.toLowerCase().contains("resource")) {
            return "Resource Intelligence";
        } else if (content.contains("👥") || content.toLowerCase().contains("member")) {
            return "Member Intelligence";
        } else if (content.contains("🏰") || content.toLowerCase().contains("territory")) {
            return "Territory Intelligence";
        } else if (content.contains("⚔️") || content.toLowerCase().contains("mission")) {
            return "Mission Intelligence";
        } else {
            return "General Intelligence";
        }
    }
    
    /**
     * Gets a formatted string representation of the spy report
     * @return Formatted string with report details
     */
    public String getFormattedReport() {
        return String.format("🕵️ Spy Report #%s\n" +
                           "From: %s | Target: %s\n" +
                           "Type: %s | Created: %s\n" +
                           "Content: %s",
                           id, fromGuildId, targetGuildId, 
                           getIntelligenceType(), createdAt.toLocalDate(), 
                           content);
    }
    
    /**
     * Gets a summary string for display purposes
     * @return Short summary of the report
     */
    public String getSummary() {
        String truncatedContent = content.length() > 50 ? content.substring(0, 47) + "..." : content;
        return String.format("[%s] %s → %s: %s", 
                           getIntelligenceType().substring(0, 3).toUpperCase(),
                           fromGuildId, targetGuildId, truncatedContent);
    }
}
