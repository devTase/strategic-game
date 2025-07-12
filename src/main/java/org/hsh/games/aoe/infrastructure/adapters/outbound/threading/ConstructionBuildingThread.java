package org.hsh.games.aoe.infrastructure.adapters.outbound.threading;

import org.hsh.games.aoe.shared.constants.ApplicationConstants;
import org.hsh.games.aoe.domain.entities.CyberOperative;
import org.hsh.games.aoe.domain.entities.buildings.Building;
import org.hsh.games.aoe.shared.utils.ConsoleUtils;
import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;

import java.util.List;

public class ConstructionBuildingThread extends Thread {

    private Building building;
    private CyberOperative operative;
    private List<ResourceAmount> playerResources;
    private List<CyberOperative> playerOperativesList;

    public ConstructionBuildingThread(Building building, CyberOperative operative, List<ResourceAmount> playerResources, List<CyberOperative> playerOperativesList) {
        this.building = building;
        this.operative = operative;
        this.playerResources = playerResources;
        this.playerOperativesList = playerOperativesList;
    }

    @Override
    public void run() {
        operative.setOccupied(true);
        System.out.printf("O operativo cyber começou a tarefa:\n %s\nTermina dentro de %d minutos\n", operative.getCurrentMission(), building.getConstructionMinutes());

        building.build(playerResources, playerOperativesList);

        operative.setOccupied(false);
        System.out.printf("\nO operativo cyber terminou a tarefa: %s\n", operative.getCurrentMission());

        try {
            Thread.sleep(ApplicationConstants.TIME_TO_SHOW_MESSAGE);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ConsoleUtils.clearConsole();
    }
}
