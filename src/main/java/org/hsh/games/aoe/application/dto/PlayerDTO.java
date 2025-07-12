package org.hsh.games.aoe.application.dto;

import org.hsh.games.aoe.domain.entities.TechPhase;

/**
 * Data Transfer Object for Player information
 * 
 * @author devTASE
 */
public record PlayerDTO(
    String playerId,
    String farmName,
    TechPhase techPhase,
    String guildId,
    boolean isInGuild
) {}
