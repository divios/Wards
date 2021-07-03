package io.github.divios.wards.wards;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTItem;
import io.github.divios.core_lib.itemutils.ItemBuilder;
import io.github.divios.core_lib.misc.FormatUtils;
import io.github.divios.wards.Wards;
import io.github.divios.wards.regions.ChunkRegion;
import io.github.divios.wards.regions.RegionI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class WardType {

    private static final Wards plugin = Wards.getInstance();
    private static final Set<String> ids = new HashSet<>();

    private final int hash;         // Hash cached

    private final String id;
    private final XMaterial material;
    private final String display_name;
    private final List<String> lore;
    private final int time;
    private final WardTypeE type;
    private final int radius;

    private WardType(String id,
                     XMaterial material,
                     String display_name,
                     List<String> lore,
                     int time,
                     WardTypeE type,
                     int radius
    ) {
        this.id = id;
        this.material = material;
        this.display_name = display_name;
        this.lore = lore;
        this.time = time;
        this.type = type;
        this.radius = radius;

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

    public int getTime() {
        return time;
    }

    public WardTypeE getType() {
        return type;
    }

    public int getRadius() {
        return radius;
    }

    public ItemStack buildItem(Player p) {

        NBTItem item = new NBTItem(new ItemBuilder(material)
                .setName(display_name).setLore(lore));

        item.setString(Wards.WARD_META, "pizza");
        item.setString(Wards.WARD_ID, id);
        item.setString(Wards.WARD_OWNER, p.getUniqueId().toString());

        return item.getItem();
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
        private Optional<XMaterial> material = Optional.empty();
        private String display_name = null;
        private String lore = null;
        private Integer time = null;
        private String type = null;
        private Integer radius = null;

        public Builder() {}

        public Builder(String id) {
            this.id = id;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setMaterial(String material) {
            this.material = XMaterial.matchXMaterial(material);
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

        public Builder setTime(Integer time) {
            this.time = time;
            return this;
        }

        public Builder setType(String  type) {
            this.type = type;
            return this;
        }

        public Builder setRadius(Integer radius) {
            this.radius = radius;
            return this;
        }

        public WardType build() throws WardsTypeException {

            if (id == null || id.isEmpty() || ids.contains(id)) {
                throw new WardsTypeException("id");
            }
            ids.add(id);

            if (!material.isPresent()) {
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

            return new WardType(id,
                    material.get(),
                    display_name,
                    Arrays.asList(lore.split("\\|")),
                    time,
                    WardTypeE.valueOf(type.toUpperCase()),
                    radius);

        }
    }

    public static class WardsTypeException extends Exception {

        private final String cause;

        private WardsTypeException(String cause) {
            this.cause = cause;
        }

        public String getWho() { return cause; }

    }

    private enum WardTypeE {
        CUBOID, SPHEROID, CHUNK
    }

}
