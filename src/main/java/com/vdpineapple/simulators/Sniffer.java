package com.vdpineapple.simulators;
import com.vdpineapple.LootTableRNG;

public class Sniffer {
    private static boolean dropTorchflower(LootTableRNG rng) {
        return rng.nextInt(2) == 0;
    }

    public static int digsUntilNTorchflowers(long seed, int n, int cutoff) {
        LootTableRNG rng = new LootTableRNG("minecraft:gameplay/sniffer_digging");
        rng.setSeed(seed);

        int digs = 0;
        int torchflowers = 0;
        while (torchflowers < n) {
            if (dropTorchflower(rng)) {
                torchflowers += 1;
            }
            digs += 1;
            if (digs > cutoff) {
                return Integer.MAX_VALUE;
            }
        }

        return digs;
    }
}
