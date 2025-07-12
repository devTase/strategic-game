package org.hsh.games.aoe.application.dto;

import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;
import org.hsh.games.aoe.domain.entities.resources.ResourceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for ResourceDTO
 */
@DisplayName("ResourceDTO Tests")
public class ResourceDTOTest {

    @Test
    @DisplayName("Should create ResourceDTO from ResourceAmount")
    public void testFromResourceAmount() {
        ResourceDTO resourceDTO = ResourceDTO.from(new ResourceAmount(
                ResourceType.ENERGY, 100));

        assertEquals(ResourceType.ENERGY, resourceDTO.type());
        assertEquals(100, resourceDTO.amount());
        assertEquals("Energy Cells", resourceDTO.description());
    }
}
