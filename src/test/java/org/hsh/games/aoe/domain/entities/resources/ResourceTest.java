package org.hsh.games.aoe.domain.entities.resources;

import org.hsh.games.aoe.domain.entities.Difficulty;
import org.hsh.games.aoe.domain.valueobjects.ResourceQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Resource entity
 * 
 * @author devTASE
 */
@DisplayName("Resource Entity Tests")
class ResourceTest {

    @Test
    @DisplayName("Should create resource with valid resource type")
    void shouldCreateResourceWithValidResourceType() {
        // Given
        ResourceType resourceType = ResourceType.ENERGY;

        // When
        Resource resource = new Resource(resourceType);

        // Then
        assertNotNull(resource);
        assertEquals(resourceType, resource.getType());
        assertEquals(resourceType.getDescription(), resource.getName());
        assertEquals(resourceType.getHardToGet(), resource.getHardToGet());
        assertEquals(resourceType.getTimeLimitForSearch(), resource.getTimeLimitForSearch());
        assertEquals(resourceType.getAmountMaxToBeFound(), resource.getAmountMaxToBeFound());
        assertEquals(resourceType.getPricePerUnit(), resource.getPricePerUnit());
    }

    @Test
    @DisplayName("Should have correct properties for different resource types")
    void shouldHaveCorrectPropertiesForDifferentResourceTypes() {
        // Given
        Resource energyResource = new Resource(ResourceType.ENERGY);
        Resource quantumResource = new Resource(ResourceType.QUANTUM_ENERGY);

        // When & Then
        assertEquals("Energy Cells", energyResource.getName());
        assertEquals(Difficulty.EASY, energyResource.getHardToGet());
        assertEquals(5, energyResource.getPricePerUnit());
        
        assertEquals("Quantum Energy", quantumResource.getName());
        assertEquals(Difficulty.EXTREME, quantumResource.getHardToGet());
        assertEquals(50, quantumResource.getPricePerUnit());
    }

    @Test
    @DisplayName("Should get amount from resource")
    void shouldGetAmountFromResource() {
        // Given
        Resource resource = new Resource(ResourceType.COMPONENTS);

        // When
        int amount = resource.getAmount();

        // Then
        assertEquals(ResourceType.COMPONENTS.getAmountMaxToBeFound(), amount);
    }

    @Test
    @DisplayName("Should subtract resource quantity successfully")
    void shouldSubtractResourceQuantitySuccessfully() {
        // Given
        Resource resource = new Resource(ResourceType.DATA);
        ResourceQuantity quantity = new ResourceQuantity(ResourceType.DATA, 50);

        // When
        Resource newResource = resource.subtract(quantity);

        // Then
        assertNotNull(newResource);
        assertEquals(ResourceType.DATA, newResource.getType());
        assertEquals(ResourceType.DATA.getAmountMaxToBeFound() - 50, newResource.getAmount());
    }

