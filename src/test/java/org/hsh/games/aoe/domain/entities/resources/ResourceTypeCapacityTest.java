package org.hsh.games.aoe.domain.entities.resources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTypeCapacityTest {

    @Test
    @DisplayName("Should return correct maximum capacity for each resource type")
    void shouldReturnCorrectMaxCapacity() {
        assertEquals(1000, ResourceType.ENERGY.getMaxCap());
        assertEquals(800, ResourceType.DATA.getMaxCap());
        assertEquals(600, ResourceType.COMPONENTS.getMaxCap());
        assertEquals(400, ResourceType.CIRCUITS.getMaxCap());
        assertEquals(200, ResourceType.NANOMATERIALS.getMaxCap());
        assertEquals(100, ResourceType.QUANTUM_ENERGY.getMaxCap());
        assertEquals(50, ResourceType.CRYPTO.getMaxCap());
    }

    @Test
    @DisplayName("Should return correct minimum gather amount for each resource type")
    void shouldReturnCorrectMinGather() {
        assertEquals(10, ResourceType.ENERGY.getMinGather());
        assertEquals(5, ResourceType.DATA.getMinGather());
        assertEquals(3, ResourceType.COMPONENTS.getMinGather());
        assertEquals(2, ResourceType.CIRCUITS.getMinGather());
        assertEquals(1, ResourceType.NANOMATERIALS.getMinGather());
        assertEquals(1, ResourceType.QUANTUM_ENERGY.getMinGather());
        assertEquals(1, ResourceType.CRYPTO.getMinGather());
    }

    @Test
    @DisplayName("Should return correct maximum gather amount for each resource type")
    void shouldReturnCorrectMaxGather() {
        assertEquals(50, ResourceType.ENERGY.getMaxGather());
        assertEquals(30, ResourceType.DATA.getMaxGather());
        assertEquals(20, ResourceType.COMPONENTS.getMaxGather());
        assertEquals(15, ResourceType.CIRCUITS.getMaxGather());
        assertEquals(10, ResourceType.NANOMATERIALS.getMaxGather());
        assertEquals(5, ResourceType.QUANTUM_ENERGY.getMaxGather());
        assertEquals(3, ResourceType.CRYPTO.getMaxGather());
    }

    @Test
    @DisplayName("Should have min gather less than or equal to max gather for all resource types")
    void shouldHaveMinGatherLessOrEqualToMaxGather() {
        for (ResourceType resourceType : ResourceType.values()) {
            assertTrue(resourceType.getMinGather() <= resourceType.getMaxGather(),
                    "Min gather should be <= max gather for " + resourceType.name());
        }
    }

    @Test
    @DisplayName("Should have capacity limits that make sense relative to gathering amounts")
    void shouldHaveReasonableCapacityLimits() {
        for (ResourceType resourceType : ResourceType.values()) {
            // Max capacity should be significantly larger than max gather amount
            assertTrue(resourceType.getMaxCap() > resourceType.getMaxGather() * 10,
                    "Max capacity should be much larger than max gather for " + resourceType.name());
        }
    }

    @Test
    @DisplayName("Should follow difficulty progression in capacity and gather limits")
    void shouldFollowDifficultyProgression() {
        // Basic resources (EASY) should have higher capacity and gather amounts
        assertTrue(ResourceType.ENERGY.getMaxCap() > ResourceType.CRYPTO.getMaxCap());
        assertTrue(ResourceType.DATA.getMaxCap() > ResourceType.QUANTUM_ENERGY.getMaxCap());
        
        // Gather amounts should decrease with difficulty
        assertTrue(ResourceType.ENERGY.getMaxGather() > ResourceType.CRYPTO.getMaxGather());
        assertTrue(ResourceType.DATA.getMaxGather() > ResourceType.QUANTUM_ENERGY.getMaxGather());
    }
}
