package io.github.divios.wards.observer;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.wards.Wards;
import io.github.divios.wards.events.WardInteractEvent;
import io.github.divios.wards.events.WardPlaceEvent;
import io.github.divios.wards.utils.ParticleUtils;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardType;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;

import java.util.UUID;
import java.util.stream.IntStream;

public class BlockPlaceEvent extends abstractObserver {

    protected BlockPlaceEvent() {
        super();
    }

    @Override
    protected EventListener<org.bukkit.event.block.BlockPlaceEvent> initListener() {
        return new EventListener<>(plugin, org.bukkit.event.block.BlockPlaceEvent.class,
                EventPriority.HIGHEST, o -> {

            NBTItem item = new NBTItem(o.getItemInHand());
            if (!item.hasKey(Wards.WARD_META)) return;

            String id = item.getString(Wards.WARD_ID);
            Player p = o.getPlayer();
            Block block = o.getBlockPlaced();
            Location l = block.getLocation();
            WardType type = WardsManager.getInstance().getWardType(id);

            if (type == null) return;

            WardPlaceEvent event = new WardPlaceEvent(p, l, type);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) return;

            String owner = new NBTItem(o.getItemInHand()).getString(Wards.WARD_OWNER);

            utils.setWardMetadata(block, UUID.fromString(owner));

            IntStream.range(0, 40).forEach(i -> {
                ParticleUtils.spawnParticlePlace(p, l.clone());
            });

            WardsManager.getInstance().createWard(new Ward.Builder(o.getItemInHand())
                    .setLocation(l)
                    .build());
        });
    }
}
