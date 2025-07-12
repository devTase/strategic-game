package org.hsh.games.aoe.domain.entities.skills;

import java.util.Objects;

public final class Skill {
    private final SkillType type;
    private final int level;
    private final String description;
    private final double baseEffectPerLevel;

    public Skill(SkillType type, int level, String description, double baseEffectPerLevel) {
        if (type == null) {
            throw new IllegalArgumentException("Skill type cannot be null");
        }
        if (level < 1 || level > 10) {
            throw new IllegalArgumentException("Skill level must be between 1 and 10, got: " + level);
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Skill description cannot be null or empty");
        }
        
        this.type = type;
        this.level = level;
        this.description = description;
        this.baseEffectPerLevel = baseEffectPerLevel;
    }

    public SkillType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public double getBaseEffectPerLevel() {
        return baseEffectPerLevel;
    }

    public double getTotalEffect() {
        return level * baseEffectPerLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return level == skill.level &&
               Double.compare(skill.baseEffectPerLevel, baseEffectPerLevel) == 0 &&
               type == skill.type &&
               Objects.equals(description, skill.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, level, description, baseEffectPerLevel);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "type=" + type +
                ", level=" + level +
                ", description='" + description + '\'' +
                ", baseEffectPerLevel=" + baseEffectPerLevel +
                ", totalEffect=" + getTotalEffect() +
                '}';
    }
}
