package io.github.divios.wards.observer;

public class ObservablesManager {

    private static ObservablesManager INSTANCE = null;

    private final BlockDestroyEvent destroyEvent = new BlockDestroyEvent();
    private final BlockInteractEvent interactEvent = new BlockInteractEvent();
    private final BlockPlaceEvent placeEvent = new BlockPlaceEvent();

    public static ObservablesManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ObservablesManager();
        return INSTANCE;
    }

    public void sToInteract(IObserver observer) {
        interactEvent.subscribe(observer);
    }

    public void unToInteract(IObserver observer) {
        interactEvent.unsubscribe(observer);
    }

    public void sToPlaceEvent(IObserver observer) {
        placeEvent.subscribe(observer);
    }

    public void unToPlaceEvent(IObserver observer) {
        placeEvent.unsubscribe(observer);
    }



}
