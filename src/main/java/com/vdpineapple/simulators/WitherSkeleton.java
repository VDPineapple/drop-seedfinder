package com.vdpineapple.simulators;
import com.vdpineapple.LootTableRNG;

public class WitherSkeleton {

    public static boolean skullDrop(LootTableRNG rng, int looting) {
        rng.nextLong();
        rng.nextLong();
        if (looting > 0) {
            rng.nextLong();
            rng.nextLong();
        }
        float val = rng.nextFloat();
        float cutoff = 0.025f + 0.01f * looting;

        return (val < cutoff);
    }
    public static int killsUntilNSkulls(long seed, int desiredSkulls, int looting, int cutoff) {
        LootTableRNG rng = new LootTableRNG("minecraft:entities/wither_skeleton");
        rng.setSeed(seed);

        int skullsCount = 0;
        int kills = 0;
        while (skullsCount < desiredSkulls) {
            if (skullDrop(rng, looting)) {
                skullsCount += 1;
            }
            kills += 1;
            if (kills > cutoff) {
                return Integer.MAX_VALUE;
            }
        }
        return kills;
    }
}
