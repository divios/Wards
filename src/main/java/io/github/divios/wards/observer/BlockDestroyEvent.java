package io.github.divios.wards.observer;

import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockDestroyEvent extends abstractObserver {

    protected BlockDestroyEvent() {
        super();
    }

    /**
     * Prevents blocks marked as Wards from exploding
     * @return
     */

    @Override
    protected EventListener initListener() {
        return new EventListener<>(plugin, BlockExplodeEvent.class,
                EventPriority.HIGHEST, o ->
                o.blockList().removeIf(block ->
                        WardsManager.getInstance().getWard(o.getBlock().getLocation()) != null));
    }
}
