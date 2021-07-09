package io.github.divios.wards.observer;

import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.event.SingleSubscription;
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
    protected SingleSubscription<BlockExplodeEvent> initListener() {

       return Events.subscribe(BlockExplodeEvent.class, EventPriority.HIGHEST)
                .handler(o -> o.blockList().removeIf(block ->
                        WardsManager.getInstance().getWard(o.getBlock().getLocation()) != null));

    }
}
