package io.github.divios.wards.events;

import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.misc.confirmIH;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class WardInteractEvent extends abstractEvent implements IObserver {

    public WardInteractEvent(WardsManager manager) {
        super(manager);
        OManager.sToInteract(this);
    }

    @Override
    public void update(IObservable observable, Object object) {
        PlayerInteractEvent o = (PlayerInteractEvent) object;

        Player p = o.getPlayer();
        Ward ward = manager.getWard(o.getClickedBlock().getLocation());

        if (ward == null) return;
        o.setCancelled(true);

        if (!ward.getAcceptedP().contains(p.getUniqueId())) {
            Msg.sendMsg(p, "No tienes permiso para interactuar con este ward");
            return;
        }

        if (o.getPlayer().isSneaking()) {
            new confirmIH(plugin, p, (player, aBoolean) -> {
                if (aBoolean) {

                    WardsManager.getInstance().deleteWard(ward);
                    ItemUtils.give(p, ward.buildItem(), 1);
                    utils.cleanBlock(ward.getCenter());
                    Msg.sendMsg(p, "&7Your took your ward " + ward.getName());
                    player.spawnParticle(Particle.FLAME,
                            ward.getCenter().clone().add(0.5, 0.5, 0.5), 40);

                }
                p.closeInventory();
            }, ward.getType().buildItem(), "&8Confirm Action",
                    "&aYES", "&cNO");
        }

        else ward.openInv(o.getPlayer());
    }
}
