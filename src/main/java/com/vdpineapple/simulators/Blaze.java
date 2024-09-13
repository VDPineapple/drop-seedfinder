package com.vdpineapple.simulators;
import com.vdpineapple.LootTableRNG;

public class Blaze {
    public static int blazeDrop(LootTableRNG rng, int looting) {
        // 0-1 blaze rods
        int rods = rng.nextIntIncl(0, 1);
        if (looting > 0) {
            float f = (float)looting * rng.nextFloat();
            rods += Math.round(f);
        }
        return rods;
    }

    public static int killsUntilNRods(long seed, int desiredRods, int looting, int cutoff) {
        LootTableRNG rng = new LootTableRNG("minecraft:entities/blaze");
        rng.setSeed(seed);

        int rodsCount = 0;
        int kills = 0;
        while (rodsCount < desiredRods) {
            rodsCount += blazeDrop(rng, looting);
            kills += 1;
            if (kills > cutoff) {
                return Integer.MAX_VALUE;
            }
        }
        return kills;
    }
}
