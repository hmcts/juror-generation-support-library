package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.NameGenerator;

import java.lang.annotation.Annotation;
import javax.lang.model.element.VariableElement;

public class LastNameGeneratorImpl extends RandomFromCollectionGeneratorImpl<String> {

    public LastNameGeneratorImpl() {
        super(NameGenerator.LAST_NAMES);
    }

    public static String createInitializationString(VariableElement element, LastNameGenerator annotation) {
        return "new LastNameGeneratorImpl()";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return LastNameGenerator.class;
    }
}
