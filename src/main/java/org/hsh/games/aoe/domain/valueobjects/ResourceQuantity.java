package org.hsh.games.aoe.domain.valueobjects;

import org.hsh.games.aoe.domain.entities.resources.ResourceType;

/**
 * Value object representing a resource quantity with type
 * 
 * @author devTASE
 */
public record ResourceQuantity(ResourceType type, int amount) {
    
    public ResourceQuantity {
        if (amount < 0) {
            throw new IllegalArgumentException("Resource quantity cannot be negative");
        }
    }
    
    public ResourceQuantity add(ResourceQuantity other) {
        if (!this.type.equals(other.type)) {
            throw new IllegalArgumentException("Cannot add different resource types");
        }
        return new ResourceQuantity(this.type, this.amount + other.amount);
    }
    
    public ResourceQuantity subtract(ResourceQuantity other) {
        if (!this.type.equals(other.type)) {
            throw new IllegalArgumentException("Cannot subtract different resource types");
        }
        int result = this.amount - other.amount;
        if (result < 0) {
            throw new IllegalArgumentException("Insufficient resources for operation");
        }
        return new ResourceQuantity(this.type, result);
    }
    
    public boolean isGreaterThan(ResourceQuantity other) {
        return this.amount > other.amount;
    }
    
    public boolean isGreaterThanOrEqual(ResourceQuantity other) {
        return this.amount >= other.amount;
    }
    
    public boolean isEmpty() {
        return amount == 0;
    }
    
    public ResourceType getType() {
        return type;
    }
    
    public int getAmount() {
        return amount;
    }
}
