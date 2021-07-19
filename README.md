
## What is Wards?

Wards is not a protection plugin. It's main purpose is to alert and display when a not desire player enters/exits a region, very similar to a centinel in League of Legends. The player who enters is[ highligted ](https://minecraft.fandom.com/wiki/Glowing)to the owner and allies and is also pinned. This could alert if someone is trying to steal from you base or giving information and advantage in a war of factions for example

## How does it work?

When a spigot server starts, all .yml inside the folder wards are parsed into a ward object. After that, players can receive those wards with a single command. When those items are placed, they are added to a very efficient task, which itinerates throught every ward and checks for players in it's configurable radius. This task is repeated async but not, for example, every 20 ticks, that could potencially cause lag, rather all wards are spread equally along those 20 ticks and runned in a queue, so the work is divided equally over the period Instead of a \(potential\) lag spike every 20 ticks.

## Performance

If the above explanation doesn't convince you, then let me tell you this. I've tested the plug-in with more than one hundred players with farms working and more than 80 wards placed with no stress on the cpu from Wards whatsoever. This was achieve by running all the heavy task in async and optimized them as hard as possible

## TODO

- Key that allows to break a centinel
- Implement roles per Ward
- Implement a potion to make a player invisible to a ward
- Add support/compatibility with protection plugins
- Add support/compatibility with factions plugins
- Add a compass that indicates if a ward is nearby??
- Limit amount of wards per player with permissions
