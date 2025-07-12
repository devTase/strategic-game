package org.hsh.games.aoe.infrastructure.adapters.outbound.threading;

import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.domain.entities.buildings.Building;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import java.util.List;

public class ResourceGeneratorThread extends Thread {
    private final Building building;
    private final List<ResourceAmount> playerResources;
    private long sleepDuration;
    private final List<CyberOperative> playerOperativesList;

    public ResourceGeneratorThread(Building building, List<ResourceAmount> playerResources, List<CyberOperative> playerOperativesList) {
        this.building = building;
        this.sleepDuration = building.getTimeBetweenProductions();
        this.playerResources = playerResources;
        this.playerOperativesList = playerOperativesList;
    }

    @Override
    public void run() {
        if(building.getResourceProduction().isEmpty()) {
            return;
        } else {
            System.out.println("A iniciar produção de recursos...");
        }
        while (!isInterrupted()) {
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                return;
            }

            synchronized (this) {
                addResourcesToPlayerInventory();
            }
        }
    }

    private void addResourcesToPlayerInventory() {
        building.getResourceProduction().forEach(resourceAmount -> {
            playerResources.stream()
                    .filter(playerResource -> playerResource.getResource().equals(resourceAmount.getResource()))
                    .forEach(playerResource -> {
                        playerResource.setAmount(playerResource.getAmount() + resourceAmount.getAmount());
                        if(playerResource.getResource() == ResourceType.DATA){
                            addCyberOperativeToPlayerOperativesList();
                        }
                        System.out.println("Adicionado +" + resourceAmount.getAmount() + " de " + resourceAmount.getResource().getDescription() + " ao inventário!");
                    });
        });
    }

    private void addCyberOperativeToPlayerOperativesList() {
        for(ResourceAmount playerResource : playerResources) {
            if(playerResource.getResource() == ResourceType.DATA) {
                if(playerOperativesList.size() < playerResource.getAmount()) {
                    playerOperativesList.add(new CyberOperative("operative_added"));
                } else {
                    System.out.println("Chegaste ao limite de operativos cyber! Atualiza a Central de Comando!");
                    this.interrupt();
                }
            }
        }
    }
}
