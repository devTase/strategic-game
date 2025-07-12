package org.hsh.games.aoe.domain.entities.rebelcell;

/**
 * Represents a report generated from a spying action.
 * Contains intelligence information about a target guild.
 * 
 * @author devTASE
 */
public class SpyReport {
    private final String reportId;
    private final String fromGuildId;
    private final String targetGuildId;
    private final String content;

    private SpyReport(String reportId, String fromGuildId, String targetGuildId, String content) {
        this.reportId = reportId;
        this.fromGuildId = fromGuildId;
        this.targetGuildId = targetGuildId;
        this.content = content;
    }

    public static SpyReport createNew(String reportId, String fromGuildId, String targetGuildId, String content) {
        return new SpyReport(reportId, fromGuildId, targetGuildId, content);
    }

    public static SpyReport createResourceReport(String reportId, String fromGuildId, String targetGuildId, int resourceCount) {
        String content = "Resources: " + resourceCount + " units available.";
        return new SpyReport(reportId, fromGuildId, targetGuildId, content);
    }

    public String getReportId() {
        return reportId;
    }

    public String getFromGuildId() {
        return fromGuildId;
    }

    public String getTargetGuildId() {
        return targetGuildId;
    }

    public String targetGuildId() {
        return targetGuildId;
    }

    public boolean isFrom(String guildId) {
        return fromGuildId.equals(guildId);
    }

    public String getIntelligenceType() {
        return "Resource Intelligence";
    }

    public String getFormattedReport() {
        return String.format("📊 Spy Report #%s\n🎯 Target: %s\n📝 %s", 
                reportId, targetGuildId, content);
    }

    public long getAgeInHours() {
        // For demo purposes, return a mock age
        return 1;
    }

    @Override
    public String toString() {
        return String.format("SpyReport{id='%s', fromGuild='%s', targetGuild='%s', content='%s'}",
                reportId, fromGuildId, targetGuildId, content);
    }
}

