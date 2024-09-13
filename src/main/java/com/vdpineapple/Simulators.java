package com.vdpineapple;
import com.vdpineapple.simulators.*;
import java.util.HashMap;

public class Simulators {
    private static final float TIME_PER_WITHER = 0.75f;
    private static final float TIME_PER_SHULKER = 8.0f;
    private static final float TIME_PER_BARTER = 1.2f;
    private static final float TIME_PER_ROD = 3.0f;
    private static final float TIME_PER_ELDER_GUARDIAN = 20.0f;
    private static final float TIME_PER_OMINOUS_VAULT = 15.0f;

    // Values needed
    private static final int SKULLS_NEEDED = 3;
    private static final int SHELLS_NEEDED = 4;
    private static final int RODS_NEEDED = 6;
    private static final int TORCHFLOWERS_NEEDED = 3;
    private static final int PEARLS_NEEDED = 18;
    private static final int FIRE_RES_NEEDED = 1;
    private static final HashMap<String, Integer> requiredDrops = new HashMap<>();
    static {
        requiredDrops.put("Ender Pearl", PEARLS_NEEDED);
        requiredDrops.put("Fire Resistance Potion", FIRE_RES_NEEDED);
    }

    private static final int SHELLS_LOOTING = 0;
    private static final int RODS_LOOTING = 0;
    private static final int SKULLS_LOOTING = 3;

    // Values beyond which the RNG is unacceptable
    private static final int WITHER_CUTOFF = 20;
    private static final int SHULKER_CUTOFF = 5;
    private static final int BARTER_CUTOFF = 99;
    private static final int ROD_CUTOFF = 9;
    private static final int ELDER_GUARDIAN_CUTOFF = 1;
    private static final int OMINOUS_VAULT_CUTOFF = 2;
    private static final int TORCHFLOWER_CUTOFF = 3;

    // Returns a float indicating the quality of the total RNG for the seed
    // with estimated time losses for each element
    public static float aaQuality(long seed, float cutoff) {
        // We need at least 18 pearls and 1 fire resistance potion
        float time = 0.0f;
        int torchflowers = Sniffer.digsUntilNTorchflowers(seed, TORCHFLOWERS_NEEDED, TORCHFLOWER_CUTOFF);
        if (torchflowers == Integer.MAX_VALUE) {return Float.MAX_VALUE;}
        int heavyCoreVaults = OminousVault.ominousVaultsUntilHeavyCore(seed, OMINOUS_VAULT_CUTOFF);
        if (heavyCoreVaults == Integer.MAX_VALUE) {return Float.MAX_VALUE;}
        time += heavyCoreVaults * TIME_PER_OMINOUS_VAULT;
        time += ElderGuardian.killsUntilTideTrim(seed, ELDER_GUARDIAN_CUTOFF) * TIME_PER_ELDER_GUARDIAN;
        if (time > cutoff) {return Float.MAX_VALUE;}
        time += Shulker.killsUntilNShulker(seed, SHELLS_NEEDED, SHELLS_LOOTING, SHULKER_CUTOFF) * TIME_PER_SHULKER;
        if (time > cutoff) {return Float.MAX_VALUE;}
        time += Barter.bartersUntilDesired(seed, requiredDrops, BARTER_CUTOFF, false) * TIME_PER_BARTER;
        if (time > cutoff) {return Float.MAX_VALUE;}
        time += Blaze.killsUntilNRods(seed, RODS_NEEDED, RODS_LOOTING, ROD_CUTOFF) * TIME_PER_ROD;
        time += WitherSkeleton.killsUntilNSkulls(seed, SKULLS_NEEDED, SKULLS_LOOTING, WITHER_CUTOFF) * TIME_PER_WITHER;

        return time;
    }

