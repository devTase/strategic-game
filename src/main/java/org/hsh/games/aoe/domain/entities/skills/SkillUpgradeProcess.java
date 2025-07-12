package org.hsh.games.aoe.domain.entities.skills;

import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkillUpgradeProcess {
    private final SkillType skillType;
    private final int fromLevel;
    private final int toLevel;
    private final long startEpochMillis;
    private final long durationMillis;
    private final List<ResourceAmount> requiredResources;
    private boolean finished;

    private SkillUpgradeProcess(Builder builder) {
        this.skillType = builder.skillType;
        this.fromLevel = builder.fromLevel;
        this.toLevel = builder.toLevel;
        this.startEpochMillis = builder.startEpochMillis;
        this.durationMillis = builder.durationMillis;
        this.requiredResources = new ArrayList<>(builder.requiredResources);
        this.finished = builder.finished;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public int getFromLevel() {
        return fromLevel;
    }

    public int getToLevel() {
        return toLevel;
    }

    public long getStartEpochMillis() {
        return startEpochMillis;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public List<ResourceAmount> getRequiredResources() {
        return new ArrayList<>(requiredResources);
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public long getEndEpochMillis() {
        return startEpochMillis + durationMillis;
    }

    public boolean isUpgradeComplete() {
        return finished || System.currentTimeMillis() >= getEndEpochMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillUpgradeProcess that = (SkillUpgradeProcess) o;
        return fromLevel == that.fromLevel &&
               toLevel == that.toLevel &&
               startEpochMillis == that.startEpochMillis &&
               durationMillis == that.durationMillis &&
               finished == that.finished &&
               skillType == that.skillType &&
               Objects.equals(requiredResources, that.requiredResources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillType, fromLevel, toLevel, startEpochMillis, durationMillis, requiredResources, finished);
    }

    @Override
    public String toString() {
        return "SkillUpgradeProcess{" +
                "skillType=" + skillType +
                ", fromLevel=" + fromLevel +
                ", toLevel=" + toLevel +
                ", startEpochMillis=" + startEpochMillis +
                ", durationMillis=" + durationMillis +
                ", requiredResources=" + requiredResources +
                ", finished=" + finished +
                '}';
    }

    public static class Builder {
        private SkillType skillType;
        private int fromLevel;
        private int toLevel;
        private long startEpochMillis;
        private long durationMillis;
        private List<ResourceAmount> requiredResources = new ArrayList<>();
        private boolean finished = false;

        public Builder skillType(SkillType skillType) {
            this.skillType = skillType;
            return this;
        }

        public Builder fromLevel(int fromLevel) {
            this.fromLevel = fromLevel;
            return this;
        }

        public Builder toLevel(int toLevel) {
            this.toLevel = toLevel;
            return this;
        }

        public Builder startEpochMillis(long startEpochMillis) {
            this.startEpochMillis = startEpochMillis;
            return this;
        }

        public Builder durationMillis(long durationMillis) {
            this.durationMillis = durationMillis;
            return this;
        }

        public Builder requiredResources(List<ResourceAmount> requiredResources) {
            this.requiredResources = requiredResources != null ? new ArrayList<>(requiredResources) : new ArrayList<>();
            return this;
        }

        public Builder finished(boolean finished) {
            this.finished = finished;
            return this;
        }

        public SkillUpgradeProcess build() {
            validateBuildParameters();
            return new SkillUpgradeProcess(this);
        }

        private void validateBuildParameters() {
            if (skillType == null) {
                throw new IllegalArgumentException("Skill type cannot be null");
            }
            if (fromLevel < 1 || fromLevel > 10) {
                throw new IllegalArgumentException("From level must be between 1 and 10, got: " + fromLevel);
            }
            if (toLevel < 1 || toLevel > 10) {
                throw new IllegalArgumentException("To level must be between 1 and 10, got: " + toLevel);
            }
            if (toLevel != fromLevel + 1) {
                throw new IllegalArgumentException("To level must be exactly fromLevel + 1. From: " + fromLevel + ", To: " + toLevel);
            }
            if (startEpochMillis < 0) {
                throw new IllegalArgumentException("Start epoch millis cannot be negative");
            }
            if (durationMillis <= 0) {
                throw new IllegalArgumentException("Duration millis must be positive");
            }
            if (requiredResources == null) {
                throw new IllegalArgumentException("Required resources cannot be null");
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
