package io.github.divios.wards.potions;

import com.google.common.base.Preconditions;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.itemutils.ItemUtils;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class potionFactory {

    private static final Wards plugin = Wards.getInstance();
    private static final String WARDS_POTION = "Ward_meta_potion";

    public static ItemStack createPotion(Material material, int duration) {
        Preconditions.checkArgument(duration > 0);

        ItemStack potion = new ItemStack(material, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration * 20, 2), true);
        potion.setItemMeta(meta);

        return ItemBuilder.of(potion)
                .setName(Wards.configManager.getLangValues().POTION_NAME)
                .addLore(
                        Msg.msgList(Wards.configManager.getLangValues().POTION_LORE)
                                .add("\\{duration}", String.valueOf(duration))
                                .build()
                )
                .setMetadata(WARDS_POTION, duration);
    }

    public static boolean isPotion(ItemStack item) {

        if (!utils.isPotion(item)) return false;

        return new NBTItem(item).hasKey(WARDS_POTION);

    }

    public static int getDuration(ItemStack item) {

        if (!isPotion(item)) return -1;

        return ItemUtils.getMetadata(item, WARDS_POTION, Integer.class);

    }

}
