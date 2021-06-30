package io.github.divios.wards.observer;


import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.Ward;

import java.util.HashSet;
import java.util.Set;

public abstract class abstractObserver {

    protected static final Wards plugin = Wards.getInstance();

    private final Set<IObserver> subscribers = new HashSet<>();
    private final EventListener listener;

    public static final String WARD_META = Wards.WARD_META;
    public static final String WARD_BLOCK = Wards.WARD_BLOCK;

    protected abstractObserver() {
        this.listener = initListener();
    }

    public void subscribe(IObserver subscriber) { subscribers.add(subscriber); }

    public void unsubscribe(IObserver subscriber) { subscribers.remove(subscriber); }

    protected void updateAll(IObservable observable, Object object) {
        subscribers.forEach(iObserver -> iObserver.update(observable, object));
    }

    protected abstract EventListener initListener();

    public void destroy() {
        listener.unregister();
        subscribers.clear();
    }

}
