package org.hsh.games.aoe.domain.entities.buildings;

public enum ConstructionProcess {
    CREATION("In Construction"),
    UPDATE("In Update");

    String process;

    ConstructionProcess(String process) {
        this.process = process;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
