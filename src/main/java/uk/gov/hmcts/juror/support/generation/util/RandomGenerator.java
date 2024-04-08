package uk.gov.hmcts.juror.support.generation.util;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;

public final class RandomGenerator {
    private static final UniformRandomProvider RANDOM;

    private RandomGenerator() {

    }

    static {
        RANDOM = RandomSource.XO_SHI_RO_512_SS.create();
    }

    public static int nextInt(int minInclusive, int maxExclusive) {
        return RANDOM.nextInt(minInclusive, maxExclusive);
    }

    public static long nextLong(long minInclusive, long maxExclusive) {
        return RANDOM.nextLong(minInclusive, maxExclusive);
    }

    public static double nextDouble(double minInclusive, double maxExclusive) {
        return RANDOM.nextDouble(minInclusive, maxExclusive);
    }
}