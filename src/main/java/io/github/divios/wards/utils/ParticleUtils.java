package io.github.divios.wards.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.github.divios.wards.Wards;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ParticleUtils {

    private static final Wards plugin = Wards.getInstance();
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();;

    public static void addGlow(Player player, Entity to) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, to.getEntityId()); //Set packet's entity id
        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher.setEntity(to); //Set the new data watcher's target
        watcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException ignored) {

        }
    }

    public static void removeGlow(Player player, Entity to) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, to.getEntityId()); //Set packet's entity id
        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher.setEntity(to); //Set the new data watcher's target
        watcher.setObject(0, serializer, (byte) (0x0)); //Set status to glowing, found on protocol page
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException ignored) {
        }
    }

    public static void spawnParticleShape(Player p, Location l) {
        if (Wards.MID_VERSION > 12)
            p.spawnParticle(Particle.REDSTONE, l, 1, new Particle.DustOptions(Color.ORANGE, 1));
        else if (Wards.MID_VERSION > 8)
            p.spawnParticle(Particle.REDSTONE, l, 0, 1, 139/255F, 0, 1);
        else
            l.getWorld().playEffect(l, Effect.valueOf("COLOURED_DUST"), null);
    }

    public static void spawnParticlePlace(Player p, Location l) {
        if (Wards.MID_VERSION <= 12) return;

        p.spawnParticle(Particle.SPELL_WITCH,
                l.add(Math.random(), Math.random(), Math.random()), 1);
    }

}
