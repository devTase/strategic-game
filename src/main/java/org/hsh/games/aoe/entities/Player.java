package org.hsh.games.aoe.entities;

public class Player {

    private String farmName;
    private EraAge eraAge;
    private static final int MIN_NAME_LENGTH = 3;

    public Player(String farmName) {
        validateFarmName(farmName);
        this.farmName = farmName;
        this.eraAge = EraAge.getByLevel(1);
    }

    private void validateFarmName(String farmName) {
        if (farmName == null || farmName.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da aldeia não pode estar vazio");
        }
        if (farmName.trim().length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("O nome da aldeia deve ter pelo menos " + MIN_NAME_LENGTH + " caracteres");
        }
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        validateFarmName(farmName);
        this.farmName = farmName;
    }

    public void setEraAge(EraAge eraAge) {
        this.eraAge = eraAge;
    }

    public EraAge getEraAge() {
        return eraAge;
    }
}
