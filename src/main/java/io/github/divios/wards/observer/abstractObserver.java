package io.github.divios.wards.observer;


import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.wards.Wards;

public abstract class abstractObserver {

    protected static final Wards plugin = Wards.getInstance();

    private final EventListener listener;

    protected abstractObserver() {
        listener = initListener();
    }

    protected abstract EventListener initListener();

    public void destroy() {
        listener.unregister();
    }

}
