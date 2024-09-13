package com.vdpineapple.simulators;
import com.vdpineapple.LootTableRNG;
import java.util.HashMap;


public class Barter {
    public static HashMap<String, Integer> barter(LootTableRNG rng) {
        int roulette = rng.nextInt(459);
        String item = "";
        int amount = 1;
        if (roulette < 5) {
            item = "Soul Speed Book";
            rng.nextLong();
            rng.nextLong();
        } else if (roulette < 13) {
            item = "Iron Boots";
            rng.nextLong();
            rng.nextLong();
        } else if (roulette < 21) {
            item = "Fire Resistance Potion";
        } else if (roulette < 29) {
            item = "Splash Fire Resistance Potion";
        } else if (roulette < 39) {
            item = "Water Bottle";
        } else if (roulette < 49) {
            item = "Iron Nugget";
            amount = rng.nextIntIncl(10, 36);
        } else if (roulette < 59) {
            item = "Ender Pearl";
            amount = rng.nextIntIncl(2, 4);
        } else if (roulette < 79) {
            item = "String";
            amount = rng.nextIntIncl(3, 9);
        } else if (roulette < 99) {
            item = "Quartz";
            amount = rng.nextIntIncl(5, 12);
        } else if (roulette < 139) {
            item = "Obsidian";
        } else if (roulette < 179) {
            item = "Crying Obsidian";
            amount = rng.nextIntIncl(1, 3);
        } else if (roulette < 219) {
            item = "Fire Charge";
        } else if (roulette < 259) {
            item = "Leather";
            amount = rng.nextIntIncl(2, 4);
        } else if (roulette < 299) {
            item = "Soul Sand";
            amount = rng.nextIntIncl(2, 8);
        } else if (roulette < 339) {
            item = "Nether Brick";
            amount = rng.nextIntIncl(2, 8);
        } else if (roulette < 379) {
            item = "Spectral Arrow";
            amount = rng.nextIntIncl(6, 12);
        } else if (roulette < 419) {
            item = "Gravel";
            amount = rng.nextIntIncl(8, 16);
        } else {
            item = "Blackstone";
            amount = rng.nextIntIncl(8, 16);
        }
        HashMap<String, Integer> trade = new HashMap<String, Integer>();
        trade.put(item, amount);
        return trade;
    }

    private static boolean allBartersSatified(HashMap<String, Integer> items, HashMap<String, Integer> desiredItems) {
        for (String item : desiredItems.keySet()) {
            if (items.get(item) < desiredItems.get(item)) {
                return false;
            }
        }
        return true;
    }

    public static int bartersUntilDesired(long seed, HashMap<String, Integer> desiredItems, int cutoff, boolean verbose) {
        LootTableRNG rng = new LootTableRNG("minecraft:gameplay/piglin_bartering");
        rng.setSeed(seed);
        HashMap<String, Integer> items = new HashMap<String, Integer>();
        String[] possible_items = {
            "Soul Speed Book", "Iron Boots", "Fire Resistance Potion", "Splash Fire Resistance Potion", "Water Bottle",
            "Iron Nugget", "Ender Pearl", "String", "Quartz", "Obsidian", "Crying Obsidian", "Fire Charge", "Leather",
            "Soul Sand", "Nether Brick", "Spectral Arrow", "Gravel", "Blackstone"
        };
        for (String item : possible_items) {
            items.put(item, 0);
        }

        int tradeCount = 1;
        while (!allBartersSatified(items, desiredItems)) {
            HashMap<String, Integer> trade = barter(rng);
            String item = trade.keySet().toArray()[0].toString();
            int amount = trade.get(trade.keySet().toArray()[0]);
            items.put(item, items.get(item) + amount);

            if (verbose) {
                if (amount == 0) {
                    System.out.println("Trade " + tradeCount + ": " + item);
                } else {
                    System.out.println("Trade " + tradeCount + ": " + item + " x" + amount);
                }
            }
            tradeCount++;
            if (tradeCount > cutoff) {
                return Integer.MAX_VALUE;
            }
        }

        // Print all the items
        if (verbose) {
            for (String item : items.keySet()) {
                System.out.println(item + ": " + items.get(item));
            }
        }
        
        return tradeCount;
    }



    public static int pearlsFromNTrades(long seed, int numTrades) {
        LootTableRNG rng = new LootTableRNG("minecraft:gameplay/piglin_bartering");
        rng.setSeed(seed);
        int pearls = 0;
        for (int i = 0; i < numTrades; i++) {
            HashMap<String, Integer> trade = barter(rng);
            if (trade.keySet().toArray()[0].toString().equals("Ender Pearl")) {
                pearls += trade.get(trade.keySet().toArray()[0]);
            }
        }
        return pearls;
    }
}
