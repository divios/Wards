package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import io.github.divios.core_lib.inventory.InventoryGUI;
import io.github.divios.core_lib.inventory.ItemButton;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.*;
import io.github.divios.wards.Wards;
import io.github.divios.wards.tasks.WardsShowTask;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class WardInventory {

    private static final Wards plugin = Wards.getInstance();

    private final Ward ward;
    private InventoryGUI mainInv;
    private InventoryGUI settingsInv;

    public WardInventory(Ward ward) {
        this.ward = ward;
        mainInv = build();
        settingsInv = createSettings();
    }

    private InventoryGUI build() {

        InventoryGUI builded = new InventoryGUI(plugin, 27, ward.getName());

        IntStream.of(0, 1, 7, 8, 9, 17, 18, 19, 25).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.GLASS_PANE_1)
                    .setName("&c"), e -> {}), value);
        });

        IntStream.of(2, 6, 10, 16, 20, 24).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.GLASS_PANE_2)
                    .setName("&c"), e -> {}), value);
        });

        IntStream.of(3, 4, 5, 12, 14, 21, 22, 23).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.GLASS_PANE_3)
                    .setName("&c"), e -> {}), value);
        });

        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.TIME_MATERIAL)
                        .setName(Wards.guiValues.TIME_NAME)
                        .addLore(Msg.msgList(Wards.guiValues.TIME_LORE)
                                .add("\\{time}", (ward.getTimer() == -1 ?
                                        FormatUtils.color("&c&l" + XSymbols.TIMES_3.parseSymbol()) :
                                        FormatUtils.formatTimeOffset(ward.getTimer() * 1000L) + "")).build()),
                e -> {})
                , 11);


        builded.addButton(ItemButton.create(new ItemBuilder(ward.getType().buildItem())
                    .setLore(Msg.msgList(Wards.guiValues.TYPE_LORE)
                            .add("\\{type}", "" + ward.getType().getType())
                            .add("\\{radius}", ward.getType().getRadius() + "")
                            .build())
                , e-> {}), 13);


        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.SHOW_MATERIAL)
                    .setName(Wards.guiValues.SHOW_NAME).addLore(Wards.guiValues.SHOW_LORE),
                e -> {
                    e.getWhoClicked().closeInventory();
                    WardsShowTask.generate((Player) e.getWhoClicked(), ward);
                }), 15);

        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.SETTINGS_MATERIAL)
                        .setName(Wards.guiValues.SETTINGS_NAME)
                        .addLore(Wards.guiValues.SETTINGS_LORE),
                e -> settingsInv.open((Player) e.getWhoClicked())), 26);

        builded.setDestroyOnClose(false);

        return builded;
    }

    private InventoryGUI createSettings() {
        InventoryGUI builded = new InventoryGUI(plugin, 27, ward.getName());

        IntStream.of(0, 1, 7, 8, 9, 17, 18, 19, 25).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.BLUE_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {}), value);
        });

        IntStream.of(2, 6, 10, 15, 16, 20, 24).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {}), value);
        });

        IntStream.of(3, 4, 5, 12, 13, 14, 21, 22, 23).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {}), value);
        });

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.PAPER)
                        .setName(Wards.guiValues.CHANGE_NAME_NAME)
                        .addLore(Wards.guiValues.CHANGE_NAME_LORE),
                e -> {

                    Player p = (Player) e.getWhoClicked();
                    ChatPrompt.prompt(plugin, p, s -> {
                        if (s.isEmpty()) {
                            settingsInv.open(p);
                            Msg.sendMsg(p, "&7The name cannot be empty!");
                        }

                        ward.setName(s);
                        destroy();
                        Task.syncDelayed(plugin, () -> {
                            mainInv = build();
                            settingsInv = createSettings();
                            settingsInv.open(p);
                        }, 1L);

                    }, motive -> {
                        if (WardsManager.getInstance().getWard(ward.getCenter()) == null) return;
                        Task.syncDelayed(plugin, () -> settingsInv.open(p));
                    }, "&e&lChange name", "&7Input new name");
                }), 11);

        builded.addButton(ItemButton.create(new ItemBuilder(Wards.guiValues.RETURN_MATERIAL)
                        .setName(Wards.guiValues.RETURN_NAME)
                        .addLore(Wards.guiValues.RETURN_LORE),
                e -> mainInv.open((Player) e.getWhoClicked())), 26);

        builded.setDestroyOnClose(false);

        return builded;
    }

    public void show(Player p) {
        mainInv.open(p);
    }

    public void update() {
        mainInv.getInventory().setItem(11, new ItemBuilder(Wards.guiValues.TIME_MATERIAL)
                .setName(Wards.guiValues.TIME_NAME)
                .addLore(Msg.msgList(Wards.guiValues.TIME_LORE)
                        .add("\\{time}", (ward.getTimer() == -1 ?
                                FormatUtils.color("&c&l" + XSymbols.TIMES_3.parseSymbol()) :
                                FormatUtils.formatTimeOffset(ward.getTimer() * 1000L) + "")).build()));
    }

    public void destroy() {

        Task.syncDelayed(plugin, () -> {            // Close invs sync
                    new ArrayList<>(mainInv.getInventory().getViewers())
                            .forEach(HumanEntity::closeInventory);

                    new ArrayList<>(settingsInv.getInventory().getViewers())
                            .forEach(HumanEntity::closeInventory);
                });

        mainInv.destroy();
        settingsInv.destroy();

    }

}
