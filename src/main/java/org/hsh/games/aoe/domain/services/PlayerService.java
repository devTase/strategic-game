package org.hsh.games.aoe.domain.services;

import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.domain.entities.DailyReward;
import org.hsh.games.aoe.domain.entities.Player;
import org.hsh.games.aoe.domain.entities.TechPhase;
import org.hsh.games.aoe.domain.entities.buildings.Building;
import org.hsh.games.aoe.domain.entities.buildings.BuildingAndResourceAvailabilityPerLevel;
import org.hsh.games.aoe.domain.entities.buildings.ConstructionProcess;
import org.hsh.games.aoe.domain.entities.skills.PlayerSkills;
import org.hsh.games.aoe.shared.constants.ApplicationConstants;
import org.hsh.games.aoe.domain.entities.buildings.ConstructionType;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.infrastructure.adapters.outbound.threading.ResourceConsumptionThread;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerService {
    private Player player;
    private List<ResourceAmount> playerResources = new ArrayList<>();
    private List<CyberOperative> cyberOperatives = new ArrayList<>();
    private List<Building> buildingList = new ArrayList<>();
    private DailyRewardService dailyRewardService = new DailyRewardService();
    private PlayerSkills playerSkills;

    public PlayerService(Player player) {
        this.player = player;
        this.playerSkills = new PlayerSkills(player);
        this.setLevel(1);
        addCyberOperative(new CyberOperative("operative1"));
        addCyberOperative(new CyberOperative("operative2"));
        addCyberOperative(new CyberOperative("operative3"));
    }

    public Player getPlayer() {
        return player;
    }

    public List<CyberOperative> getCyberOperatives() {
        return cyberOperatives;
    }

    public List<ResourceAmount> getPlayerResources() {
        return playerResources;
    }

    public void setPlayerResources(List<ResourceAmount> playerResources) {
        this.playerResources = playerResources;
    }

    public void addCyberOperative(CyberOperative operative) {
        for(ResourceAmount playerResource : playerResources) {
            if(playerResource.getResource() == ResourceType.DATA) {
                if(cyberOperatives.size() < playerResource.getAmount()) {
                    cyberOperatives.add(operative);
                    startConsumptionThread(operative);
                } else {
                    System.out.println("Chegaste ao limite de operativos cyber!");
                }
            }
        }
    }

    public void startConsumptionThread(CyberOperative operative) {
        ResourceConsumptionThread consumptionThread = new ResourceConsumptionThread(operative, playerResources);
        consumptionThread.start();
        consumptionThread.setOnThreadInterruptedListener(() -> removeCyberOperative(operative));
    }

    private void removeCyberOperative(CyberOperative operative) {
        cyberOperatives.remove(operative);
        Optional<ResourceAmount> populationResource = playerResources.stream()
                .filter(resource -> resource.getResource() == ResourceType.DATA)
                .findFirst();
        populationResource.ifPresent(resource -> {
            int populationAmount = resource.getAmount();
            if (populationAmount > 0) {
                resource.setAmount(populationAmount - 1);
            }
        });
        System.out.println("Operative cyber " + operative.getName() + " morreu por falta de recursos.");
    }

    public void showResourcesHeader() {
        showEnhancedDashboard();
    }

    public void showEnhancedDashboard() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                    🏛️  STRATEGIC GAME  🏛️                                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        // Village Info
        System.out.printf("║ 🏘️  Aldeia: %-25s │ %s Fase: %-25s ║%n", 
            player.getFarmName(), getPhaseIcon(), player.getTechPhase().getPhaseName());
        
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        // Resources Section
        System.out.println("║                                        📦 RECURSOS 📦                                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        // Split resources into two columns for better layout
        List<ResourceAmount> basicResources = new ArrayList<>();
        List<ResourceAmount> advancedResources = new ArrayList<>();
        
        for (ResourceAmount resource : playerResources) {
            if (isBasicResource(resource.getResource())) {
                basicResources.add(resource);
            } else {
                advancedResources.add(resource);
            }
        }
        
        // Display basic resources
        for (int i = 0; i < Math.max(basicResources.size(), advancedResources.size()); i++) {
            String leftSide = "";
            String rightSide = "";
            
            if (i < basicResources.size()) {
                ResourceAmount resource = basicResources.get(i);
                if (resource.getResource() == ResourceType.DATA) {
                    long availableOperatives = cyberOperatives.stream().filter(operative -> !operative.isOccupied()).count();
                    leftSide = String.format("║ %s %-12s: %d/%d disponíveis", 
                        getResourceIcon(resource.getResource()), 
                        resource.getResource().getDescription(),
                        availableOperatives, cyberOperatives.size());
                } else {
                    leftSide = String.format("║ %s %-12s: %-8d", 
                        getResourceIcon(resource.getResource()), 
                        resource.getResource().getDescription(), 
                        resource.getAmount());
                }
            } else {
                leftSide = "║" + " ".repeat(45);
            }
            
            if (i < advancedResources.size()) {
                ResourceAmount resource = advancedResources.get(i);
                rightSide = String.format("│ %s %-12s: %-8d ║", 
                    getResourceIcon(resource.getResource()), 
                    resource.getResource().getDescription(), 
                    resource.getAmount());
            } else {
                rightSide = "│" + " ".repeat(45) + "║";
            }
            
            System.out.println(leftSide + rightSide);
        }
        
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        // Quick Status Section
        System.out.println("║                                      ⚡ STATUS RÁPIDO ⚡                                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
        
        long busyOperatives = cyberOperatives.stream().filter(CyberOperative::isOccupied).count();
        int totalBuildings = buildingList.size();
        long completedBuildings = buildingList.stream().filter(Building::getBuilded).count();
        
        // Daily reward status
        String playerId = player.getFarmName();
        String dailyStatus = dailyRewardService.hasClaimedToday(playerId) ? 
            "✅ Coletada" : "🎁 Disponível";
        int currentStreak = dailyRewardService.getCurrentStreak(playerId);
        
        System.out.printf("║ 🤖 Operativos Cyber Ocupados: %-8d │ 🏢️  Edifícios: %d/%d concluídos        ║%n", 
            busyOperatives, completedBuildings, totalBuildings);
        System.out.printf("║ 🎁 Recompensa Diária: %-14s     │ 🔥 Streak: %-3d dias consecutivos      ║%n",
            dailyStatus, currentStreak);
        
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private String getPhaseIcon() {
        return switch (player.getTechPhase()) {
            case UPRISING -> "⚡";
            case AUGMENTED_STREETS -> "🤖";
            case NEURAL_NEXUS -> "🧠";
            case DRONE_DOMINION -> "🚁";
            case QUANTUM_DAWN -> "⚛️";
            case SINGULARITY_PREP -> "🔮";
            case POST_SINGULARITY -> "🌌";
            case HYPER_MESH -> "🕸️";
            case EXO_REALITY -> "👁️";
        };
    }
    
    private String getResourceIcon(ResourceType resourceType) {
        return switch (resourceType) {
            case ENERGY -> "⚡";
            case DATA -> "💾";
            case COMPONENTS -> "🔧";
            case CIRCUITS -> "🖲️";
            case NANOMATERIALS -> "⚛️";
            case QUANTUM_ENERGY -> "🌟";
            case CRYPTO -> "💰";
        };
    }
    
    private boolean isBasicResource(ResourceType resourceType) {
        return resourceType == ResourceType.ENERGY || 
               resourceType == ResourceType.DATA || 
               resourceType == ResourceType.COMPONENTS;
    }

    /**
     * Check if the player is eligible for a new tech phase
     * @return true if the player is eligible for a new tech phase
     * @return false if the player is not eligible for a new tech phase
     */
    public boolean isPlayerEligibleForNewPhase() {
        return buildingList.stream().allMatch(building ->
                player.getTechPhase()
                        .getRequirementsForNextLevel()
                        .get(ConstructionType.getEnumFromConstant(building.getConstructionTypeName())) <= building.getLevel());
    }

    // Deprecated method for backward compatibility
    @Deprecated(since = "2.0", forRemoval = true)
    public boolean isPlayerEligibleForNewEra() {
        return isPlayerEligibleForNewPhase();
    }

    public void setLevel(int level) {
        player.setTechPhase(TechPhase.getByLevel(level));
        System.out.println("Descoberta Uma Nova FASE TECNOLÓGICA! A Fase " + player.getTechPhase().getPhaseName());

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        BuildingAndResourceAvailabilityPerLevel availability = BuildingAndResourceAvailabilityPerLevel
                .getByLevel(player.getTechPhase().getLevel());
        
        List<ConstructionType> availableConstructions = availability.getAvailableConstructions();
        List<ResourceType> availableResourceTypes = availability.getAvailableResources();

        buildingList.addAll(availableConstructions.stream()
                .map(constructionType -> new Building(false, constructionType))
                .toList());

        playerResources.addAll(availableResourceTypes.stream()
                .map(resourceType -> new ResourceAmount(resourceType, resourceType.getInitialOffer()))
                .toList());
    }

    public void addResource(ResourceType resourceType, int amount) {
        Optional<ResourceAmount> optionalResource = playerResources.stream()
                .filter(resource -> resource.getResource() == resourceType)
                .findFirst();

        if (optionalResource.isPresent()) {
            ResourceAmount resource = optionalResource.get();
            resource.setAmount(resource.getAmount() + amount);
        } else {
            playerResources.add(new ResourceAmount(resourceType, amount));
        }
    }

    public List<Building> getBuildingList() {
        return buildingList;
    }

    public boolean checkIfPlayerHasEnoughResources(Building building) {
        List<ResourceAmount> requiredResources = building.getResourceCost();
        boolean hasResourcesAvailable = false;

        for (ResourceAmount buildingResource : requiredResources) {
            for (ResourceAmount playerResource : playerResources) {
                if(playerResource.getResource() == buildingResource.getResource()) {
                    hasResourcesAvailable = playerResource.getAmount() >= buildingResource.getAmount();
                    if(!hasResourcesAvailable) return false;
                }
            }
        }

        return hasResourcesAvailable;
    }

    public void addNewBuilding(Building newBuilding) {
        buildingList.add(newBuilding);
    }

    public Boolean isFirstTimeBuilding(Building selectedBuilding) {
        return buildingList.contains(selectedBuilding) && !selectedBuilding.getBuilded();
    }

    public Boolean checkIfBuildingAmountHasReached(Building building) {
        int constructionsAllowed = building.getAmountConstructionsAllowed();
        long constructedCount = getBuildingList().stream()
                .filter(b -> b.getConstructionTypeName().equals(building.getConstructionTypeName()) && b.getBuilded())
                .count();

        return constructedCount >= constructionsAllowed;
    }

    public Boolean checkIfBuildingHasReachedItsMaximLevel(Building building) {
        return building.getLevel() >= building.getMaxLevel();
    }

    public void sendCyberOperativesToConstructionJob(ConstructionProcess process, Building construction) {
        prepareNeededResources(construction);
        getCyberOperativeAvailable().makeConstruction(process, construction, playerResources, cyberOperatives);
    }

    public void sendCyberOperativesToSearchJob(ResourceType resourcesToSearch) {
        getCyberOperativeAvailable().searchResources(resourcesToSearch, playerResources);
    }

    private void prepareNeededResources(Building construction) {
        List<ResourceAmount> requiredResources = construction.getResourceCost();

        for (ResourceAmount buildingResource : requiredResources) {
            for (ResourceAmount playerResource : playerResources) {
                if(playerResource.getResource() == buildingResource.getResource()) {
                    playerResource.setAmount(playerResource.getAmount() - buildingResource.getAmount());
                }
            }
        }
    }

    public CyberOperative getCyberOperativeAvailable() {
        Optional<CyberOperative> operative = cyberOperatives.stream().filter(x -> !x.isOccupied()).findFirst();
        if(operative.isPresent()) {
            return operative.get();
        } else {
            System.out.println("Todos os seus operativos cyber estão ocupados com tarefas!");
            System.out.println("Crie mais operativos cyber.");
            return null;
        }
    }

    public void checkForNewPhaseConditions() {
        if(isPlayerEligibleForNewPhase()) {
            int nextLevel = player.getTechPhase().getNextPhase().getLevel();
            setLevel(nextLevel);
        }
    }

    // Deprecated method for backward compatibility
    @Deprecated(since = "2.0", forRemoval = true)
    public void checkForNewEraConditions() {
        checkForNewPhaseConditions();
    }

    public Set<String> getAvailableConstructionTypes() {
        return buildingList.stream()
                .filter(building -> !checkIfBuildingAmountHasReached(building) && !checkIfBuildingHasReachedItsMaximLevel(building))
                .map(Building::getConstructionTypeName)
                .collect(Collectors.toSet());
    }

    public List<Building> getBuildingsFromConstructionName(String constructionName) {
        return getBuildingList().stream()
                .filter(building -> building.getConstructionTypeName().equals(constructionName))
                .toList();
    }

    public void claimDailyReward() {
        List<ResourceAmount> rewards = dailyRewardService.claimDailyReward(player.getFarmName(), player.getTechPhase());
        rewards.forEach(this::addResourceFromReward);
        System.out.println("Recompensa diária coletada!");
        rewards.forEach(reward -> System.out.printf("%s: +%d\n", reward.getResource().getDescription(), reward.getAmount()));
    }
    
    /**
     * Claims daily reward with option to deposit directly to guild vault.
     * 
     * @param depositToGuild true to deposit to guild vault, false to add to player inventory
     */
    public void claimDailyRewardWithGuildOption(boolean depositToGuild) {
        List<ResourceAmount> rewards;
        
        try {
            if (depositToGuild && dailyRewardService.isGuildVaultDepositAvailable()) {
                rewards = dailyRewardService.claimDailyRewardWithGuildOption(
                    player.getFarmName(), player.getTechPhase(), true);
                
                // If rewards list is empty, they were deposited to guild vault
                if (rewards.isEmpty()) {
                    System.out.println("✅ Recompensa diária depositada na arca da guilda!");
                    
                    // Show what rewards were given to the guild
                    int currentStreak = dailyRewardService.getCurrentStreak(player.getFarmName());
                    if (currentStreak > 0) {
                        // Get the current day reward that was just claimed
                        DailyReward claimedReward = dailyRewardService.getNextReward(player.getFarmName());
                        // We need to get the previous reward since getNextReward returns the next one
                        // For now, we'll create a temporary service to calculate the rewards
                        DailyRewardService tempService = new DailyRewardService();
                        List<ResourceAmount> guildDeposits = tempService.claimDailyReward(
                            "temp_calc", player.getTechPhase());
                        
                        // The first reward (day 1) is what we just deposited
                        if (currentStreak == 1) {
                            System.out.println("Recursos depositados na guilda:");
                            guildDeposits.forEach(reward -> 
                                System.out.printf("🏦 %s: +%d (depositado na arca da guilda)\n", 
                                                reward.getResource().getDescription(), reward.getAmount()));
                        }
                    }
                    return;
                }
            } else {
                rewards = dailyRewardService.claimDailyRewardWithGuildOption(
                    player.getFarmName(), player.getTechPhase(), false);
            }
            
            // Add rewards to player inventory (normal behavior)
            rewards.forEach(this::addResourceFromReward);
            System.out.println("💰 Recompensa diária coletada para o inventário!");
            rewards.forEach(reward -> 
                System.out.printf("%s: +%d\n", reward.getResource().getDescription(), reward.getAmount()));
                
        } catch (IllegalArgumentException e) {
            // Handle cases where guild vault deposit isn't possible
            System.err.println("❌ Erro ao depositar na guilda: " + e.getMessage());
            System.out.println("🔄 Depositando no inventário do jogador...");
            
            // Fallback to player inventory
            claimDailyReward();
        } catch (IllegalStateException e) {
            // Handle service or vault capacity issues
            System.err.println("❌ " + e.getMessage());
        }
    }
    
    /**
     * Sets guild service for daily reward service to enable guild vault deposits.
     * 
     * @param guildService The guild service to use
     */
    public void setGuildService(GuildService guildService) {
        this.dailyRewardService.setGuildService(guildService);
    }

    public DailyRewardService getDailyRewardService() {
        return dailyRewardService;
    }

    private void addResourceFromReward(ResourceAmount reward) {
        Optional<ResourceAmount> existingResource = playerResources.stream()
                .filter(resource -> resource.getResource() == reward.getResource())
                .findFirst();

        if (existingResource.isPresent()) {
            existingResource.get().setAmount(existingResource.get().getAmount() + reward.getAmount());
        } else {
            playerResources.add(reward);
        }
    }

    /**
     * Validates if all mandatory buildings for the current tech phase are built and meet level requirements.
     * Mandatory buildings are defined in the TechPhase requirements map.
     * 
     * @return true if all mandatory buildings are met, false otherwise
     */
    public boolean validateMandatoryBuildings() {
        Map<ConstructionType, Integer> requiredBuildings = player.getTechPhase().getRequirementsForNextLevel();
        
        for (Map.Entry<ConstructionType, Integer> entry : requiredBuildings.entrySet()) {
            ConstructionType requiredType = entry.getKey();
            int requiredLevel = entry.getValue();
            
            boolean hasRequiredBuilding = buildingList.stream()
                    .anyMatch(building -> 
                            building.getConstructionTypeName().equals(requiredType.getName()) &&
                            building.getBuilded() &&
                            building.getLevel() >= requiredLevel
                    );
            
            if (!hasRequiredBuilding) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Gets a list of missing mandatory buildings for the current tech phase.
     * 
     * @return List of construction types that are missing or don't meet level requirements
     */
    public List<ConstructionType> getMissingMandatoryBuildings() {
        Map<ConstructionType, Integer> requiredBuildings = player.getTechPhase().getRequirementsForNextLevel();
        List<ConstructionType> missingBuildings = new ArrayList<>();
        
        for (Map.Entry<ConstructionType, Integer> entry : requiredBuildings.entrySet()) {
            ConstructionType requiredType = entry.getKey();
            int requiredLevel = entry.getValue();
            
            boolean hasRequiredBuilding = buildingList.stream()
                    .anyMatch(building -> 
                            building.getConstructionTypeName().equals(requiredType.getName()) &&
                            building.getBuilded() &&
                            building.getLevel() >= requiredLevel
                    );
            
            if (!hasRequiredBuilding) {
                missingBuildings.add(requiredType);
            }
        }
        
        return missingBuildings;
    }
    
    /**
     * Gets the time multiplier from player skills for construction speed.
     * Used by Building class to calculate construction time with skill modifiers.
     * @return Time multiplier (less than 1.0 means faster construction)
     */
    public double getConstructionTimeMultiplier() {
        return playerSkills.getTimeMultiplier();
    }
    
    /**
     * Gets the cost multiplier from player skills for construction cost.
     * Used by Building class to calculate construction cost with skill modifiers.
     * @return Cost multiplier (less than 1.0 means cheaper construction)
     */
    public double getConstructionCostMultiplier() {
        return playerSkills.getCostMultiplier();
    }
    
    /**
     * Gets the loot multiplier from player skills for plunder bonus.
     * Used by combat/raid services to calculate loot with skill modifiers.
     * @return Loot multiplier (greater than 1.0 means more loot)
     */
    public double getLootMultiplier() {
        return playerSkills.getLootMultiplier();
    }
    
    /**
     * Gets the player skills instance for advanced operations.
     * @return PlayerSkills instance
     */
    public PlayerSkills getPlayerSkills() {
        return playerSkills;
    }
}
