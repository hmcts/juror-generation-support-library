package uk.gov.hmcts.juror.support.generation.util;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;

import java.util.Map;

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

    public static boolean nextBoolean() {
        return nextBoolean(0.5, 0.5);
    }

    public static boolean nextBoolean(double trueWeight, double falseWeight) {
        return new RandomFromCollectionGeneratorWeightedImpl<>(
            Map.of(true, trueWeight, false, falseWeight))
            .generate();
    }
}