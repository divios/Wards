package io.github.divios.wards.observer;

import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.confirmIH;
import io.github.divios.wards.Wards;
import io.github.divios.wards.events.WardInteractEvent;
import io.github.divios.wards.events.WardRemoveEvent;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockInteractEvent extends abstractObserver {

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

                    Player p = o.getPlayer();
                    Ward ward = WardsManager.getInstance().getWard(o.getClickedBlock().getLocation());

                    if (ward == null) return;
                    o.setCancelled(true);

                    if (!ward.getAcceptedP().contains(p.getUniqueId())) {
                        return;
                    }

                    WardInteractEvent event = new WardInteractEvent(p, ward);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) return;

                    if (o.getPlayer().isSneaking()) {
                        new confirmIH(plugin, p, (player, aBoolean) -> {
                            if (aBoolean) {

                                WardsManager.getInstance().deleteWard(ward);
                                ItemUtils.give(p, ward.buildItem(), 1);
                                utils.cleanBlock(ward.getCenter());
                                Msg.sendMsg(p, Msg.singletonMsg(Wards.langValues.WARD_PICK_UP)
                                        .add("\\{ward}", ward.getName()).build());
                                player.spawnParticle(Particle.FLAME,
                                        ward.getCenter().clone().add(0.5, 0.5, 0.5), 40);

                                Bukkit.getPluginManager().callEvent(new WardRemoveEvent(p, ward));

                            }
                            p.closeInventory();
                        }, ward.getType().buildItem(), Wards.guiValues.CONFIRM_TITLE,
                                Wards.guiValues.CONFIRM_YES, Wards.guiValues.CONFIRM_NO);
                    }

                    else ward.openInv(o.getPlayer());
                });
    }
}
