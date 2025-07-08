package org.hsh.games.aoe.entities.rebelcell;

/**
 * Enum representing different types of missions available to Rebel Cells.
 * Each mission type has unique characteristics and requirements.
 * 
 * @author devTASE
 */
public enum MissionType {
    RESOURCE_RAID("Resource Raid", "🎯", "Raid enemy positions for resources"),
    RESOURCE_RUN("Resource Run", "🎯", "Run resources to secure locations"),
    TERRITORY_CONQUEST("Territory Conquest", "🏴", "Conquer enemy territories"),
    ESPIONAGE("Espionage", "🔍", "Gather intelligence through spying"),
    VAULT_DEFENSE("Vault Defense", "🛡️", "Defend guild vault from attackers"),
    BATTLE("Battle", "⚔️", "Engage in direct combat"),
    INFILTRATION("Infiltration", "🥷", "Infiltrate enemy installations for intelligence"),
    SABOTAGE("Sabotage", "💥", "Sabotage enemy infrastructure"),
    RESCUE("Rescue Mission", "🚑", "Rescue captured allies"),
    DEFENSE("Defense", "🛡️", "Defend allied territories"),
    RECONNAISSANCE("Reconnaissance", "🔍", "Gather intelligence on enemy movements");

    private final String displayName;
    private final String emoji;
    private final String description;

    MissionType(String displayName, String emoji, String description) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayNameWithEmoji() {
        return emoji + " " + displayName;
    }
}
