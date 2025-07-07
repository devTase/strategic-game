package org.hsh.games.aoe.entities;

import java.util.Objects;

public class ResourceAmount {
    private ResourceType resourceType;
    private int amount;

    public ResourceAmount(ResourceType resourceType, int amount) {
        this.resourceType = resourceType;
        this.amount = amount;
    }

    public ResourceType getResource() {
        return resourceType;
    }

    public void setResource(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceAmount that = (ResourceAmount) o;
        return amount == that.amount && resourceType == that.resourceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceType, amount);
    }

    @Override
    public String toString() {
        return "ResourceAmount{" +
                "resource=" + resourceType +
                ", amount=" + amount +
                '}';
    }
}
