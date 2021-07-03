package io.github.divios.wards.regions;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

public class SpheroidRegion implements RegionI {

    private final io.github.divios.core_lib.region.SpheroidRegion region;

    public SpheroidRegion(Location l, double radius) {
        region = new io.github.divios.core_lib.region.SpheroidRegion(l, radius);
    }

    @Override
    public boolean isInside(Location l) {
        return region.contains(l);
    }

    @Override
    public Set<Block> getBlocks() {
        return null;
    }

    @Override
    public Set<Block> getSurface() {
        return region.getSurface();
    }

    @Override
    public Set<Chunk> getChunks() {
        return region.getChunks();
    }

    @Override
    public Set<Chunk> getLoadedChunks() {
        return region.getLoadedChunks();
    }

    @Override
    public Location getCenter() {
        return region.getCenter();
    }
}
