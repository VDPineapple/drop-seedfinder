package com.vdpineapple;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

public class LootTableRNG {

    private static final HashFunction MD5 = Hashing.md5();
    private long seedLo;
    private long seedHi;

    // The same
    private final long seedLoHash;
    private final long seedHiHash;
    public LootTableRNG(String identifier) {
        byte[] bs = MD5.hashString(identifier, Charsets.UTF_8).asBytes();
        seedLoHash = Longs.fromBytes(bs[0], bs[1], bs[2], bs[3], bs[4], bs[5], bs[6], bs[7]);
        seedHiHash = Longs.fromBytes(bs[8], bs[9], bs[10], bs[11], bs[12], bs[13], bs[14], bs[15]);
    }

    // The same
    public void setSeed(long seed) {
        long l2 = seed ^ 0x6A09E667F3BCC909L;
        long l3 = l2 + -7046029254386353131L;
        // XOR happens before mixing in 1.20-pre5 and later
        this.seedLo = mixStafford13(l2 ^ seedLoHash);
        this.seedHi = mixStafford13(l3 ^ seedHiHash);

        if ((this.seedLo | this.seedHi) == 0L) {
            this.seedLo = -7046029254386353131L;
            this.seedHi = 7640891576956012809L;
        }
    }
    //        XoroshiroRandomSource(RandomSupport.upgradeSeedTo128bit       (l).xor(RandomSequence.seedForKey(resourceLocation)));
    //        XoroshiroRandomSource(RandomSupport.upgradeSeedTo128bitUnmixed(l).xor(RandomSequence.seedForKey(resourceLocation)).mixed());
    // 
    // The same
    public static long mixStafford13(long seed) {
        seed = (seed ^ seed >>> 30) * -4658895280553007687L;
        seed = (seed ^ seed >>> 27) * -7723592293110705685L;
        return seed ^ seed >>> 31;
    }


    // The same
    public long nextLong() {
        long l = this.seedLo;
        long m = this.seedHi;

        //return value only
        long n = Long.rotateLeft(l + m, 17) + l;

        //actual generator
        m ^= l;
        this.seedLo = Long.rotateLeft(l, 49) ^ m ^ m << 21;
        this.seedHi = Long.rotateLeft(m, 28);

        return n;
    }
    
    private long nextBits(int i) {
        return this.nextLong() >>> 64 - i;
    }
    public float nextFloat() {
        return (float)this.nextBits(24) * 5.9604645E-8F;
    }
    public double nextDouble() {
        return (double)this.nextBits(53) * 1.110223E-16F;
    }
    public int nextInt() {
        return (int)this.nextLong();
    }
    // The same
    public int nextInt(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        } else {
            long l = Integer.toUnsignedLong(this.nextInt());
            long m = l * (long)i;
            long n = m & 0xFFFFFFFFL;
            if (n < (long)i) {
                int n2 = Integer.remainderUnsigned(~i + 1, i);
                while (n < (long)n2) {
                    l = Integer.toUnsignedLong(this.nextInt());
                    m = l * (long)i;
                    n = m & 0xFFFFFFFFL;
                }
            }
            long o = m >> 32;
            return (int)o;
        }
    }

    public int nextIntIncl(int i, int j) {
        if (i >= j) {
            throw new IllegalArgumentException("Bound must be greater than origin");
        } else {
            int k = j - i;
            if (k > 0) {
                // j is inclusive
                return i + this.nextInt(k + 1);
            } else {
                return i;
            }
        }
    }
}