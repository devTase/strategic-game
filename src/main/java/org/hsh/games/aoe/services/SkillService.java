package org.hsh.games.aoe.services;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.entities.skills.*;
import org.hsh.games.aoe.threads.SkillUpgradeThread;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton service for managing player skills and skill upgrades.
 * Provides functionality for listing skills, starting upgrades, and managing upgrade processes.
 */
public class SkillService {
    
    private static SkillService instance;
    
    // Base costs for skill upgrades (can be adjusted based on balance)
    private static final int BASE_WOOD_COST = 100;
    private static final int BASE_FOOD_COST = 80;
    private static final int BASE_STONE_COST = 50;
    
    // Base duration for skill upgrades (in milliseconds)
    private static final long BASE_UPGRADE_DURATION_MS = 30000; // 30 seconds for testing
    
    private SkillService() {
        // Private constructor for singleton
    }
    
    /**
     * Gets the singleton instance of SkillService.
     * @return The SkillService instance
     */
    public static synchronized SkillService getInstance() {
        if (instance == null) {
            instance = new SkillService();
        }
        return instance;
    }
    
    /**
     * Lists all skills for a player with their current levels.
     * @param playerService The player service containing player skills
     * @return Map of skill types to their current skill objects
     */
    public Map<SkillType, Skill> listSkills(PlayerService playerService) {
        if (playerService == null) {
            throw new IllegalArgumentException("Player service cannot be null");
        }
        
        return playerService.getPlayerSkills().getSkills();
    }
    
    /**
     * Gets the current skill upgrade process for a player.
     * @param playerService The player service
     * @return Current upgrade process or null if no upgrade is running
     */
    public SkillUpgradeProcess getCurrentUpgrade(PlayerService playerService) {
        if (playerService == null) {
            throw new IllegalArgumentException("Player service cannot be null");
        }
        
        return playerService.getPlayerSkills().getCurrentUpgradeProcess();
    }
    
    /**
     * Starts a skill upgrade for the specified skill type.
     * Validates all requirements and deducts resources before starting.
     * 
     * @param playerService The player service
     * @param skillType The skill type to upgrade
     * @throws IllegalStateException if Academy not built, another upgrade running, level == 10, 
     *                              insufficient resources, or worker unavailable
     */
    public void startUpgrade(PlayerService playerService, SkillType skillType) {
        if (playerService == null) {
            throw new IllegalArgumentException("Player service cannot be null");
        }
        if (skillType == null) {
            throw new IllegalArgumentException("Skill type cannot be null");
        }
        
        // Validation 1: Check if Training Facility is built
        if (!isAcademyBuilt(playerService)) {
            throw new IllegalStateException("Training Facility must be built before upgrading skills");
        }
        
        // Validation 2: Check if another upgrade is running
        if (getCurrentUpgrade(playerService) != null) {
            SkillUpgradeProcess current = getCurrentUpgrade(playerService);
            if (!current.isUpgradeComplete()) {
                throw new IllegalStateException("Another skill upgrade is already in progress: " + 
                                              current.getSkillType().getDisplayName());
            }
        }
        
        PlayerSkills playerSkills = playerService.getPlayerSkills();
        Skill currentSkill = playerSkills.getSkill(skillType);
        
        if (currentSkill == null) {
            throw new IllegalStateException("Skill not found: " + skillType.getDisplayName());
        }
        
        // Validation 3: Check if skill is already at max level
        if (currentSkill.getLevel() >= 10) {
            throw new IllegalStateException("Skill " + skillType.getDisplayName() + 
                                          " is already at maximum level (10)");
        }
        
        int newLevel = currentSkill.getLevel() + 1;
        
        // Calculate dynamic costs and duration
        List<ResourceAmount> requiredResources = calculateUpgradeCost(newLevel);
        long upgradeDuration = calculateUpgradeDuration(newLevel);
        
        // Validation 4: Check if player has enough resources
        if (!hasEnoughResources(playerService, requiredResources)) {
            throw new IllegalStateException("Insufficient resources for upgrade. Required: " + 
                                          formatResourceRequirements(requiredResources));
        }
        
        // Validation 5: Check if cyber operative is available
        CyberOperative availableOperative = playerService.getCyberOperativeAvailable();
        if (availableOperative == null) {
            throw new IllegalStateException("No available cyber operatives for skill upgrade");
        }
        
        // All validations passed - proceed with upgrade
        
        // Deduct resources
        deductResources(playerService, requiredResources);
        
        // Create upgrade process
        SkillUpgradeProcess upgradeProcess = SkillUpgradeProcess.builder()
            .skillType(skillType)
            .fromLevel(currentSkill.getLevel())
            .toLevel(newLevel)
            .startEpochMillis(System.currentTimeMillis())
            .durationMillis(upgradeDuration)
            .requiredResources(requiredResources)
            .build();
        
        // Set upgrade process in player skills
        playerSkills.setCurrentUpgradeProcess(upgradeProcess);
        
        // Occupy cyber operative
        availableOperative.setOccupied(true);
        
        // Start upgrade thread
        SkillUpgradeThread upgradeThread = new SkillUpgradeThread(upgradeProcess, playerService);
        upgradeThread.setOnUpgradeCompleteListener(() -> {
            // Free cyber operative when upgrade completes
            availableOperative.setOccupied(false);
            System.out.println("Cyber operative " + availableOperative.getName() + " is now available");
        });
        
        upgradeThread.start();
        
        System.out.println("🎓 Started upgrading " + skillType.getDisplayName() + 
                         " from level " + currentSkill.getLevel() + " to level " + newLevel);
        System.out.println("⏱️ Upgrade will complete in " + (upgradeDuration / 1000) + " seconds");
        System.out.println("👷 Cyber operative " + availableOperative.getName() + " is now working on the upgrade");
    }
    
