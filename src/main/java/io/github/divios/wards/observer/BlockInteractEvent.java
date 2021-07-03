package io.github.divios.wards.observer;

import io.github.divios.core_lib.misc.EventListener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockInteractEvent extends abstractObserver implements IObservable{

    protected BlockInteractEvent() {
        super();
    }

    @Override
    protected EventListener initListener() {
        return new EventListener<>(plugin, PlayerInteractEvent.class, EventPriority.HIGHEST,
                o -> {
                    if (o.getClickedBlock() == null) return;
                    if (!o.getClickedBlock().hasMetadata(WARD_BLOCK)) return;

                    updateAll(this, o);
                });
    }
}
