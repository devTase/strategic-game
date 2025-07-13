package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.valueobjects.PlayerId;

import java.util.UUID;

public class Player {
    private PlayerId playerId;
    private String farmName;
    private TechPhase techPhase;
    private String guildId; // Optional guild reference
    private static final int MIN_NAME_LENGTH = 3;

    public Player(String farmName) {
        validateFarmName(farmName);
        this.playerId = new PlayerId(UUID.randomUUID().toString());
        this.farmName = farmName;
        this.techPhase = TechPhase.getByLevel(1);
    }

    private void validateFarmName(String farmName) {
        if (farmName == null || farmName.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da aldeia não pode estar vazio");
        }
        if (farmName.trim().length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("O nome da aldeia deve ter pelo menos " + MIN_NAME_LENGTH + " caracteres");
        }
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(PlayerId playerId) {
        this.playerId = playerId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        validateFarmName(farmName);
        this.farmName = farmName;
    }

    public void setTechPhase(TechPhase techPhase) {
        this.techPhase = techPhase;
    }

    public TechPhase getTechPhase() {
        return techPhase;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public boolean isInGuild() {
        return guildId != null && !guildId.isBlank();
    }

    public boolean canAdvanceToNextPhase() {
        return techPhase.getLevel() < TechPhase.getMaxLevel();
    }

    public void advanceToNextPhase() {
        if (canAdvanceToNextPhase()) {
            this.techPhase = TechPhase.getByLevel(techPhase.getLevel() + 1);
        }
    }

    public void joinGuild(String guildId) {
        if (guildId == null || guildId.trim().isEmpty()) {
            throw new IllegalArgumentException("Guild ID cannot be null or empty");
        }
        if (isInGuild()) {
            throw new IllegalStateException("Player is already in a guild");
        }
        this.guildId = guildId;
    }

    public void leaveGuild() {
        if (!isInGuild()) {
            throw new IllegalStateException("Player is not in a guild");
        }
        this.guildId = null;
    }
}
