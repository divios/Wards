package io.github.divios.wards.observer;

public class ObservablesManager {

    private static ObservablesManager INSTANCE = null;

    private final BlockDestroyEvent destroyEvent = new BlockDestroyEvent();
    private final BlockInteractEvent interactEvent = new BlockInteractEvent();
    private final BlockPlaceEvent placeEvent = new BlockPlaceEvent();
    private final PotionConsumeEvent potionConsumeEvent = new PotionConsumeEvent();

    public static ObservablesManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ObservablesManager();
        return INSTANCE;
    }

    public BlockDestroyEvent getDestroyEvent() {
        return destroyEvent;
    }

    public BlockInteractEvent getInteractEvent() {
        return interactEvent;
    }

    public BlockPlaceEvent getPlaceEvent() {
        return placeEvent;
    }

    public PotionConsumeEvent getPotionConsumeEvent() {
        return potionConsumeEvent;
    }
}
