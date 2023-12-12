
package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.lang.model.element.VariableElement;

public class FormattedLocalDateTimeGeneratorImpl extends AbstractValueGenerator<String> {

    private final LocalDateTimeGeneratorImpl generator;
    private final DateTimeFormatter formatter;

    public FormattedLocalDateTimeGeneratorImpl(String format, LocalDateTime minInclusive, LocalDateTime maxExclusive) {
        super(false);
        generator = new LocalDateTimeGeneratorImpl(minInclusive, maxExclusive);
        formatter = DateTimeFormatter.ofPattern(format);
    }

    @Override
    public String generateValue() {
        return generator.generateValue().format(formatter);
    }

    public static String createInitializationString(VariableElement element,
                                                    FormattedLocalDateTimeGenerator annotation) {

        return "new FormattedLocalDateTimeGeneratorImpl("
            + "\"" + Utils.escape(annotation.format()) +  "\", "
            + Utils.getLocalDateTimeStringFromDateTimeFilter(annotation.generator().minInclusive())
            + ", " + Utils.getLocalDateTimeStringFromDateTimeFilter(annotation.generator().maxExclusive()) + ")";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return FormattedLocalDateTimeGenerator.class;
    }
}
