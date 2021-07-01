package io.github.divios.wards.observer;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.misc.EventListener;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.wards.Wards;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class BlockInteractEvent extends abstractObserver implements IObservable{

    protected BlockInteractEvent() {
        super();
    }

    @Override
    protected EventListener initListener() {
        return new EventListener<>(plugin, PlayerInteractEvent.class, EventPriority.HIGHEST,
                o -> {
                    if (o.getClickedBlock() == null) return;
                    if (!o.getClickedBlock().hasMetadata(WARD_BLOCK)) return;

                    o.setCancelled(true);

                    Block block = o.getClickedBlock();
                    Player p = o.getPlayer();

                    UUID uuid;
                    try {
                        uuid = UUID.fromString(block.getMetadata(Wards.WARD_BLOCK).get(0).asString());
                    } catch (Exception e) { return; }

                    if (!p.getUniqueId().equals(uuid)) {
                        p.sendMessage(FormatUtils.color("&7No eres el owner de este ward"));
                        return;
                    }

                    updateAll(this, o);

                });
    }
}
