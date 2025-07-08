package org.hsh.games.aoe.entities.rebelcell;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RebelCellRank enum.
 * 
 * @author devTASE
 */
class RebelCellRankTest {

    @Test
    void testRankDisplayName() {
        assertEquals("Rebel Hacker", RebelCellRank.HACKER.getDisplayName());
        assertEquals("Rebel Operator", RebelCellRank.OPERATOR.getDisplayName());
        assertEquals("Rebel Cell Leader", RebelCellRank.CELL_LEADER.getDisplayName());
        assertEquals("Rebel Infiltrator", RebelCellRank.INFILTRATOR.getDisplayName());
    }

    @Test
    void testRankEmoji() {
        assertEquals("\uD83D\uDCBB", RebelCellRank.HACKER.getEmoji()); // 💻
        assertEquals("\uD83D\uDCE1", RebelCellRank.OPERATOR.getEmoji()); // 📡
        assertEquals("\uD83D\uDC82\u200D♂️", RebelCellRank.CELL_LEADER.getEmoji()); // 👨‍✈️
        assertEquals("\uD83E\uDD77", RebelCellRank.INFILTRATOR.getEmoji()); // 🥷
    }

    @Test
    void testRankLeadershipPrivileges() {
        assertFalse(RebelCellRank.HACKER.hasLeadershipPrivileges());
        assertFalse(RebelCellRank.OPERATOR.hasLeadershipPrivileges());
        assertTrue(RebelCellRank.CELL_LEADER.hasLeadershipPrivileges());
        assertFalse(RebelCellRank.INFILTRATOR.hasLeadershipPrivileges());
    }

    @Test
    void testRankInfiltrationAbility() {
        assertFalse(RebelCellRank.HACKER.canInfiltrate());
        assertFalse(RebelCellRank.OPERATOR.canInfiltrate());
        assertFalse(RebelCellRank.CELL_LEADER.canInfiltrate());
        assertTrue(RebelCellRank.INFILTRATOR.canInfiltrate());
    }

    @Test
    void testGetByDisplayName() {
        assertEquals(RebelCellRank.HACKER, RebelCellRank.getByDisplayName("Rebel Hacker"));
        assertEquals(RebelCellRank.OPERATOR, RebelCellRank.getByDisplayName("Rebel Operator"));
        assertEquals(RebelCellRank.CELL_LEADER, RebelCellRank.getByDisplayName("Rebel Cell Leader"));
        assertEquals(RebelCellRank.INFILTRATOR, RebelCellRank.getByDisplayName("Rebel Infiltrator"));
    }

    @Test
    void testGetByDisplayNameIgnoresCase() {
        assertEquals(RebelCellRank.HACKER, RebelCellRank.getByDisplayName("rebel hacker"));
        assertEquals(RebelCellRank.OPERATOR, RebelCellRank.getByDisplayName("REBEL OPERATOR"));
        assertEquals(RebelCellRank.CELL_LEADER, RebelCellRank.getByDisplayName("ReBel CeLl LeAdEr"));
    }

    @Test
    void testGetByDisplayNameReturnsNullForInvalidName() {
        assertNull(RebelCellRank.getByDisplayName("Invalid Rank"));
        assertNull(RebelCellRank.getByDisplayName(""));
        assertNull(RebelCellRank.getByDisplayName(null));
    }

    @Test
    void testGetDisplayNameWithEmoji() {
        assertEquals("💻 Rebel Hacker", RebelCellRank.HACKER.getDisplayNameWithEmoji());
        assertEquals("📡 Rebel Operator", RebelCellRank.OPERATOR.getDisplayNameWithEmoji());
        assertEquals("👨‍✈️ Rebel Cell Leader", RebelCellRank.CELL_LEADER.getDisplayNameWithEmoji());
        assertEquals("🥷 Rebel Infiltrator", RebelCellRank.INFILTRATOR.getDisplayNameWithEmoji());
    }

    @Test
    void testRankDescriptions() {
        assertNotNull(RebelCellRank.HACKER.getDescription());
        assertNotNull(RebelCellRank.OPERATOR.getDescription());
        assertNotNull(RebelCellRank.CELL_LEADER.getDescription());
        assertNotNull(RebelCellRank.INFILTRATOR.getDescription());
        
        assertTrue(RebelCellRank.HACKER.getDescription().contains("security"));
        assertTrue(RebelCellRank.OPERATOR.getDescription().contains("logistics"));
        assertTrue(RebelCellRank.CELL_LEADER.getDescription().contains("Leader"));
        assertTrue(RebelCellRank.INFILTRATOR.getDescription().contains("covert"));
    }

    @Test
    void testAllRanksAreDifferent() {
        RebelCellRank[] ranks = RebelCellRank.values();
        assertEquals(4, ranks.length);
        
        for (int i = 0; i < ranks.length; i++) {
            for (int j = i + 1; j < ranks.length; j++) {
                assertNotEquals(ranks[i], ranks[j]);
                assertNotEquals(ranks[i].getRank(), ranks[j].getRank());
                assertNotEquals(ranks[i].getDisplayName(), ranks[j].getDisplayName());
            }
        }
    }
}
