
package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.lang.model.element.VariableElement;

public class FormattedLocalDateGeneratorImpl extends AbstractValueGenerator<String> {

    private final LocalDateGeneratorImpl generator;
    private final DateTimeFormatter formatter;

    public FormattedLocalDateGeneratorImpl(String format, LocalDate minInclusive, LocalDate maxExclusive) {
        super(false);
        generator = new LocalDateGeneratorImpl(minInclusive, maxExclusive);
        formatter = DateTimeFormatter.ofPattern(format);
    }

    @Override
    public String generateValue() {
        return generator.generateValue().format(formatter);
    }

    public static String createInitializationString(VariableElement element,
                                                    FormattedLocalDateGenerator annotation) {

        return "new FormattedLocalDateGeneratorImpl("
            + "\"" + Utils.escape(annotation.format()) + "\", "
            + Utils.getLocalDateStringFromDateFilter(annotation.generator().minInclusive())
            + ", " + Utils.getLocalDateStringFromDateFilter(annotation.generator().maxExclusive()) + ")";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return FormattedLocalDateGenerator.class;
    }
}
