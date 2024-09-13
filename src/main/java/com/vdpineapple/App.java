package com.vdpineapple;
import java.io.*;

public class App {
    public static void main(String[] args) {
        // float best = Float.MAX_VALUE;
        // for (long seed = 0;; seed++) {
        //     float quality = Simulators.aaQuality(seed, best);
        //     if (quality < best) {
        //         best = quality;
        //         System.out.println("\nSeed found: " + seed + " with quality " + quality);
        //         Simulators.aaQualityReport(seed);
        //     }
        // }
        // long seed = 4748513;
        // long seed2 = 4748513 + 1 << 48;
        // Simulators.aaQualityReport(seed);
        // Simulators.aaQualityReport(seed2);
        String fileName = "/home/user/Documents/tas/templeVillageRPFastionShip.txt";
        String newFileName = "/home/user/Documents/tas/templeVillageRPFastionShipDrops.txt";
        BufferedReader reader = null;
        BufferedWriter writer = null;

        int structureSeedsDone = 0;
        int totalStructureSeeds = 433251;
        int hits = 0;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            writer = new BufferedWriter(new FileWriter(newFileName));
            String line = null;
            while ((line = reader.readLine()) != null) { // read the file line by line
                line = line.trim();
                long structureSeed = Long.parseLong(line, 10);
                // Each structure seed has 2^16 corresponding world seeds
                // These are found by adding 0-65535 to the first 16 bits of the structure seed
                for (long i = 0; i < 65536; i++) {
                    long worldSeed = structureSeed + (i << 48);
                    float quality = Simulators.aaQuality(worldSeed, Float.MAX_VALUE);
                    if (quality < 1000) {
                        hits++;
                        writer.write(Long.toString(worldSeed));
                        writer.newLine();
                        // flush changes to file
                        writer.flush();
                    }
                }

                structureSeedsDone++;
                if (structureSeedsDone % 1000 == 0) {
                    System.out.println("Structure seeds done: " + structureSeedsDone + " out of " + totalStructureSeeds + " with " + hits + " hits");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Simulators.chances(1000000);
    }
}

// mvn exec:java -Dexec.mainClass=com.vdpineapple.App