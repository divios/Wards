package io.github.divios.wards.regions;

import io.github.divios.wards.utils.ChunkUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CuboidRegion implements RegionI {

    private final io.github.divios.core_lib.region.CuboidRegion region;
    private final Location center;

    public CuboidRegion(Location l, int radius) {
        region = io.github.divios.core_lib.region.CuboidRegion.cubeRadius(l, radius);
        this.center = l;
    }

    @Override
    public boolean isInside(Location l) {
        return region.contains(l);
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

        Stream.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN)
                .forEach(blockFace -> {
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

    @Override
    public Set<Chunk> getLoadedChunks() {
        return region.getLoadedChunks();
    }

    @Override
    public Location getCenter() {
        return center;
    }
}
