package org.hsh.games.aoe.entities;

public class Player {

    private String farmName;
    private TechPhase techPhase;
    private String guildId; // Optional guild reference
    private static final int MIN_NAME_LENGTH = 3;

    public Player(String farmName) {
        validateFarmName(farmName);
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

    // Deprecated methods for backward compatibility - will be removed in future versions
    @Deprecated(since = "2.0", forRemoval = true)
    public void setEraAge(EraAge eraAge) {
        // Convert EraAge to TechPhase for backward compatibility
        this.techPhase = TechPhase.getByLevel(eraAge.getLevel());
    }

    @Deprecated(since = "2.0", forRemoval = true) 
    public EraAge getEraAge() {
        // Convert TechPhase to EraAge for backward compatibility
        return EraAge.getByLevel(techPhase.getLevel());
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
}
