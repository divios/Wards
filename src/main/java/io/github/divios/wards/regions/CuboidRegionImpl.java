package io.github.divios.wards.regions;

import io.github.divios.core_lib.region.CuboidRegion;
import io.github.divios.wards.utils.ChunkUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CuboidRegionImpl extends RegionI {

    public CuboidRegionImpl(Location l, int radius) {
        super(l, CuboidRegion.cubeRadius(l, radius));
    }

    @Override
    Set<Block> getBlocksImpl() {
        Set<Block> blocks = new HashSet<>();

        region.getChunks().forEach(chunk -> {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 256; y++) {
                    for (int z = 0; z < 256; z++) {
                        Location l = new Location(region.getWorld(), x, y, z);
                        if (region.contains(l))
                            blocks.add(l.getBlock());
                    }
                }
            }
        });
        return blocks;
    }

    @Override
    Set<Block> getSurfaceImpl() {
        Set<Block> surface = new HashSet<>();

        Stream.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN)
                .forEach(blockFace -> {
                    CuboidRegion face = ((CuboidRegion) region).getFace(blockFace);

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
