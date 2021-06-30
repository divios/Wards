package io.github.divios.wards.wards;

import io.github.divios.core_lib.misc.EventListener;
import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.UUID;

public class Ward {

    private final String id;
    private final WardType type;
    private final int radius;

    private final HashMap<UUID, Location> placed = new HashMap<>();

    public Ward(String id, WardType type, int radius) {
        this.id = id;
        this.type = type;
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public WardType getType() {
        return type;
    }

    public int getRadius() {
        return radius;
    }


    private void initListeners() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ward ward = (Ward) o;
        return id.equals(ward.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
