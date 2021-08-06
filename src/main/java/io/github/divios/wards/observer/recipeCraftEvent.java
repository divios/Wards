package io.github.divios.wards.observer;

import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.wards.Wards;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;

/*
 * The only use of this event is to add
 * the owner to the ward item when crafting
 * since it is not possible on wardType@WardRecipe
 */

public class recipeCraftEvent extends abstractObserver{

    protected recipeCraftEvent() {
        super();
    }

    @Override
    protected SingleSubscription<CraftItemEvent> initListener() {
        return Events.subscribe(CraftItemEvent.class, EventPriority.HIGHEST)
                .handler(o -> {

                    NBTItem item = new NBTItem(o.getRecipe().getResult());
                    if (!item.hasKey(Wards.WARD_META)) return;
                    if (WardsManager.getInstance().getWardType(item.getString(Wards.WARD_ID)) == null) return;

                    item.setString(Wards.WARD_OWNER, o.getWhoClicked().getUniqueId().toString());
                    o.getInventory().setResult(item.getItem());

                });
    }
}
