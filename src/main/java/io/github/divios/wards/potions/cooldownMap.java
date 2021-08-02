package io.github.divios.wards.potions;

import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.core_lib.scheduler.Task;
import io.github.divios.wards.Wards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class cooldownMap {

    private final Map<UUID, Integer> cooldowns = new ConcurrentHashMap<>();
    private final Task task;

    private cooldownMap() {
        task = init();
    }

    public static cooldownMap create() { return new cooldownMap(); }

    public void add(@NotNull Player p, int duration) {
        add(p.getUniqueId(), duration);
    }

    public void add(@NotNull UUID uuid, int duration) {
        if (exist(uuid))
            cooldowns.computeIfPresent(uuid, (uuid1, integer) -> integer + duration);
        else
            cooldowns.put(uuid, duration);
    }

    public boolean exist(@NotNull Player p) {
        return exist(p.getUniqueId());
    }

    public boolean exist(@NotNull UUID uuid) {
        return cooldowns.containsKey(uuid);
    }

    public @Nullable Integer get(@NotNull Player p) {
        return get(p.getUniqueId());
    }

    public Integer get(@NotNull UUID uuid) {
        return cooldowns.get(uuid);
    }

    public @NotNull Map<UUID, Integer> asMap() {
        return Collections.unmodifiableMap(cooldowns);
    }

    public void clear(@NotNull Player p) {
        clear(p.getUniqueId());
    }

    public void clear(@NotNull UUID uuid) {
        cooldowns.remove(uuid);
    }

    public void clearAll() {
        cooldowns.clear();
    }

    public void destroy() {
        clearAll();
        task.stop();
    }

    private Task init() {

        return Schedulers.builder()
                .sync()
                .afterAndEvery(20)
                .run(() -> {

                    for (Iterator<Map.Entry<UUID, Integer>> it = cooldowns.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<UUID, Integer> entry = it.next();

                        entry.setValue(entry.getValue() - 1);

                        if (entry.getValue() <= 0) {
                            it.remove();
                            Optional.ofNullable(Bukkit.getPlayer(entry.getKey()))
                                    .ifPresent(player -> Msg.sendMsg(player, Wards.configManager.getLangValues().POTION_EXPIRE));
                        }
                    }

                });
    }

}
