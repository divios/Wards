package io.github.divios.wards.observer;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.misc.EventListener;
import org.bukkit.event.EventPriority;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class BlockPlaceEvent extends abstractObserver implements IObservable{

    protected BlockPlaceEvent() {
        super();
    }

    @Override
    protected EventListener initListener() {
        return new EventListener<>(plugin, org.bukkit.event.block.BlockPlaceEvent.class,
                EventPriority.HIGHEST, o -> {
                NBTItem item = new NBTItem(o.getItemInHand());
                if (!item.hasKey(WARD_META)) return;

                o.getBlockPlaced().setMetadata(WARD_BLOCK,
                        new FixedMetadataValue(plugin, item.getString(WARD_META)));

                updateAll(this, o);
        });
    }
}
