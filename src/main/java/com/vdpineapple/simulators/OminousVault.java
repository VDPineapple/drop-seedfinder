package com.vdpineapple.simulators;
import com.vdpineapple.LootTableRNG;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OminousVault {
    private static void ominousVaultCommonDrop(LootTableRNG rng) {
        // 0-4 = emerald
        // 5-8 = wind charge
        // 9-11 = slowness tipped
        // 12-13 = diamond
        // 14 = ominous bottle

        int commonRoulette = rng.nextInt(15);
        // System.out.println("Common Roulette: " + commonRoulette);
        rng.nextLong();
    }

    // Returns whether to abort the simulation (false), or continue (true)
    private static boolean ominousVaultRareDrop(LootTableRNG rng) {
        // 0-4 = emerald block
        // 5-8 = iron block
        // 9-12 = crossbow
        // 13-15 = golden apple
        // 16-18 = diamond axe
        // 19-21 = diamond chestplate
        // 22-23 = knockback, punch, smite, looting, or multishot book
        // 24-25 = breach or density book
        // 26-27 = wind burst book
        // 28 = diamond block
        int rareRoulette = rng.nextInt(29);
        // System.out.println("Rare Roulette: " + rareRoulette);
        // these numbers roll again
        Set<Integer> rollAgain = new HashSet<>(Arrays.asList(22, 23, 24, 25));
        // Simulating enchanting is required for these items, which is too complicated
        Set<Integer> errorNums = new HashSet<>(Arrays.asList(9, 10, 11, 16, 17, 18, 19, 20, 21));
        if (errorNums.contains(rareRoulette)) {
            return false;
        }
        if (rollAgain.contains(rareRoulette)) {
            rng.nextLong();
            rng.nextLong();
        }
        return true;
    }

    // Returns either:
    // -1 = abort simulation
    // 0 = no heavy core
    // 1 = heavy core
    private static int ominousVaultHeavyCoreDrop(LootTableRNG rng) {
        int rareOrCommon = rng.nextInt(10);
        if (rareOrCommon < 8) {
            if (!ominousVaultRareDrop(rng)) {
                return -1;
            }
        } else {
            ominousVaultCommonDrop(rng);
        }

        int extraCommon = 1 + rng.nextInt(3);
        for (int i = 0; i < extraCommon; i++) {
            ominousVaultCommonDrop(rng);
        }

        float uniqueChance = rng.nextFloat();
        // System.out.println("Unique Chance: " + uniqueChance);
        if (uniqueChance >= 0.75f) {
            return 0;
        }
        
        int uniqueRoulette = rng.nextInt(10);
        // System.out.println(uniqueRoulette);
        if (uniqueRoulette == 9) {
            return 1;
        }
        return 0;
    }

    public static int ominousVaultsUntilHeavyCore(long seed, int cutoff) {
        LootTableRNG rng = new LootTableRNG("minecraft:chests/trial_chambers/reward_ominous");
        rng.setSeed(seed);

        int vaults = 0;
        for (int i = 0; i < cutoff; i++) {
            vaults += 1;
            int result = ominousVaultHeavyCoreDrop(rng);
            if (result == -1) {
                return Integer.MAX_VALUE;
            } else if (result == 1) {
                return vaults;
            }
        }

        return Integer.MAX_VALUE;
    }
}
