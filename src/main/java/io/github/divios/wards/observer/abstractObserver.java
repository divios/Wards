package io.github.divios.wards.observer;


import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.WardsManager;

public abstract class abstractObserver {

    protected static final Wards plugin = Wards.getInstance();
    protected static final WardsManager manager = WardsManager.getInstance();

    private final SingleSubscription listener;

    protected abstractObserver() {
        listener = initListener();
    }

    protected abstract SingleSubscription initListener();

    public void destroy() {
        listener.unregister();
    }

}
