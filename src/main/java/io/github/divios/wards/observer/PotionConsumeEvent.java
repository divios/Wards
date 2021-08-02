package io.github.divios.wards.observer;

import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.potions.potionFactory;
import io.github.divios.wards.potions.potionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PotionConsumeEvent extends abstractObserver{

    protected PotionConsumeEvent() { super(); }

    @Override
    protected SingleSubscription<PlayerItemConsumeEvent> initListener() {

        return Events.subscribe(PlayerItemConsumeEvent.class, EventPriority.HIGH)
                .handler(o -> {

                    Player p = o.getPlayer();
                    ItemStack item = o.getItem();

                    if (!potionFactory.isPotion(item)) return;

                    Msg.sendMsg(p, Wards.configManager.getLangValues().POTION_DRINK);
                    potionManager.getInstance().add(p, potionFactory.getDuration(item));

                });
    }
}
