package io.github.divios.wards.events;

import io.github.divios.wards.Wards;
import io.github.divios.wards.observer.ObservablesManager;
import io.github.divios.wards.wards.WardsManager;

public abstract class abstractEvent {

    protected static final Wards plugin = Wards.getInstance();
    protected static final ObservablesManager OManager = ObservablesManager.getInstance();

    protected final WardsManager manager;

    public abstractEvent(WardsManager manager) {
        this.manager = manager;

    }

}
