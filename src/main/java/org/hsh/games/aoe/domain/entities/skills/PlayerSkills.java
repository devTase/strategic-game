package org.hsh.games.aoe.domain.entities.skills;

import org.hsh.games.aoe.domain.entities.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerSkills {
    private final Player player;
    private final Map<SkillType, Skill> skills;
    private SkillUpgradeProcess currentUpgradeProcess;

    public PlayerSkills(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.player = player;
        this.skills = new HashMap<>();
        initializeSkills();
    }

    private void initializeSkills() {
        // Initialize all skills at level 1
        skills.put(SkillType.CONSTRUCTION_SPEED, new Skill(
            SkillType.CONSTRUCTION_SPEED, 
            1, 
            "Increases construction speed", 
            0.05 // 5% per level
        ));
        
        skills.put(SkillType.CONSTRUCTION_COST, new Skill(
            SkillType.CONSTRUCTION_COST, 
            1, 
            "Reduces construction cost", 
            0.03 // 3% per level
        ));
        
        skills.put(SkillType.PLUNDER_BONUS, new Skill(
            SkillType.PLUNDER_BONUS, 
            1, 
            "Increases plunder bonus", 
            0.04 // 4% per level
        ));
    }

    public Player getPlayer() {
        return player;
    }

    public Map<SkillType, Skill> getSkills() {
        return new HashMap<>(skills);
    }

    public Skill getSkill(SkillType skillType) {
        return skills.get(skillType);
    }

    public void updateSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null");
        }
        skills.put(skill.getType(), skill);
    }

    /**
     * Returns the time multiplier for construction speed.
     * Formula: 1 - effect (faster construction)
     */
    public double getTimeMultiplier() {
        Skill constructionSpeedSkill = skills.get(SkillType.CONSTRUCTION_SPEED);
        if (constructionSpeedSkill != null) {
            return 1.0 - constructionSpeedSkill.getTotalEffect();
        }
        return 1.0;
    }

    /**
     * Returns the cost multiplier for construction cost.
     * Formula: 1 - effect (cheaper construction)
     */
    public double getCostMultiplier() {
        Skill constructionCostSkill = skills.get(SkillType.CONSTRUCTION_COST);
        if (constructionCostSkill != null) {
            return 1.0 - constructionCostSkill.getTotalEffect();
        }
        return 1.0;
    }

    /**
     * Returns the loot multiplier for plunder bonus.
     * Formula: 1 + effect (more loot)
     */
    public double getLootMultiplier() {
        Skill plunderBonusSkill = skills.get(SkillType.PLUNDER_BONUS);
        if (plunderBonusSkill != null) {
            return 1.0 + plunderBonusSkill.getTotalEffect();
        }
        return 1.0;
    }
    
    /**
     * Gets the current skill upgrade process.
     * @return Current upgrade process or null if no upgrade is running
     */
    public SkillUpgradeProcess getCurrentUpgradeProcess() {
        return currentUpgradeProcess;
    }
    
    /**
     * Sets the current skill upgrade process.
     * @param upgradeProcess The upgrade process to set
     */
    public void setCurrentUpgradeProcess(SkillUpgradeProcess upgradeProcess) {
        this.currentUpgradeProcess = upgradeProcess;
    }
}
