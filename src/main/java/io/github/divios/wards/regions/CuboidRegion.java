package io.github.divios.wards.regions;

import io.github.divios.wards.utils.ChunkUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CuboidRegion implements RegionI {

    private final io.github.divios.core_lib.region.CuboidRegion region;

    public CuboidRegion(Location l, int radius) {
        region = io.github.divios.core_lib.region.CuboidRegion.cubeRadius(l, radius);
    }

    @Override
    public boolean isInside(Location l) {
        return region.contains(l);
    }

    @Override
    public boolean isInside(Player p) {
        return isInside(p.getLocation());
    }

    @Override
    public Set<Block> getBlocks() {
        Set<Block> blocks = new HashSet<>();

        getChunks().forEach(chunk -> {
            blocks.addAll(ChunkUtils.getBlocks(chunk,
                    block -> isInside(block.getLocation())));
        });

        return blocks;
    }

    @Override
    public Set<Block> getSurface() {
        Set<Block> surface = new HashSet<>();

        Arrays.stream(BlockFace.values()).forEach(blockFace -> {
            io.github.divios.core_lib.region.CuboidRegion face = region.getFace(blockFace);

            face.getChunks().forEach(chunk -> {
                surface.addAll(ChunkUtils.getBlocks(chunk,
                        block -> face.contains(block.getLocation())));
            });

        });
        return surface;
    }

    @Override
    public Set<Chunk> getChunks() {
        return region.getChunks();
    }
}
