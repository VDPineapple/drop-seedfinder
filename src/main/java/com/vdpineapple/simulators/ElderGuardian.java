package com.vdpineapple.simulators;
import com.vdpineapple.LootTableRNG;

public class ElderGuardian {
    public static boolean tideTrimDrop(LootTableRNG rng) {
        rng.nextLong();
        rng.nextLong();
        float extraFish = rng.nextFloat();
        if (extraFish < 0.025f) {
            rng.nextLong();
        }
        
        return rng.nextInt(5) == 4;
    }

    public static int killsUntilTideTrim(long seed, int cutoff) {
        LootTableRNG rng = new LootTableRNG("minecraft:entities/elder_guardian");
        rng.setSeed(seed);

        int kills = 0;
        for (int i = 0; i < cutoff; i++) {
            kills += 1;
            if (tideTrimDrop(rng)) {
                return kills;
            }
        }

        return Integer.MAX_VALUE;
    }
}
