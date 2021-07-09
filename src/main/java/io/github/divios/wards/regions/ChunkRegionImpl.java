package io.github.divios.wards.regions;

import io.github.divios.core_lib.region.CuboidRegion;
import io.github.divios.core_lib.region.Region;
import io.github.divios.wards.utils.ChunkUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ChunkRegionImpl extends RegionI {

    public ChunkRegionImpl(Location l, int radius) {
        super(l, () -> generateRegion(l, radius));
    }

    private static Region generateRegion(Location l, int radius) {
        World world = l.getWorld();
        int[] center = {l.getChunk().getX(), l.getChunk().getZ()};
        int itits = 1 + 2 * (radius - 1);
        int x = -(radius - 1);
        int z = -(radius - 1);

        Chunk topLeftChunk = world.getChunkAt(center[0] + x, center[1] + z);
        Chunk bottomRightChunk = world.getChunkAt(center[0] + (x + itits - 1), center[1] + (z + itits - 1));

        Location topLeft = topLeftChunk.getBlock(0, 0, 0).getLocation();
        Location bottomRight = bottomRightChunk.getBlock(15, 0, 15).getLocation();

        return new CuboidRegion(topLeft, bottomRight).expand(0, 0, 255, 0, 0, 0);
    }

    @Override
    Set<Block> getBlocksImpl() {
        Set<Block> blocks = new HashSet<>();

        region.getChunks().forEach(chunk -> {
            blocks.addAll(ChunkUtils.getBlocks(chunk));
        });

        return blocks;
    }

    @Override
    Set<Block> getSurfaceImpl() {
        Set<Block> surface = new HashSet<>();

        Stream.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN)
                .forEach(blockFace -> {
                    CuboidRegion face = ((CuboidRegion)region).getFace(blockFace);

                    face.getChunks().forEach(chunk -> {
                        surface.addAll(ChunkUtils.getBlocks(chunk,
                                block -> face.contains(block.getLocation())));
                    });

                });

        return surface;
    }

}
