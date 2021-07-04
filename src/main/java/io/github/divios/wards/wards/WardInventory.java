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
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.BLUE_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {}), value);
        });

        IntStream.of(2, 6, 10, 16, 20, 24).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {}), value);
        });

        IntStream.of(3, 4, 5, 12, 14, 21, 22, 23).forEach(value -> {
            builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE)
                    .setName("&c"), e -> {}), value);
        });

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.CLOCK)
                        .setName("&6&lRemaining time").addLore("&7Remaining: &6" + (ward.getTimer() == -1 ?
                                FormatUtils.color("&c&l" + XSymbols.TIMES_3.parseSymbol()) :
                                FormatUtils.formatTimeOffset(ward.getTimer() * 1000L))),
                e -> {})
                , 11);

        builded.addButton(ItemButton.create(new ItemBuilder(ward.getType().buildItem())
                    .setLore("&7Type: &6" + ward.getType().getType(),
                            "&7Radius: &6" + ward.getType().getRadius())
                , e-> {}), 13);


        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.NETHER_STAR)
                    .setName("&6&lShow").addLore("&7Show bounds of this ward"),
                e -> {
                    e.getWhoClicked().closeInventory();
                    WardsShowTask.generate((Player) e.getWhoClicked(), ward);
                }), 15);

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.PAPER)
                        .setName("&6&lSettings").addLore("&7Click to manage this ward settings"),
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
                        .setName("&e&lChange Name").addLore("&7Click to change the name of this ward"),
                e -> {
                    new ChatPrompt(plugin, (Player) e.getWhoClicked(), (player, s) -> {

                        if (s.isEmpty()) {
                            settingsInv.open(player);
                            Msg.sendMsg(player, "&7The name cannot be empty!");
                        }

                        ward.setName(s);
                        destroy();
                        Task.syncDelayed(plugin, () -> {
                            mainInv = build();
                            settingsInv = createSettings();
                            settingsInv.open(player);
                        }, 1L);

                    }, player -> {

                        if (WardsManager.getInstance().getWard(ward.getCenter()) == null) return;
                        settingsInv.open(player);

                    }, "&e&lChange Name", "&7Input new name");
                }), 11);

        builded.addButton(ItemButton.create(new ItemBuilder(XMaterial.OAK_DOOR)
                        .setName("&c&lReturn").addLore("&7Click to go back"),
                e -> mainInv.open((Player) e.getWhoClicked())), 26);

        builded.setDestroyOnClose(false);

        return builded;
    }

    public void show(Player p) {
        mainInv.open(p);
    }

    public void update() {
        mainInv.getInventory().setItem(11, new ItemBuilder(XMaterial.CLOCK)
                .setName("&6Remaining time").setLore("&7Remaining: &6" +
                        FormatUtils.formatTimeOffset(ward.getTimer() * 1000L)));
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
