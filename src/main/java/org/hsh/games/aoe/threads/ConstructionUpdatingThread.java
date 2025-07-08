package org.hsh.games.aoe.threads;

import org.hsh.games.aoe.ApplicationConstants;
import org.hsh.games.aoe.entities.Building;
import org.hsh.games.aoe.utils.ConsoleUtils;
import org.hsh.games.aoe.entities.ResourceAmount;
import org.hsh.games.aoe.entities.CyberOperative;

import java.util.List;

public class ConstructionUpdatingThread extends Thread {

    private Building building;
    private CyberOperative operative;
    private List<ResourceAmount> playerResources;

    public ConstructionUpdatingThread(Building building, CyberOperative operative, List<ResourceAmount> playerResources) {
        this.building = building;
        this.operative = operative;
        this.playerResources = playerResources;
    }

    @Override
    public void run() {
        operative.setOccupied(true);
        System.out.println("O operativo cyber " + " comecou a tarefa:\n" + operative.getCurrentMission() + "\nTermina dentro de " + building.getConstructionMinutes() + " minutos.");

        this.building.upgrade();

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
