package io.github.divios.wards.regions;

import io.github.divios.core_lib.cache.Lazy;
import io.github.divios.core_lib.region.Region;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class RegionI {

    private final Location center;
    protected final Region region;
    private int hash;

    private Lazy<Set<Block>> blocks;
    private Lazy<Set<Block>> surface;

    public RegionI(Location center, Region region) {
        this.center = center;
        this.region = region;
        this.hash = region.hashCode();

        blocks = Lazy.suppliedBy(this::getBlocksImpl);
        surface = Lazy.suppliedBy(this::getSurfaceImpl);

    }

    public RegionI(Location center, Supplier<Region> generator) {
        this(center, generator.get());
    }

    abstract Set<Block> getBlocksImpl();

    abstract Set<Block> getSurfaceImpl();

    private void peekUpdate() {
        int newHash = region.hashCode();

        if (newHash != hash) {
            hash = newHash;
            blocks = Lazy.suppliedBy(this::getBlocksImpl);
            surface = Lazy.suppliedBy(this::getSurfaceImpl);
        }
    }

    public boolean isInside(Location l) {
        return region.contains(l);
    }

    public Set<Block> getBlocks() {
        peekUpdate();
        return blocks.get();
    }

    public Set<Block> getSurface() {
        peekUpdate();
        return surface.get();
    }

    public Set<Chunk> getChunks() {
        return region.getChunks();
    }

    public Set<Chunk> getLoadedChunks() {
        return getChunks().stream()
                .filter(Chunk::isLoaded)
                .collect(Collectors.toSet());
    }

    public Location getCenter() {return center;}

}
