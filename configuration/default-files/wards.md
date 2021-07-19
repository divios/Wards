---
description: Pre-configured Wards
---

# Wards

## Guardian

A simple ward in form of a beacon with no expired time that guards a CUBOID region

```yaml
#  Sentinel
#=========================================================
#
#
#

# id (String)
#
# ID is the internal name used to identify this type of Ward.
# It MUST remain unique and should not contain spaces or any special characters.
#
id: Guardian

# material (Material)
#
# Material of the Ward Block.
# This MUST be a valid Minecraft block.
#
# Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
# for valid materials of spigot latest version
#
material: BEACON

#  name (String)
#
# The name of the item of the Ward.
#
display_name: '&c&lGuardian'

#  lore (String)
#
# The lore of the item of the chunk loader.
#
lore: '&7Place it to watch a zone and|&7get a ping if someone not desired enters'

# time (Integer)
#
# The default amount of time the Ward will be alive.
#
# Set to -1 if you want to disable this feature.
#
time: -1

# type
#
# The type of region this ward watches. The names are representative
# of the type of region they hold.
#
# <type>: CUBOID, SPHEROID, CHUNK
#
type: CUBOID

# radius (Integer)
#
# The distance this ward watches.
#
# For the CHUNK type means the radius of chunks for each direction.
#
radius: 8
```

## Sentinel

A simple ward in form of a respawn\_anchor that guards a SPHREOID region and expires after 300 seconds

```yaml
#  Sentinel
#=========================================================
#
#
#

# id (String)
#
# ID is the internal name used to identify this type of Ward.
# It MUST remain unique and should not contain spaces or any special characters.
#
id: Sentinel

# material (Material)
#
# Material of the Ward Block.
# This MUST be a valid Minecraft block.
#
# Check https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
# for valid materials of spigot latest version
#
material: RESPAWN_ANCHOR

#  name (String)
#
# The name of the item of the Ward.
#
display_name: '&9&lSentinel'

#  lore (String)
#
# The lore of the item of the chunk loader.
#
lore: '&7Place it to watch a zone and|&7get a ping if someone not desired enters'

# time (Integer)
#
# The default amount of time the Ward will be alive.
#
# Set to -1 if you want to disable this feature.
#
time: 300

# type
#
# The type of region this ward watches. The names are representative
# of the type of region they hold.
#
# <type>: CUBOID, SPHEROID, CHUNK
#
type: SPHEROID

# radius (Integer)
#
# The distance this ward watches. Has to be > 0
#
# For the CHUNK type means the radius of chunks for each direction .
#
radius: 20

```
