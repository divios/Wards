package io.github.divios.wards.observer;

import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockInteractEvent extends abstractObserver implements IObservable {

    protected BlockInteractEvent() {
        super();
    }

    @Override
    protected EventListener initListener() {
        return new EventListener<>(plugin, PlayerInteractEvent.class, EventPriority.HIGHEST,
                o -> {
                    if (o.getClickedBlock() == null) return;
                    if (WardsManager.getInstance()
                                    .getWard(o.getClickedBlock().getLocation()) == null) return;

                    updateAll(this, o);
                });
    }
}
