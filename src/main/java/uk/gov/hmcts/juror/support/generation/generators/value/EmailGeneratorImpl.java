package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.NameGenerator;

import java.lang.annotation.Annotation;
import javax.lang.model.element.VariableElement;

public class EmailGeneratorImpl extends AbstractValueGenerator<String> {

    private final RegexGeneratorImpl emailSuffixRegexGen;

    public EmailGeneratorImpl() {
        super(false);
        emailSuffixRegexGen = new RegexGeneratorImpl(false, "[a-z]{2,5}\\.(com|net|co\\.uk)");
    }

    @Override
    public String generateValue() {
        return NameGenerator.generateFirstName() + "." + NameGenerator.generateLastName() + "@"
            + emailSuffixRegexGen.generateValue();
    }

    public static String createInitializationString(VariableElement element, EmailGenerator annotation) {
        return "new EmailGeneratorImpl()";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return EmailGenerator.class;
    }
}
