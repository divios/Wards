package io.github.divios.wards.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ChunkUtils {

    public static Set<Block> getBlocks(Chunk chunk) {
        return getBlocks(chunk, block -> true);
    }

    public static Set<Block> getBlocks(Chunk chunk, Predicate<Block> condition) {

        Set<Block> blocks = new HashSet<>();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {

                    Block block = chunk.getBlock(x, y ,z);
                    if (!condition.test(block)) continue;

                    blocks.add(chunk.getBlock(x, y ,z));
                }
            }
        }
        return blocks;
    }

}
