package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;

import java.lang.annotation.Annotation;
import javax.lang.model.element.VariableElement;

public class IntRangeGeneratorImpl extends AbstractValueGenerator<Integer> {

    final int minInclusive;
    final int maxExclusive;

    public IntRangeGeneratorImpl(boolean forceUnique, int minInclusive, int maxExclusive) {
        super(forceUnique);
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
    }

    @Override
    protected Integer generateValue() {
        return RandomGenerator.nextInt(minInclusive, maxExclusive);
    }

    public static String createInitializationString(VariableElement element, IntRangeGenerator annotation) {
        return "new IntRangeGeneratorImpl(" + annotation.forceUnique() + ", "
            + annotation.minInclusive() + ", " + annotation.maxExclusive() + ")";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return IntRangeGenerator.class;
    }
}
