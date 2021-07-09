package io.github.divios.wards.regions;

import io.github.divios.core_lib.region.SpheroidRegion;
import io.github.divios.wards.utils.ChunkUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class SpheroidRegionImpl extends RegionI {

    public SpheroidRegionImpl(Location l, double radius) {
        super(l, new SpheroidRegion(l, radius));
    }

    @Override
    Set<Block> getBlocksImpl() {
        return getChunks().stream()
                .flatMap(chunk -> ChunkUtils.getBlocks(chunk, region::contains).stream())
                .collect(Collectors.toSet());
    }

    @Override
    Set<Block> getSurfaceImpl() {
        return ((SpheroidRegion) region).getSurface();
    }

}
