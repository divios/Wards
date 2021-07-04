package io.github.divios.wards.observer;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.wards.Wards;
import org.bukkit.event.EventPriority;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockPlaceEvent extends abstractObserver implements IObservable{

    protected BlockPlaceEvent() {
        super();
    }

    @Override
    protected EventListener initListener() {
        return new EventListener<>(plugin, org.bukkit.event.block.BlockPlaceEvent.class,
                EventPriority.HIGHEST, o -> {

                NBTItem item = new NBTItem(o.getItemInHand());
                if (!item.hasKey(Wards.WARD_META)) return;

                updateAll(this, o);
        });
    }
}
