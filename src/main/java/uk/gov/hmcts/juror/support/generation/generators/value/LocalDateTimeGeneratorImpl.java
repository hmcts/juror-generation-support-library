
package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.time.LocalDateTime;
import javax.lang.model.element.VariableElement;

public class LocalDateTimeGeneratorImpl extends AbstractValueGenerator<LocalDateTime> {


    private final LocalDateGeneratorImpl localDateGenerator;
    private final LocalTimeGeneratorImpl localTimeGenerator;

    public LocalDateTimeGeneratorImpl(LocalDateTime minInclusive, LocalDateTime maxExclusive) {
        super(false);
        this.localDateGenerator = new LocalDateGeneratorImpl(minInclusive.toLocalDate(), maxExclusive.toLocalDate());
        this.localTimeGenerator = new LocalTimeGeneratorImpl(minInclusive.toLocalTime(), maxExclusive.toLocalTime());
    }

    @Override
    public LocalDateTime generateValue() {
        return LocalDateTime.of(localDateGenerator.generateValue(), localTimeGenerator.generateValue());

    }

    public static String createInitializationString(VariableElement element, LocalDateTimeGenerator annotation) {

        return "new LocalDateTimeGeneratorImpl(" + Utils.getLocalDateTimeStringFromDateTimeFilter(
            annotation.minInclusive())
            + ", " + Utils.getLocalDateTimeStringFromDateTimeFilter(annotation.maxExclusive()) + ")";
    }
}
