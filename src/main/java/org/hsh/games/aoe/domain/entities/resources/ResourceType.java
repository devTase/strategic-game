package org.hsh.games.aoe.domain.entities.resources;

import org.hsh.games.aoe.domain.entities.Difficulty;
import org.hsh.games.aoe.domain.entities.TechPhase;
import org.hsh.games.aoe.shared.utils.ThreadUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum ResourceType {
    ENERGY("Energy Cells", 250, Difficulty.EASY, ThreadUtils.toMilliseconds(8), 150, 5, "⚡", 1000, 10, 50),
    DATA("Data Streams", 180, Difficulty.EASY, ThreadUtils.toMilliseconds(6), 120, 3, "💾", 800, 5, 30),
    COMPONENTS("Tech Components", 120, Difficulty.MEDIUM, ThreadUtils.toMilliseconds(12), 80, 8, "🔧", 600, 3, 20),
    CIRCUITS("Neural Circuits", 60, Difficulty.MEDIUM, ThreadUtils.toMilliseconds(18), 50, 15, "🖲️", 400, 2, 15),
    NANOMATERIALS("Nanomaterials", 30, Difficulty.HARD, ThreadUtils.toMilliseconds(25), 35, 25, "⚛️", 200, 1, 10),
    QUANTUM_ENERGY("Quantum Energy", 15, Difficulty.EXTREME, ThreadUtils.toMilliseconds(35), 20, 50, "🌟", 100, 1, 5),
    CRYPTO("Crypto Tokens", 8, Difficulty.EXTREME, ThreadUtils.toMilliseconds(40), 12, 100, "💰", 50, 1, 3);

    // Legacy resource mapping for transition period
    private static final Map<String, ResourceType> LEGACY_MAPPING = Map.of(
        "WOOD", ENERGY,
        "POPULATION", DATA,
        "WATER", ENERGY,
        "FOOD", DATA,
        "STONE", COMPONENTS,
        "IRON", CIRCUITS,
        "SILVER", NANOMATERIALS,
        "GRAPES", COMPONENTS,
        "GOLD", QUANTUM_ENERGY,
        "FAVOR", CRYPTO
    );

    String description;
    Difficulty hardToGet;
    int timeLimitForSearch;
    int amountMaxToBeFound;
    int pricePerUnit;
    int initialOffer;
    String icon;
    private final int maxCap;
    private final int minGather;
    private final int maxGather;

    ResourceType(
            String description,
            int initialOffer,
            Difficulty hardToGet,
            int timeLimitForSearch,
            int amountMaxToBeFound,
            int pricePerUnit,
            String icon,
            int maxCap,
            int minGather,
            int maxGather
    ) {
        this.description = description;
        this.hardToGet = hardToGet;
        this.timeLimitForSearch = timeLimitForSearch;
        this.amountMaxToBeFound = amountMaxToBeFound;
        this.pricePerUnit = pricePerUnit;
        this.initialOffer = initialOffer;
        this.icon = icon;
        this.maxCap = maxCap;
        this.minGather = minGather;
        this.maxGather = maxGather;
    }

    public String getIcon() {
        return icon;
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
     * Gets the maximum capacity for this resource type
     * @return The maximum amount that can be stored for this resource
     */
    public int getMaxCap() {
        return maxCap;
    }

    /**
     * Gets the minimum amount that can be gathered per collection for this resource type
     * @return The minimum gathering amount
     */
    public int getMinGather() {
        return minGather;
    }

    /**
     * Gets the maximum amount that can be gathered per collection for this resource type
     * @return The maximum gathering amount
     */
    public int getMaxGather() {
        return maxGather;
    }

    /**
     * Gets resources available in the given phase level.
     * This method is exposed for use by GuildMissionService and other services.
     * @param currentPhaseLevel The phase level to get resources for
     * @return List of resource types available in the phase
     */
    public static List<ResourceType> getResourcesPackBasedOnCurrentPhase(int currentPhaseLevel) {
        TechPhase currentPhase = TechPhase.getByLevel(currentPhaseLevel);
        return switch (Objects.requireNonNull(currentPhase)) {
            case UPRISING -> List.of(ENERGY, DATA);
            case AUGMENTED_STREETS -> List.of(COMPONENTS);
            case NEURAL_NEXUS -> List.of(CIRCUITS);
            case DRONE_DOMINION -> List.of(NANOMATERIALS);
            case QUANTUM_DAWN -> List.of(QUANTUM_ENERGY);
            case SINGULARITY_PREP -> List.of(CRYPTO);
            case POST_SINGULARITY -> List.of(ENERGY, DATA, COMPONENTS);
            case HYPER_MESH -> List.of(CIRCUITS, NANOMATERIALS);
            case EXO_REALITY -> List.of(QUANTUM_ENERGY, CRYPTO);
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
            case CRYPTO -> 0.6; // Crypto tokens have network fees
            case QUANTUM_ENERGY -> 0.7; // Quantum energy degrades during transfer
            case DATA -> 0.9; // Data streams have minor compression loss
            default -> 1.0; // All other resources transfer at full rate
        };
    }

    /**
     * Gets the modern ResourceType from a legacy resource name
     * Used during transition period to maintain compatibility
     * @param legacyName The legacy resource name (e.g., "WOOD", "GOLD")
     * @return The corresponding modern ResourceType
     * @throws IllegalArgumentException if legacy name is not recognized
     */
    public static ResourceType fromLegacyName(String legacyName) {
        ResourceType mapped = LEGACY_MAPPING.get(legacyName.toUpperCase());
        if (mapped == null) {
            throw new IllegalArgumentException("Unknown legacy resource: " + legacyName);
        }
        return mapped;
    }

    /**
     * Gets all legacy resource names that map to this modern resource
     * @return List of legacy names that convert to this resource
     */
    public List<String> getLegacyNames() {
        return LEGACY_MAPPING.entrySet().stream()
                .filter(entry -> entry.getValue() == this)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Gets the conversion map for all legacy resources
     * @return Map of legacy resource names to modern ResourceType
     */
    public static Map<String, ResourceType> getLegacyConversionMap() {
        return Map.copyOf(LEGACY_MAPPING);
    }
}
