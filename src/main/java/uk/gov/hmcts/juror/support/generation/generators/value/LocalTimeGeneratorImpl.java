
package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.time.LocalTime;
import javax.lang.model.element.VariableElement;

public class LocalTimeGeneratorImpl extends AbstractValueGenerator<LocalTime> {

    private final long maxExclusive;
    private final long minInclusive;

    public LocalTimeGeneratorImpl(LocalTime minInclusive, LocalTime maxExclusive) {
        super(false);
        this.maxExclusive = maxExclusive.toNanoOfDay();
        this.minInclusive = minInclusive.toNanoOfDay();
    }

    @Override
    public LocalTime generateValue() {
        return LocalTime.ofNanoOfDay(RandomGenerator.nextLong(minInclusive, maxExclusive));
    }

    public static String createInitializationString(VariableElement element, LocalTimeGenerator annotation) {

        return "new LocalTimeGeneratorImpl(" + Utils.getLocalDateTimeStringFromTimeFilter(annotation.minInclusive())
            + ", " + Utils.getLocalDateTimeStringFromTimeFilter(annotation.maxExclusive()) + ")";
    }
}