    @Test
    @DisplayName("Should throw exception when subtracting different resource type")
    void shouldThrowExceptionWhenSubtractingDifferentResourceType() {
        // Given
        Resource resource = new Resource(ResourceType.ENERGY);
        ResourceQuantity quantity = new ResourceQuantity(ResourceType.DATA, 50);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> resource.subtract(quantity));
    }

    @Test
    @DisplayName("Should throw exception when subtracting more than available")
    void shouldThrowExceptionWhenSubtractingMoreThanAvailable() {
        // Given
        Resource resource = new Resource(ResourceType.CIRCUITS);
        ResourceQuantity quantity = new ResourceQuantity(ResourceType.CIRCUITS, 1000);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> resource.subtract(quantity));
    }

    @Test
    @DisplayName("Should check if has enough resources")
    void shouldCheckIfHasEnoughResources() {
        // Given
        Resource resource = new Resource(ResourceType.COMPONENTS);
        ResourceQuantity sufficientQuantity = new ResourceQuantity(ResourceType.COMPONENTS, 50);
        ResourceQuantity insufficientQuantity = new ResourceQuantity(ResourceType.COMPONENTS, 200);

        // When
        boolean hasSufficient = resource.hasEnough(sufficientQuantity);
        boolean hasInsufficient = resource.hasEnough(insufficientQuantity);

        // Then
        assertTrue(hasSufficient);
        assertFalse(hasInsufficient);
    }

    @Test
    @DisplayName("Should return false when checking different resource type")
    void shouldReturnFalseWhenCheckingDifferentResourceType() {
        // Given
        Resource resource = new Resource(ResourceType.ENERGY);
        ResourceQuantity differentTypeQuantity = new ResourceQuantity(ResourceType.DATA, 10);

        // When
        boolean hasEnough = resource.hasEnough(differentTypeQuantity);

        // Then
        assertFalse(hasEnough);
    }

    @Test
    @DisplayName("Should add resource quantity to player resources")
    void shouldAddResourceQuantityToPlayerResources() {
        // Given
        Resource resource = new Resource(ResourceType.NANOMATERIALS);
        ResourceQuantity quantity = new ResourceQuantity(ResourceType.NANOMATERIALS, 10);
        int originalAmount = resource.getAmount();

        // When
        resource.addToPlayerResources(quantity);

        // Then
        assertEquals(originalAmount + 10, resource.getAmount());
    }

    @Test
    @DisplayName("Should throw exception when adding different resource type")
    void shouldThrowExceptionWhenAddingDifferentResourceType() {
        // Given
        Resource resource = new Resource(ResourceType.CRYPTO);
        ResourceQuantity quantity = new ResourceQuantity(ResourceType.ENERGY, 5);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> resource.addToPlayerResources(quantity));
    }

    @Test
    @DisplayName("Should add to player resources list correctly")
    void shouldAddToPlayerResourcesListCorrectly() {
        // Given
        Resource resource = new Resource(ResourceType.DATA);
        List<ResourceAmount> playerResources = new ArrayList<>();
        playerResources.add(new ResourceAmount(ResourceType.DATA, 100));
        ResourceAmount resourcesToAdd = new ResourceAmount(ResourceType.DATA, 50);

        // When
        resource.addToPlayerResources(playerResources, resourcesToAdd);

        // Then
        assertEquals(150, playerResources.get(0).getAmount());
    }

    @Test
    @DisplayName("Should determine if player worker is killed based on difficulty")
    void shouldDetermineIfPlayerWorkerIsKilledBasedOnDifficulty() {
        // Given
        Resource easyResource = new Resource(ResourceType.ENERGY);
        Resource extremeResource = new Resource(ResourceType.CRYPTO);

        // When
        boolean easyKilled = easyResource.isPLayerWorkerKilled();
        // Note: extreme resource killing is random, so we just test it doesn't throw

        // Then
        assertFalse(easyKilled); // Easy resources never kill workers
        assertDoesNotThrow(extremeResource::isPLayerWorkerKilled);
    }

    @Test
    @DisplayName("Should handle player worker injury based on difficulty")
    void shouldHandlePlayerWorkerInjuryBasedOnDifficulty() {
        // Given
        Resource easyResource = new Resource(ResourceType.DATA);
        Resource hardResource = new Resource(ResourceType.NANOMATERIALS);

        // When & Then
        assertDoesNotThrow(easyResource::isPLayerWorkerInjured);
        assertDoesNotThrow(hardResource::isPLayerWorkerInjured);
    }

    @Test
    @DisplayName("Should set and get resource properties")
    void shouldSetAndGetResourceProperties() {
        // Given
        Resource resource = new Resource(ResourceType.COMPONENTS);
        String newName = "Modified Components";
        Difficulty newDifficulty = Difficulty.HARD;

        // When
        resource.setName(newName);
        resource.setHardToGet(newDifficulty);

        // Then
        assertEquals(newName, resource.getName());
        assertEquals(newDifficulty, resource.getHardToGet());
    }

    @Test
    @DisplayName("Should set and get timing properties")
    void shouldSetAndGetTimingProperties() {
        // Given
        Resource resource = new Resource(ResourceType.CIRCUITS);
        int newTimeLimit = 5000;
        int newMaxAmount = 75;
        int newPrice = 20;

        // When
        resource.setTimeLimitForSearch(newTimeLimit);
        resource.setAmountMaxToBeFound(newMaxAmount);
        resource.setPricePerUnit(newPrice);

        // Then
        assertEquals(newTimeLimit, resource.getTimeLimitForSearch());
        assertEquals(newMaxAmount, resource.getAmountMaxToBeFound());
        assertEquals(newPrice, resource.getPricePerUnit());
    }
}
