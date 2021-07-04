package io.github.divios.wards.regions;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Set;

public interface RegionI {

    boolean isInside(Location l);
    Set<Block> getBlocks();
    Set<Block> getSurface();
    Set<Chunk> getChunks();
    Set<Chunk> getLoadedChunks();
    Location getCenter();

}
