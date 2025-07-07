package org.hsh.games.aoe.entities.achievements;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing the progress of an achievement.
 * 
 * It holds a reference to the Achievement itself, the current progress,
 * a flag indicating if it's unlocked, and the date when it was unlocked.
 */
public class AchievementProgress {
    
    private final Achievement achievement;
    private int currentProgress;
    private final Map<String, Integer> conditionProgress;
    private boolean isUnlocked;
    private LocalDateTime unlockDate;

    /**
     * Constructor for AchievementProgress.
     * 
     * @param achievement the achievement being tracked
     * @param conditionProgress progress by condition key
     */
    public AchievementProgress(Achievement achievement, Map<String, Integer> conditionProgress) {
        if (achievement == null) {
            throw new IllegalArgumentException("Achievement cannot be null");
        }
        this.achievement = achievement;
        this.conditionProgress = (conditionProgress != null) ? 
            Collections.unmodifiableMap(conditionProgress) : Collections.emptyMap();
        this.currentProgress = 0;
        this.isUnlocked = false;
    }
    
    public Achievement getAchievement() {
        return achievement;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        checkUnlockStatus();
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public LocalDateTime getUnlockDate() {
        return unlockDate;
    }

    private void checkUnlockStatus() {
        if (currentProgress >= achievement.getPoints() && !isUnlocked) {
            isUnlocked = true;
            unlockDate = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchievementProgress that = (AchievementProgress) o;
        return achievement.equals(that.achievement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(achievement);
    }
    
    @Override
    public String toString() {
        return "AchievementProgress{" +
                "achievement=" + achievement +
                ", currentProgress=" + currentProgress +
                ", isUnlocked=" + isUnlocked +
                ", unlockDate=" + unlockDate +
                '}';
    }
}

