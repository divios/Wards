package io.github.divios.wards.events;

import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.observer.IObservable;
import io.github.divios.wards.observer.IObserver;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
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

        ward.openInv(o.getPlayer());
    }
}
