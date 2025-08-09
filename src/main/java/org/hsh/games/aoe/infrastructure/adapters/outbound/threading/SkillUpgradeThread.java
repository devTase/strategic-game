package org.hsh.games.aoe.infrastructure.adapters.outbound.threading;

import org.hsh.games.aoe.domain.entities.skills.PlayerSkills;
import org.hsh.games.aoe.domain.entities.skills.Skill;
import org.hsh.games.aoe.domain.entities.skills.SkillType;
import org.hsh.games.aoe.domain.entities.skills.SkillUpgradeProcess;
import org.hsh.games.aoe.domain.services.PlayerService;

/**
 * Thread responsible for handling skill upgrade processing.
 * Runs for the duration of the skill upgrade and updates the skill level upon completion.
 */
public class SkillUpgradeThread extends Thread {
    
    private final SkillUpgradeProcess upgradeProcess;
    private final PlayerService playerService;
    private Runnable onUpgradeCompleteListener;
    
    public SkillUpgradeThread(SkillUpgradeProcess upgradeProcess, PlayerService playerService) {
        if (upgradeProcess == null) {
            throw new IllegalArgumentException("Upgrade process cannot be null");
        }
        if (playerService == null) {
            throw new IllegalArgumentException("Player service cannot be null");
        }
        
        this.upgradeProcess = upgradeProcess;
        this.playerService = playerService;
        this.setName("SkillUpgrade-" + upgradeProcess.getSkillType().name() + "-Thread");
    }
    
    @Override
    public void run() {
        try {
            // Calculate remaining time if the upgrade was already started
            long currentTime = System.currentTimeMillis();
            long startTime = upgradeProcess.getStartEpochMillis();
            long duration = upgradeProcess.getDurationMillis();
            long endTime = startTime + duration;
            
            if (currentTime < endTime) {
                long remainingTime = endTime - currentTime;
                Thread.sleep(remainingTime);
            }
            
            // Check if thread was interrupted during sleep
            if (!isInterrupted()) {
                completeUpgrade();
            }
            
        } catch (InterruptedException e) {
            // Thread was interrupted, do not complete the upgrade
            System.out.println("Skill upgrade was interrupted for " + upgradeProcess.getSkillType().getDisplayName());
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Completes the skill upgrade by updating the skill level and clearing the upgrade process.
     */
    private void completeUpgrade() {
        synchronized (playerService) {
            try {
                PlayerSkills playerSkills = playerService.getPlayerSkills();
                SkillType skillType = upgradeProcess.getSkillType();
                
                // Get current skill
                Skill currentSkill = playerSkills.getSkill(skillType);
                if (currentSkill == null) {
                    System.err.println("Error: Could not find skill " + skillType.getDisplayName() + " for upgrade completion");
                    return;
                }
                
                // Get description, provide default if null or empty
                String description = currentSkill.description();
                if (description == null || description.trim().isEmpty()) {
                    description = getDefaultDescription(skillType);
                }
                
                // Create upgraded skill
                Skill upgradedSkill = new Skill(
                    skillType,
                    upgradeProcess.getToLevel(),
                    description,
                    currentSkill.baseEffectPerLevel()
                );
                
                // Update skill
                playerSkills.updateSkill(upgradedSkill);
                
                // Mark upgrade as finished and clear from player skills
                upgradeProcess.setFinished(true);
                playerSkills.setCurrentUpgradeProcess(null);
                
                System.out.println("✅ Skill upgrade completed! " + skillType.getDisplayName() + 
                                 " is now level " + upgradedSkill.level());
                
                // Notify completion listener if set
                if (onUpgradeCompleteListener != null) {
                    onUpgradeCompleteListener.run();
                }
                
            } catch (Exception e) {
                System.err.println("Error completing skill upgrade: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Sets a listener to be called when the upgrade completes successfully.
     * @param listener The completion listener
     */
    public void setOnUpgradeCompleteListener(Runnable listener) {
        this.onUpgradeCompleteListener = listener;
    }
    
    /**
     * Gets the upgrade process being handled by this thread.
     * @return The upgrade process
     */
    public SkillUpgradeProcess getUpgradeProcess() {
        return upgradeProcess;
    }
    
    /**
     * Provides default descriptions for skill types when description is missing.
     * @param skillType The skill type
     * @return Default description for the skill type
     */
    private String getDefaultDescription(SkillType skillType) {
        return switch (skillType) {
            case CONSTRUCTION_SPEED -> "Increases construction speed";
            case CONSTRUCTION_COST -> "Reduces construction cost";
            case PLUNDER_BONUS -> "Increases plunder bonus";
        };
    }
}
