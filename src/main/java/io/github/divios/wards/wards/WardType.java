package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Preconditions;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.core_lib.misc.Pair;
import io.github.divios.core_lib.utils.Log;
import io.github.divios.wards.Wards;
import io.github.divios.wards.regions.ChunkRegionImpl;
import io.github.divios.wards.regions.CuboidRegionImpl;
import io.github.divios.wards.regions.RegionI;
import io.github.divios.wards.regions.SpheroidRegionImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WardType {

    protected static final Wards plugin = Wards.getInstance();

    private final int hash;         // Hash cached

    private final String id;
    private final XMaterial material;
    private final String display_name;
    private final List<String> lore;
    private final long time;
    private final WardTypeE type;
    private final int radius;
    private final WardsRecipe recipe;

    private WardType(
            String id,
            XMaterial material,
            String display_name,
            List<String> lore,
            long time,
            WardTypeE type,
            int radius,
            Material[][] recipeLines
    ) {
        this.id = id;
        this.material = material;
        this.display_name = display_name;
        this.lore = lore;
        this.time = time;
        this.type = type;
        this.radius = radius;
        this.recipe = recipeLines == null ? null: new WardsRecipe(this, recipeLines);

        this.hash = id.hashCode();
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return material.parseMaterial();
    }

    public String getDisplay_name() {
        return display_name;
    }

    public List<String> getLore() {
        return lore;
    }

    public long getTime() {
        return time;
    }

    public WardTypeE getType() {
        return type;
    }

    public int getRadius() {
        return radius;
    }

    public WardsRecipe getRecipe() {
        return recipe;
    }

    public ItemStack buildItem(Player p) {

        NBTItem item = new NBTItem(new ItemBuilder(material)
                .setName(display_name).setLore(lore));

        String uuid = p == null ? null : p.getUniqueId().toString();

        item.setString(Wards.WARD_META, "pizza");
        item.setString(Wards.WARD_ID, id);
        item.setString(Wards.WARD_OWNER, uuid);
        item.setLong(Wards.WARD_TIMER, time);

        return item.getItem();
    }

    public ItemStack buildItem() {
        return buildItem(null);
    }

    public RegionI getRegion(Location l) {
        RegionI region;

        if (type == WardTypeE.CHUNK)
            region = new ChunkRegionImpl(l, radius);
        else if (type == WardTypeE.CUBOID)
            region = new CuboidRegionImpl(l, radius);
        else
            region = new SpheroidRegionImpl(l, radius);

        return region;
    }

    public void destroy() {
        if (recipe!= null) recipe.destroy();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WardType wardType = (WardType) o;
        return id.equals(wardType.getId());
    }

    protected static class Builder {

        private String id = null;
        private String material = null;
        private String display_name = null;
        private String lore = null;
        private Long time = null;
        private String type = null;
        private Integer radius = null;
        private List<String> recipeLines = null;

        public Builder() {
        }

        public Builder(String id) {
            this.id = id;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setMaterial(String material) {
            this.material = material;
            return this;
        }

        public Builder setDisplay_name(String display_name) {
            this.display_name = display_name;
            return this;
        }

        public Builder setLore(String lore) {
            this.lore = lore;
            return this;
        }

        public Builder setTime(Long time) {
            this.time = time;
            return this;
        }

        public Builder setTime(Integer time) {
            this.time = time.longValue();
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setRadius(Integer radius) {
            this.radius = radius;
            return this;
        }

        public Builder setRecipe(List<String> recipesLines) {
            this.recipeLines = recipesLines;
            return this;
        }

        public WardType build() throws WardsTypeException {

            if (id == null || id.isEmpty()) {
                throw new WardsTypeException("id");
            }

            if (material == null || material.isEmpty() ||
                    !XMaterial.matchXMaterial(material).isPresent() ||
                    XMaterial.matchXMaterial(material).get().parseItem() == null ||
                    !XMaterial.matchXMaterial(material).get().parseMaterial().isBlock()
            ) {
                throw new WardsTypeException("Material");
            }

            if (display_name == null) {
                display_name = id;
            }
            display_name = FormatUtils.color(display_name);

            if (lore == null) {
                lore = "";
            }

            if (time == null) {
                throw new WardsTypeException("Time");
            }

            if (type == null || Arrays.stream(WardTypeE.values())
                    .map(Enum::toString).noneMatch(s -> s.equalsIgnoreCase(type))) {
                throw new WardsTypeException("Type");
            }

            if (radius == null) {
                throw new WardsTypeException("Radius");
            }

            Material[][] recipes;

            if (recipeLines == null || recipeLines.isEmpty()) recipes = null;

            else {

                if (recipeLines.size() > 3) throw new WardsTypeException("recipe");

                if (recipeLines.size() < 3)
                    IntStream.range(0, 3 - recipeLines.size()).forEach(i-> recipeLines.add("AIR, AIR, AIR"));

                if (recipeLines.stream().flatMap(s -> Arrays.stream(s.split(",")))
                        .noneMatch(s -> Material.getMaterial(s) != null))       // If not material found
                    throw new WardsTypeException("recipe");

                recipes = recipeLines.stream()
                        .map(s -> Stream.of(s.split(",")))
                        .map(stringStream -> stringStream.map(String::trim))
                        .map(s -> s.map(s1 -> Optional.ofNullable(Material.getMaterial(s1)).orElse(Material.AIR)))
                        .map(materialStream -> materialStream.toArray(Material[]::new))
                        .toArray(Material[][]::new);
            }

            return new WardType(
                    id,
                    XMaterial.valueOf(material),
                    display_name,
                    Arrays.asList(lore.split("\\|")),
                    time,
                    WardTypeE.valueOf(type.toUpperCase()),
                    radius,
                    recipes);

        }
    }

    public final static class WardsTypeException extends Exception {

        private final String cause;

        private WardsTypeException(String cause) {
            this.cause = cause;
        }

        public String getWho() {
            return cause;
        }

    }

    private enum WardTypeE {
        CUBOID, SPHEROID, CHUNK
    }

    private static final class WardsRecipe {

        private final List<Character> letters = new ArrayList<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'));
        private final WardType type;
        private final NamespacedKey name;

        public WardsRecipe(WardType type, Material[][] materials) {

            Preconditions.checkNotNull(type, "type null");
            Preconditions.checkArgument(materials.length == 3, "Materials length");
            Preconditions.checkArgument(Arrays.stream(materials).allMatch(materials1 -> materials1.length == 3), "Material length");

            this.type = type;
            name = new NamespacedKey(plugin, "Ward_" + type.getId());

            parseMaterials(materials);

        }

        private void parseMaterials(Material[][] materials) {

            List<Pair<Character, Material>> recipeMap = new ArrayList<>();

            for (int i = 0; i<3; i++) {
                for (int j=0; j<3; j++) {

                    Material entry = materials[i][j];

                    if (recipeMap.stream().anyMatch(pair -> pair.get2().equals(entry))) {
                        recipeMap.add(recipeMap.stream().filter(pair -> pair.get2().equals(entry)).findFirst().get());
                    } else {
                      recipeMap.add(Pair.of(letters.get(0), entry));
                      letters.remove(0);
                    }

                }
            }

            createRecipe(recipeMap);

        }

        private void createRecipe(List<Pair<Character, Material>> recipeMap) {

            Preconditions.checkArgument(recipeMap.size() == 9, "recipeMap size");

            ShapedRecipe customRecipe = new ShapedRecipe(name, type.buildItem());

            List<String> auxRecipes = new ArrayList<>();
            recipeMap.forEach(pair -> {
                if (pair.get2().equals(Material.AIR))
                    auxRecipes.add(" ");
                else
                    auxRecipes.add(String.valueOf(pair.get1()));
            });

            customRecipe.shape(
                    auxRecipes.get(0).concat(auxRecipes.get(1)).concat(auxRecipes.get(2)),
                    auxRecipes.get(3).concat(auxRecipes.get(4)).concat(auxRecipes.get(5)),
                    auxRecipes.get(6).concat(auxRecipes.get(7)).concat(auxRecipes.get(8))
            );

            Set<Character> keysSeen = new HashSet<>();
            recipeMap.forEach(key -> {
                if (keysSeen.contains(key.get1())) return;

                if (key.get2().equals(Material.AIR)) return;

                //Log.info(String.valueOf(key.get1()));
                keysSeen.add(key.get1());
                customRecipe.setIngredient(key.get1(), key.get2());
            });

            Bukkit.addRecipe(customRecipe);

        }

        public void destroy() {
            Bukkit.removeRecipe(name);
        }
    }

}
