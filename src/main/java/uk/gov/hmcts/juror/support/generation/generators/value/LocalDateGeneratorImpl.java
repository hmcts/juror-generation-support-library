
package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import javax.lang.model.element.VariableElement;

public class LocalDateGeneratorImpl extends AbstractValueGenerator<LocalDate> {


    private final long maxExclusive;
    private final long minInclusive;

    public LocalDateGeneratorImpl(LocalDate minInclusive, LocalDate maxExclusive) {
        super(false);
        this.maxExclusive = maxExclusive.toEpochDay();
        this.minInclusive = minInclusive.toEpochDay();
    }

    @Override
    public LocalDate generateValue() {
        return LocalDate.ofEpochDay(RandomGenerator.nextLong(minInclusive, maxExclusive));
    }

    public static String createInitializationString(VariableElement element, LocalDateGenerator annotation) {

        return "new LocalDateGeneratorImpl(" + Utils.getLocalDateStringFromDateFilter(annotation.minInclusive())
            + ", " + Utils.getLocalDateStringFromDateFilter(annotation.maxExclusive()) + ")";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return LocalDateGenerator.class;
    }
}
