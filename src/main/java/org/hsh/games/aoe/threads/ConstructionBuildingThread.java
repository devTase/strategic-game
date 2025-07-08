package org.hsh.games.aoe.threads;

import org.hsh.games.aoe.ApplicationConstants;
import org.hsh.games.aoe.entities.Building;
import org.hsh.games.aoe.utils.ConsoleUtils;
import org.hsh.games.aoe.entities.ResourceAmount;
import org.hsh.games.aoe.entities.CyberOperative;

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
