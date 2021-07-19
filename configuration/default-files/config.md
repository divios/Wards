---
description: Various options impacting the plugin in its generality
---

# Config

```yaml

# Do not change this
version: 0.1

locale: US

# Amount of seconds the ward structure is displayed
#
chunk_display_seconds: 10

# Amount of seconds until a player can request a ward display again
#
# Although all the operations are done async and have great performance,
# if exploited, it can cause some lag. Tweak this value depending on your player
# base and server but 30 seconds should be fine
#
chunk_display_cooldown: 30

# How often in ticks a ward checks in his given radius
# for players.
#
# This task is really optimized and done async. 20 ticks (1 sec)
# is a conservative value that should work perfectly in most of the cases
# but don't be afraid to lower it
#
ward_check_cycle_seconds: 20
```
