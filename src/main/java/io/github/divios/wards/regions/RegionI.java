package io.github.divios.wards.regions;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

public interface RegionI {

    boolean isInside(Location l);
    boolean isInside(Player p);
    Set<Block> getBlocks();
    Set<Block> getSurface();
    Set<Chunk> getChunks();

}
