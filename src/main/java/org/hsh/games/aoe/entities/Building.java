package org.hsh.games.aoe.entities;

import org.hsh.games.aoe.threads.ResourceGeneratorThread;
import org.hsh.games.aoe.utils.ThreadUtils;
import org.hsh.games.aoe.services.PlayerService;

import java.util.List;

public class Building {

    private boolean isBuilt;
    private int level;
    private int maxLevel;
    private String constructionTypeName;
    private int amountConstructionsAllowed;
    private int constructionMinutes;
    private List<ResourceAmount> resourceCost;
    private List<ResourceAmount> resourceProduction;
    private final int timeBetweenProductions;

    public Building(boolean isBuilt, ConstructionType constructionType) {
        this.isBuilt = isBuilt;
        this.level = 0;
        this.constructionTypeName = constructionType.getName();
        this.constructionMinutes = constructionType.getBaseConstructionTime();
        this.resourceProduction = constructionType.getBaseResourcesProduction();
        this.amountConstructionsAllowed = constructionType.getAmountConstructionsAllowed();
        this.resourceCost = constructionType.getBaseResourcesCost();
        this.maxLevel = constructionType.getMaxLevel();
        this.timeBetweenProductions = constructionType.getBaseProductionTime();
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    public void setBuilt(boolean built) {
        isBuilt = built;
    }

    public int getTimeBetweenProductions() {
        return timeBetweenProductions;
    }

    public void build(List<ResourceAmount> playerResources, List<CyberOperative> playerOperativesList, PlayerService playerService) {
        if(amountConstructionsAllowed > 0) {
            System.out.println("A retirar recursos necessários do teu inventário...");
            
            // Apply skill-modified construction time
            int skillModifiedTime = (int)(constructionMinutes * playerService.getConstructionTimeMultiplier());
            sleep(skillModifiedTime);
            
            level = 1;
            isBuilt = true;
            amountConstructionsAllowed--;
            
            // Apply skill-modified resource cost
            deductResourcesWithSkillModifier(playerResources, playerService.getConstructionCostMultiplier());
            
            increaseResourceCost();
            startThreadForProductions(playerResources, playerOperativesList);
        } else {
            System.out.println("Já construiste o máximo deste tipo de edificio");
        }
    }

    public void upgrade(PlayerService playerService) {
        if(level < maxLevel) {
            // Apply skill-modified construction time
            int skillModifiedTime = (int)(constructionMinutes * playerService.getConstructionTimeMultiplier());
            sleep(skillModifiedTime);
            
            level++;
            constructionMinutes += level * 2;
            increaseResourceCost();
            increaseResourceProductions();
        }
        checkIfPlayerIsEligibleToNextEra();
    }
    
    /**
     * Backward compatibility method for upgrade without skill modifiers.
     * @deprecated Use upgrade(PlayerService) instead for skill integration
     */
    @Deprecated
    public void upgrade() {
        if(level < maxLevel) {
            sleep(getConstructionTimeInMils());
            level++;
            constructionMinutes += level * 2;
            increaseResourceCost();
            increaseResourceProductions();
        }
        checkIfPlayerIsEligibleToNextEra();
    }
    
    /**
     * Backward compatibility method for build without skill modifiers.
     * @deprecated Use build(List, List, PlayerService) instead for skill integration
     */
    @Deprecated
    public void build(List<ResourceAmount> playerResources, List<CyberOperative> playerOperativesList) {
        if(amountConstructionsAllowed > 0) {
            System.out.println("A retirar recursos necessários do teu inventário...");
            sleep(getConstructionTimeInMils());
            level = 1;
            isBuilt = true;
            amountConstructionsAllowed--;
            increaseResourceCost();
            startThreadForProductions(playerResources, playerOperativesList);
        } else {
            System.out.println("Já construiste o máximo deste tipo de edificio");
        }
    }

    private void checkIfPlayerIsEligibleToNextEra() {
        // What do i need here to increase the era level?
        // i need the player object to have a method to increase the era level
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println("Construção Falhou: " + getConstructionTypeName());
            throw new RuntimeException(e);
        }
    }

    public void startThreadForProductions(List<ResourceAmount> playerResources, List<CyberOperative> playerOperativesList) {
        boolean hasConstructionProductions = timeBetweenProductions > 0;
        if(hasConstructionProductions) {
            new ResourceGeneratorThread(this, playerResources, playerOperativesList).start();
        }
    }

    private void increaseResourceCost() {
        resourceCost.forEach(resourceAmount -> resourceAmount.setAmount(resourceAmount.getAmount() * (level+1)));
    }
    
    /**
     * Deducts construction resources from player inventory with skill-based cost reduction.
     * @param playerResources Player's current resources
     * @param costMultiplier Skill-based cost multiplier (less than 1.0 means cheaper)
     */
    private void deductResourcesWithSkillModifier(List<ResourceAmount> playerResources, double costMultiplier) {
        for (ResourceAmount buildingResource : resourceCost) {
            for (ResourceAmount playerResource : playerResources) {
                if(playerResource.getResource() == buildingResource.getResource()) {
                    int adjustedCost = (int)(buildingResource.getAmount() * costMultiplier);
                    playerResource.setAmount(playerResource.getAmount() - adjustedCost);
                }
            }
        }
    }

    private void increaseResourceProductions() {
        resourceProduction.forEach(resourceAmount -> resourceAmount.setAmount(resourceAmount.getAmount() * level));
    }

    public void showDetails() {
        System.out.println(getConstructionTypeName() + " Nivel: " + getLevel());
        System.out.println("-----------------------------------------------");
        System.out.println("Custo de recursos para construção:");
        for (ResourceAmount resourceAmount : resourceCost) {
            System.out.printf("%-10s: %d%n", resourceAmount.getResource().getDescription(), resourceAmount.getAmount());
        }

        System.out.println("Tempo de construção: " + getConstructionMinutes() + " minutos");

        System.out.println("Produção de recursos por minuto:");
        if(resourceProduction.isEmpty()) {
            System.out.println("Sem Produção por agora");
        } else {
            for (ResourceAmount resourceAmount : resourceProduction) {
                System.out.println(resourceAmount.getResource() + ": " + resourceAmount.getAmount());
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public Boolean getBuilded() {
        return isBuilt;
    }

    public int getConstructionMinutes() {
        return ThreadUtils.toMinutes(constructionMinutes);
    }

    public int getConstructionTimeInMils() {
        return constructionMinutes;
    }

    public void setConstructionMinutes(int constructionMinutes) {
        this.constructionMinutes = constructionMinutes;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setResourceCost(List<ResourceAmount> resourceCost) {
        this.resourceCost = resourceCost;
    }

    public List<ResourceAmount> getResourceProduction() {
        return resourceProduction;
    }

    public void setResourceProduction(List<ResourceAmount> resourceProduction) {
        this.resourceProduction = resourceProduction;
    }

    public String getConstructionTypeName() {
        return constructionTypeName;
    }

    public void setConstructionTypeName(String constructionTypeName) {
        this.constructionTypeName = constructionTypeName;
    }

    public List<ResourceAmount> getResourceCost() {
        return resourceCost;
    }

    public int getAmountConstructionsAllowed() {
        return amountConstructionsAllowed;
    }

    public void setAmountConstructionsAllowed(int amountConstructionsAllowed) {
        this.amountConstructionsAllowed = amountConstructionsAllowed;
    }
}

