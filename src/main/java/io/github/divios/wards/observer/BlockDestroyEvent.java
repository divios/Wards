package io.github.divios.wards.observer;

import io.github.divios.core_lib.misc.EventListener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockDestroyEvent extends abstractObserver implements IObservable {

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
                EventPriority.HIGHEST, o -> {
            o.blockList().removeIf(block -> block.hasMetadata(WARD_BLOCK));
        });
    }
}
