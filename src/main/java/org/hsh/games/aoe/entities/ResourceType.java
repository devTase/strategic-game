package org.hsh.games.aoe.entities;

import org.hsh.games.aoe.ThreadUtils;

import java.util.List;
import java.util.Objects;

public enum ResourceType {
    WOOD("Madeira", 100, Difficulty.EASY, ThreadUtils.toMilliseconds(5), 100, 2),
    POPULATION("População", 3, Difficulty.HARD, ThreadUtils.toMilliseconds(30), 2,0),
    WATER("Água", 200, Difficulty.EASY, ThreadUtils.toMilliseconds(3), 100,1),
    FOOD("Comida", 100, Difficulty.EASY, ThreadUtils.toMilliseconds(7), 100,3),
    STONE("Pedra", 10, Difficulty.EASY, ThreadUtils.toMilliseconds(10), 70,4),
    IRON("Ferro", 0, Difficulty.MEDIUM, ThreadUtils.toMilliseconds(15), 62,10),
    SILVER("Prata", 0, Difficulty.MEDIUM, ThreadUtils.toMilliseconds(20), 43,8),
    GRAPES("Uvas", 0, Difficulty.MEDIUM, ThreadUtils.toMilliseconds(15), 32,7),
    GOLD("Ouro", 0, Difficulty.EXTREME, ThreadUtils.toMilliseconds(30), 8, 50),
    FAVOR("Favor aos Deuses", 0, Difficulty.EXTREME, ThreadUtils.toMilliseconds(30), 8,0);

    String description;
    Difficulty hardToGet;
    int timeLimitForSearch;
    int amountMaxToBeFound;
    int pricePerUnit;
    int initialOffer;

    ResourceType(
            String description,
            int initialOffer,
            Difficulty hardToGet,
            int timeLimitForSearch,
            int amountMaxToBeFound,
            int pricePerUnit
    ) {
        this.description = description;
        this.hardToGet = hardToGet;
        this.timeLimitForSearch = timeLimitForSearch;
        this.amountMaxToBeFound = amountMaxToBeFound;
        this.pricePerUnit = pricePerUnit;
        this.initialOffer = initialOffer;
    }

    public int getInitialOffer() {
        return initialOffer;
    }

    public void setInitialOffer(int initialOffer) {
        this.initialOffer = initialOffer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getHardToGet() {
        return hardToGet;
    }

    public void setHardToGet(Difficulty hardToGet) {
        this.hardToGet = hardToGet;
    }

    public int getTimeLimitForSearch() {
        return timeLimitForSearch;
    }

    public void setTimeLimitForSearch(int timeLimitForSearch) {
        this.timeLimitForSearch = timeLimitForSearch;
    }

    public int getAmountMaxToBeFound() {
        return amountMaxToBeFound;
    }

    public void setAmountMaxToBeFound(int amountMaxToBeFound) {
        this.amountMaxToBeFound = amountMaxToBeFound;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    /**
     * Gets resources available in the given era level.
     * This method is exposed for use by GuildMissionService and other services.
     * @param currentEraLevel The era level to get resources for
     * @return List of resource types available in the era
     */
    public static List<ResourceType> getResourcesPackBasedOnCurrentEra(int currentEraLevel) {
        EraAge currentEra = EraAge.getByLevel(currentEraLevel);
        return switch (Objects.requireNonNull(currentEra)) {
            case STONE_AGE -> List.of(POPULATION, FOOD, WATER, WOOD, STONE);
            case BRONZE_AGE -> List.of(IRON);
            case IRON_AGE -> List.of(SILVER);
            case MEDIEVAL_AGE -> List.of(GRAPES);
            case INDUSTRIAL_AGE -> List.of(GOLD);
            case MODERN_AGE -> List.of(FAVOR);
            default -> List.of();
        };
    }

    /**
     * Validates that a resource transfer to guild vault is valid
     * @param resourceType Type of resource being transferred
     * @param amount Amount to transfer
     * @throws IllegalArgumentException if transfer is invalid
     */
    public static void validateGuildVaultTransfer(ResourceType resourceType, int amount) {
        if (resourceType == null) {
            throw new IllegalArgumentException("Resource type cannot be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
    }

    /**
     * Checks if this resource type can be stored in guild vaults
     * @return true if the resource can be stored in guild vaults
     */
    public boolean isGuildVaultStorable() {
        // Most resources can be stored, but some special resources might not be
        // For now, all resources are storable in guild vaults
        return true;
    }

    /**
     * Gets the guild vault transfer multiplier for this resource type
     * Some resources might have different transfer rates to/from guild vaults
     * @return Transfer multiplier (1.0 = no change)
     */
    public double getGuildVaultTransferMultiplier() {
        return switch (this) {
            case FAVOR -> 0.5; // Favor transfers at 50% rate (divine blessing is personal)
            case POPULATION -> 0.8; // Population transfers at 80% rate (some prefer to stay)
            default -> 1.0; // All other resources transfer at full rate
        };
    }
}
