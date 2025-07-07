package org.hsh.games.aoe;

import org.hsh.games.aoe.entities.*;
import org.hsh.games.aoe.threads.ResourceConsumptionThread;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerService {
    private Player player;
    private List<ResourceAmount> playerResources = new ArrayList<>();
    private List<Worker> workers = new ArrayList<>();
    private List<Building> buildingList = new ArrayList<>();
    private DailyRewardService dailyRewardService = new DailyRewardService();

    public PlayerService(Player player) {
        this.player = player;
        this.setLevel(1);
        addWorker(new Worker("worker1"));
        addWorker(new Worker("worker2"));
        addWorker(new Worker("worker3"));
    }

    public Player getPlayer() {
        return player;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public List<ResourceAmount> getPlayerResources() {
        return playerResources;
    }

    public void setPlayerResources(List<ResourceAmount> playerResources) {
        this.playerResources = playerResources;
    }

    public void addWorker(Worker worker) {
        for(ResourceAmount playerResource : playerResources) {
            if(playerResource.getResource() == ResourceType.POPULATION) {
                if(workers.size() < playerResource.getAmount()) {
                    workers.add(worker);
                    startConsumptionThread(worker);
                } else {
                    System.out.println("Chegaste ao limite de trabalhadores!");
                }
            }
        }
    }

    public void startConsumptionThread(Worker worker) {
        ResourceConsumptionThread consumptionThread = new ResourceConsumptionThread(worker, playerResources);
        consumptionThread.start();
        consumptionThread.setOnThreadInterruptedListener(() -> removeWorker(worker));
    }

    private void removeWorker(Worker worker) {
        workers.remove(worker);
        Optional<ResourceAmount> populationResource = playerResources.stream()
                .filter(resource -> resource.getResource() == ResourceType.POPULATION)
                .findFirst();
        populationResource.ifPresent(resource -> {
            int populationAmount = resource.getAmount();
            if (populationAmount > 0) {
                resource.setAmount(populationAmount - 1);
            }
        });
        System.out.println("Trabalhador " + worker.getName() + " morreu por falta de recursos.");
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
        System.out.printf("║ 🏘️  Aldeia: %-25s │ %s Era: %-25s ║%n", 
            player.getFarmName(), getEraIcon(), player.getEraAge().getEraName());
        
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
                if (resource.getResource() == ResourceType.POPULATION) {
                    long availableWorkers = workers.stream().filter(worker -> !worker.isOccupied()).count();
                    leftSide = String.format("║ %s %-12s: %d/%d disponíveis", 
                        getResourceIcon(resource.getResource()), 
                        resource.getResource().getDescription(),
                        availableWorkers, workers.size());
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
        
        long busyWorkers = workers.stream().filter(Worker::isOccupied).count();
        int totalBuildings = buildingList.size();
        long completedBuildings = buildingList.stream().filter(Building::getBuilded).count();
        
        // Daily reward status
        String playerId = player.getFarmName();
        String dailyStatus = dailyRewardService.hasClaimedToday(playerId) ? 
            "✅ Coletada" : "🎁 Disponível";
        int currentStreak = dailyRewardService.getCurrentStreak(playerId);
        
        System.out.printf("║ 👷 Trabalhadores Ocupados: %-8d │ 🏗️  Edifícios: %d/%d concluídos        ║%n", 
            busyWorkers, completedBuildings, totalBuildings);
        System.out.printf("║ 🎁 Recompensa Diária: %-14s     │ 🔥 Streak: %-3d dias consecutivos      ║%n",
            dailyStatus, currentStreak);
        
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private String getEraIcon() {
        return switch (player.getEraAge()) {
            case STONE_AGE -> "🗿";
            case BRONZE_AGE -> "🥉";
            case IRON_AGE -> "⚔️";
            case MEDIEVAL_AGE -> "🏰";
            case RENAISSANCE -> "🎨";
            case INDUSTRIAL_AGE -> "🏭";
            case MODERN_AGE -> "🌆";
            case INFORMATION_AGE -> "💻";
            case FUTURE_AGE -> "🚀";
        };
    }
    
    private String getResourceIcon(ResourceType resourceType) {
        return switch (resourceType) {
            case WOOD -> "🪵";
            case WATER -> "💧";
            case FOOD -> "🍞";
            case STONE -> "🪨";
            case POPULATION -> "👥";
            case IRON -> "⚙️";
            case SILVER -> "🥈";
            case GRAPES -> "🍇";
            case GOLD -> "🏅";
            case FAVOR -> "⭐";
        };
    }
    
    private boolean isBasicResource(ResourceType resourceType) {
        return resourceType == ResourceType.WOOD || 
               resourceType == ResourceType.WATER || 
               resourceType == ResourceType.FOOD || 
               resourceType == ResourceType.STONE || 
               resourceType == ResourceType.POPULATION;
    }

    /**
     * Check if the player is eligible for a new era
     * @return true if the player is eligible for a new era
     * @return false if the player is not eligible for a new era
     */
    public boolean isPlayerEligibleForNewEra() {
        return buildingList.stream().allMatch(building ->
                player.getEraAge()
                        .getRequirementsForNextLevel()
                        .get(ConstructionType.getEnumFromConstant(building.getConstructionTypeName())) <= building.getLevel());
    }

    public void setLevel(int level) {
        player.setEraAge(EraAge.getByLevel(level));
        System.out.println("Descoberta Uma Nova ERA! A Era da " + player.getEraAge().getEraName());

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        BuildingAndResourceAvailabilityPerLevel availability = BuildingAndResourceAvailabilityPerLevel
                .getByLevel(player.getEraAge().getLevel());
        
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

    public void sendWorkersToConstructionJob(ConstructionProcess process, Building construction) {
        prepareNeededResources(construction);
        getWorkerAvailable().makeConstruction(process, construction, playerResources, workers);
    }

    public void sendWorkersToSearchJob(ResourceType resourcesToSearch) {
        getWorkerAvailable().searchResources(resourcesToSearch, playerResources);
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

    public Worker getWorkerAvailable() {
        Optional<Worker> worker = workers.stream().filter(x -> !x.isOccupied()).findFirst();
        if(worker.isPresent()) {
            return worker.get();
        } else {
            System.out.println("Todos os seus trabalhadores estão ocupados com tarefas!");
            System.out.println("Crie mais trabalhadores.");
            return null;
        }
    }

    public void checkForNewEraConditions() {
        if(isPlayerEligibleForNewEra()) {
            int nextLevel = player.getEraAge().getNextLevel().getLevel();
            setLevel(nextLevel);
        }
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
        List<ResourceAmount> rewards = dailyRewardService.claimDailyReward(player.getFarmName(), player.getEraAge());
        rewards.forEach(this::addResourceFromReward);
        System.out.println("Recompensa diária coletada!");
        rewards.forEach(reward -> System.out.printf("%s: +%d\n", reward.getResource().getDescription(), reward.getAmount()));
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
}
