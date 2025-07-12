package org.hsh.games.aoe.domain.entities;

import org.hsh.games.aoe.domain.entities.resources.ResourceAmount;

import java.util.List;

public abstract class Consumer<T extends ResourceAmount> {

    public abstract List<T> getConsumptionType();

    public abstract int getTimeBetweenConsumptions();
}
