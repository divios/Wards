package io.github.divios.wards.observer;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
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
    protected SingleSubscription initListener() {


        return Events.subscribe(org.bukkit.event.block.BlockPlaceEvent.class, EventPriority.HIGHEST)
                .handler(o -> {

                    NBTItem item = new NBTItem(o.getItemInHand());
                    if (!item.hasKey(Wards.WARD_META)) return;

                    String id = item.getString(Wards.WARD_ID);
                    Player p = o.getPlayer();
                    Block block = o.getBlockPlaced();
                    Location l = block.getLocation();
                    WardType type = WardsManager.getInstance().getWardType(id);

                    if (type == null) return;

                    Integer limit = utils.getWardsLimit(p);
                    int placed = WardsManager.getInstance().getWards(p).size();

                    if (limit != null && limit >= placed) {
                        Msg.sendMsg(p, Msg.singletonMsg(Wards.configManager.getLangValues().WARD_LIMIT)
                                .add("\\{limit}", String.valueOf(limit)).build());
                        o.setCancelled(true);
                        return;
                    }

                    WardPlaceEvent event = new WardPlaceEvent(p, l, type);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) return;

                    Ward ward = Ward.builder(o.getItemInHand())
                            .setLocation(l)
                            .build();

                    if (!ward.getAcceptedP().contains(p.getUniqueId())) {
                        Msg.sendMsg(p, "&7You are not allowed to place this ward");
                        o.setCancelled(true);
                        return;
                    }

                    IntStream.range(0, 40).forEach(i -> {
                        ParticleUtils.spawnParticlePlace(p, l.clone());
                    });

                    manager.createWard(ward);

                });

    }
}