    /**
     * Checks if the Training Facility building is built and available.
     * @param playerService The player service
     * @return true if Training Facility is built, false otherwise
     */
    private boolean isAcademyBuilt(PlayerService playerService) {
        return playerService.getBuildingList().stream()
            .anyMatch(building -> 
                ConstructionType.TRAINING_FACILITY.getName().equals(building.getConstructionTypeName()) && 
                building.getBuilded()
            );
    }
    
    /**
     * Calculates the resource cost for upgrading to a specific level.
     * Formula: baseCost * (level²)
     * @param level The target level
     * @return List of required resources
     */
    private List<ResourceAmount> calculateUpgradeCost(int level) {
        int multiplier = level * level;
        
        return List.of(
            new ResourceAmount(ResourceType.ENERGY, BASE_WOOD_COST * multiplier),
            new ResourceAmount(ResourceType.DATA, BASE_FOOD_COST * multiplier),
            new ResourceAmount(ResourceType.COMPONENTS, BASE_STONE_COST * multiplier)
        );
    }
    
    /**
     * Calculates the duration for upgrading to a specific level.
     * Formula: baseMs * level
     * @param level The target level
     * @return Duration in milliseconds
     */
    private long calculateUpgradeDuration(int level) {
        return BASE_UPGRADE_DURATION_MS * level;
    }
    
    /**
     * Checks if the player has enough resources for the upgrade.
     * @param playerService The player service
     * @param requiredResources The required resources
     * @return true if player has enough resources, false otherwise
     */
    private boolean hasEnoughResources(PlayerService playerService, List<ResourceAmount> requiredResources) {
        List<ResourceAmount> playerResources = playerService.getPlayerResources();
        
        for (ResourceAmount required : requiredResources) {
            Optional<ResourceAmount> playerResource = playerResources.stream()
                .filter(resource -> resource.getResource() == required.getResource())
                .findFirst();
            
            if (playerResource.isEmpty() || playerResource.get().getAmount() < required.getAmount()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Deducts the required resources from the player's inventory.
     * @param playerService The player service
     * @param requiredResources The resources to deduct
     */
    private void deductResources(PlayerService playerService, List<ResourceAmount> requiredResources) {
        List<ResourceAmount> playerResources = playerService.getPlayerResources();
        
        for (ResourceAmount required : requiredResources) {
            Optional<ResourceAmount> playerResource = playerResources.stream()
                .filter(resource -> resource.getResource() == required.getResource())
                .findFirst();
            
            if (playerResource.isPresent()) {
                ResourceAmount resource = playerResource.get();
                resource.setAmount(resource.getAmount() - required.getAmount());
                System.out.println("💰 Deducted " + required.getAmount() + " " + 
                                 required.getResource().getDescription());
            }
        }
    }
    
    /**
     * Formats resource requirements for display in error messages.
     * @param resources The resource requirements
     * @return Formatted string
     */
    private String formatResourceRequirements(List<ResourceAmount> resources) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < resources.size(); i++) {
            ResourceAmount resource = resources.get(i);
            sb.append(resource.getAmount()).append(" ").append(resource.getResource().getDescription());
            if (i < resources.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
