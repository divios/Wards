package io.github.divios.wards.regions;

import io.github.divios.core_lib.region.CuboidRegion;
import io.github.divios.wards.utils.ChunkUtils;
import io.github.divios.wards.wards.Ward;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChunkRegion implements RegionI {

    private final CuboidRegion region;
    private final Location center;
    private final int radius;

    public ChunkRegion(Location l, int radius) {

        this.radius = radius;
        this.center = l;

        World world = l.getWorld();
        int[] center = {l.getChunk().getX(), l.getChunk().getZ()};
        int itits = 1 + 2 * (radius - 1);
        int x = -(radius - 1);
        int z = -(radius - 1);

        Chunk topLeftChunk = world.getChunkAt(center[0] + x, center[1] + z);
        Chunk bottomRightChunk = world.getChunkAt(center[0] + (x + itits - 1), center[1] + (z + itits - 1));

        Location topLeft = topLeftChunk.getBlock(0, 0, 0).getLocation();
        Location bottomRight = bottomRightChunk.getBlock(15, 0, 15).getLocation();

        region = new CuboidRegion(topLeft, bottomRight).expand(0, 0, 255, 0, 0, 0);

    }

    @Override
    public boolean isInside(Location l) {
        return region.contains(l);
    }

    @Override
    public Set<Block> getBlocks() {
        Set<Block> blocks = new HashSet<>();

        region.getChunks().forEach(chunk -> {
            blocks.addAll(ChunkUtils.getBlocks(chunk));
        });

        return blocks;
    }

    @Override
    public Set<Block> getSurface() {
        Set<Block> surface = new HashSet<>();

        Stream.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN)
                .forEach(blockFace -> {
                    CuboidRegion face = region.getFace(blockFace);

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