    public static void aaQualityReport(long seed) {
        int high = 99999;
        int witherKills = WitherSkeleton.killsUntilNSkulls(seed, SKULLS_NEEDED, SKULLS_LOOTING, high);
        int shulkerKills = Shulker.killsUntilNShulker(seed, SHELLS_NEEDED, SHELLS_LOOTING, high);
        int rodKills = Blaze.killsUntilNRods(seed, RODS_NEEDED, RODS_LOOTING, high);
        int barters = Barter.bartersUntilDesired(seed, requiredDrops, high, false);
        int elderGuardianKills = ElderGuardian.killsUntilTideTrim(seed, high);
        int torchflowers = Sniffer.digsUntilNTorchflowers(seed, TORCHFLOWERS_NEEDED, TORCHFLOWER_CUTOFF);
        int heavyCoreVaults = OminousVault.ominousVaultsUntilHeavyCore(seed, OMINOUS_VAULT_CUTOFF);
        
        // System.out.println("Wither Kills (3 skulls): " + witherKills);
        System.out.println("Wither Kills (" + SKULLS_NEEDED + " skulls): " + witherKills);
        System.out.println("Shulker Kills (" + SHELLS_NEEDED + " shells): " + shulkerKills);
        System.out.println("Rod Kills (" + RODS_NEEDED + " rods): " + rodKills);
        System.out.println("Barters (" + PEARLS_NEEDED + " pearls, " + FIRE_RES_NEEDED + " fire res): " + barters);
        System.out.println("Elder Guardian Kills (Tide Trim): " + elderGuardianKills);
        System.out.println("Torchflowers: 3/" + torchflowers);
        System.out.println("Heavy Core Vaults: " + heavyCoreVaults);

    }

    public static void chances(int iterations) {
        float witherSuccesses = 0;
        float shulkerSuccesses = 0;
        float rodSuccesses = 0;
        float barterSuccesses = 0;
        float elderGuardianSuccesses = 0;
        float torchflowerSuccesses = 0;
        float heavyCoreVaultSuccesses = 0;

        for (int i = 0; i < iterations; i++) {
            long seed = (long) (Math.random() * Long.MAX_VALUE);
            if (WitherSkeleton.killsUntilNSkulls(seed, SKULLS_NEEDED, SKULLS_LOOTING, WITHER_CUTOFF) <= WITHER_CUTOFF) {witherSuccesses += (float) 1/iterations;}
            if (Shulker.killsUntilNShulker(seed, SHELLS_NEEDED, SHELLS_LOOTING, SHULKER_CUTOFF) <= SHULKER_CUTOFF) {shulkerSuccesses += (float) 1/iterations;}
            if (Blaze.killsUntilNRods(seed, RODS_NEEDED, RODS_LOOTING, ROD_CUTOFF) <= ROD_CUTOFF) {rodSuccesses += (float) 1/iterations;}
            if (Barter.bartersUntilDesired(seed, requiredDrops, BARTER_CUTOFF, false) <= BARTER_CUTOFF) {barterSuccesses += (float) 1/iterations;}
            if (ElderGuardian.killsUntilTideTrim(seed, ELDER_GUARDIAN_CUTOFF) <= ELDER_GUARDIAN_CUTOFF) {elderGuardianSuccesses += (float) 1/iterations;}
            if (Sniffer.digsUntilNTorchflowers(seed, TORCHFLOWERS_NEEDED, TORCHFLOWER_CUTOFF) <= TORCHFLOWER_CUTOFF) {torchflowerSuccesses += (float) 1/iterations;}
            if (OminousVault.ominousVaultsUntilHeavyCore(seed, OMINOUS_VAULT_CUTOFF) <= OMINOUS_VAULT_CUTOFF) {heavyCoreVaultSuccesses += (float) 1/iterations;}
        }

        System.out.println("Odds: ");
        System.out.println("Chance of " + SKULLS_NEEDED + " wither skulls in " + WITHER_CUTOFF + " kills: " + witherSuccesses * 100 + "%");
        System.out.println("Chance of " + SHELLS_NEEDED + " shulker shells in " + SHULKER_CUTOFF + " kills: " + shulkerSuccesses * 100 + "%");
        System.out.println("Chance of " + RODS_NEEDED + " blaze rods in " + ROD_CUTOFF + " kills: " + rodSuccesses * 100 + "%");
        System.out.println("Chance of " + PEARLS_NEEDED + " pearls and " + FIRE_RES_NEEDED + " fire res in " + BARTER_CUTOFF + " barters: " + barterSuccesses * 100 + "%");
        System.out.println("Chance of Tide Trim in " + ELDER_GUARDIAN_CUTOFF + " kills: " + elderGuardianSuccesses * 100 + "%");
        System.out.println("Chance of 3 torchflowers in " + TORCHFLOWER_CUTOFF + " digs: " + torchflowerSuccesses * 100 + "%");
        System.out.println("Chance of Heavy Core in " + OMINOUS_VAULT_CUTOFF + " vaults: " + heavyCoreVaultSuccesses * 100 + "%");

        // Total chance
        float totalChance = witherSuccesses * shulkerSuccesses * rodSuccesses * barterSuccesses * elderGuardianSuccesses * torchflowerSuccesses * heavyCoreVaultSuccesses;
        System.out.println("Total chance: " + String.format("%.7f", totalChance * 100) + "%");
    }

}
