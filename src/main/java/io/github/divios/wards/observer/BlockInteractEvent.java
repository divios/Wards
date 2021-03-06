package io.github.divios.wards.observer;

import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.core_lib.itemutils.ItemUtils;
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
    protected SingleSubscription<PlayerInteractEvent> initListener() {

        return Events.subscribe(PlayerInteractEvent.class, EventPriority.HIGHEST)
                .filter(o -> o.getClickedBlock() != null)
                .filter(o -> manager.getWard(o.getClickedBlock().getLocation()) != null)
                .handler(o -> {

                    Player p = o.getPlayer();
                    Ward ward = manager.getWard(o.getClickedBlock().getLocation());

                    if (ward == null) return;
                    o.setCancelled(true);

                    if (!ward.getTrusted().contains(p.getUniqueId()) && !p.hasPermission("wards.admin")) {
                        return;
                    }

                    WardInteractEvent event = new WardInteractEvent(p, ward);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) return;

                    if (o.getPlayer().isSneaking()) {           // Confirm Menu

                        confirmIH.builder()
                                .withPlayer(p)
                                .withAction(aBoolean -> {

                                    if (aBoolean) {

                                        if (!p.getUniqueId().equals(ward.getOwner())) {
                                            Msg.sendMsg(p, Wards.configManager.getLangValues().WARD_PICK_DENY);
                                            return;
                                        }

                                        WardsManager.getInstance().deleteWard(ward);
                                        ItemUtils.give(p, ward.buildItem(), 1);
                                        utils.cleanBlock(ward.getCenter());
                                        Msg.sendMsg(p, Msg.singletonMsg(Wards.configManager.getLangValues().WARD_PICK_UP)
                                                .add("\\{ward}", ward.getName()).build());
                                        p.spawnParticle(Particle.FLAME,
                                                ward.getCenter().clone().add(0.5, 0.5, 0.5), 40);

                                        Bukkit.getPluginManager().callEvent(new WardRemoveEvent(p, ward));

                                    }
                                    p.closeInventory();

                                })
                                .withItem(ward.getType().buildItem())
                                .withConfirmLore(Wards.configManager.getGuiValues().CONFIRM_YES)
                                .withCancelLore(Wards.configManager.getGuiValues().CONFIRM_NO)
                                .prompt();

                    } else ward.openInv(o.getPlayer());
                });

    }
}
