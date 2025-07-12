package org.hsh.games.aoe.domain.entities.skills;

public enum SkillType {
    CONSTRUCTION_SPEED("Construction Speed"),
    CONSTRUCTION_COST("Construction Cost"),
    PLUNDER_BONUS("Plunder Bonus");

    private final String displayName;

    SkillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
