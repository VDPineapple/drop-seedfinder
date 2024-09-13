package com.vdpineapple.simulators;
import com.vdpineapple.LootTableRNG;

public class Shulker {
    public static boolean shulkerDrop(LootTableRNG rng, int looting) {
        float val = rng.nextFloat();
        return (val < 0.5f + 0.0625f * looting);
    }

    public static int killsUntilNShulker(long seed, int desiredShells, int looting, int cutoff) {
        LootTableRNG rng = new LootTableRNG("minecraft:entities/shulker");
        rng.setSeed(seed);

        int shellsCount = 0;
        int kills = 0;
        while (shellsCount < desiredShells) {
            if (shulkerDrop(rng, looting)) {
                shellsCount += 1;
            }
            kills += 1;
            if (kills > cutoff) {
                return Integer.MAX_VALUE;
            }
        }

        return kills;
    }
}
