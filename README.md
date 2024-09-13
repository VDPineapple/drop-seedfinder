This is the code I used for my drop seedfinding but there are some issues with it. It's not intended to be used as is, but rather as a starting point for your own seedfinding.

You'll need to modify the App.java and Simulators.java to customize for your own needs.

The original seedfinder was created by Matthew Bolan.

## Known Issues
- Ominous vault seedfinding is tricky. The enchanted items seem to use the same loot table for enchanting, which would require an enchantment simulation to accurately seedfind. To avoid this, I skip any seed that has an enchanted item in the vault before the heavy core.