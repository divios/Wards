package io.github.divios.wards.regions;

import io.github.divios.core_lib.region.CuboidRegion;
import io.github.divios.wards.utils.ChunkUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkRegion implements RegionI {

    private final Set<Chunk> chunks = new HashSet<>();
    private final Location location;
    private final int radius;

    public ChunkRegion(Location l, int radius) {

        this.radius = radius;
        this.location = l;
        World world = l.getWorld();
        int[] center = {l.getChunk().getX(), l.getChunk().getZ()};

        int itits = 1 + 2 * (radius - 1);

        for (int i = 0; i < itits; i++) {
            for (int j = 0; j < itits; j++) {

                int x = -(radius - 1) + i;
                int z = -(radius - 1) + j;
                Chunk toAdd = world.getChunkAt(center[0] + x, center[1] + z);
                chunks.add(toAdd);
            }
        }
    }

    @Override
    public boolean isInside(Location l) {
        return chunks.contains(l.getChunk());
    }

    @Override
    public boolean isInside(Player p) {
        return isInside(p.getLocation());
    }

    @Override
    public Set<Block> getBlocks() {
        Set<Block> blocks = new HashSet<>();

        chunks.forEach(chunk -> {
            blocks.addAll(ChunkUtils.getBlocks(chunk));
        });

        return blocks;
    }

    @Override
    public Set<Block> getSurface() {
        Set<Block> surface = new HashSet<>();

        chunks.forEach(chunk -> {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {

                    double distance = location.distance(chunk.getBlock(x, location.getBlockY(), z).getLocation());
                    if (distance < 15) continue;

                    for (int y = 0; y < 256; y++) {
                        surface.add(chunk.getBlock(x, y ,z));
                    }
                }
            }
        });

        return surface;
    }

    @Override
    public Set<Chunk> getChunks() {
        return Collections.unmodifiableSet(chunks);
    }
}
