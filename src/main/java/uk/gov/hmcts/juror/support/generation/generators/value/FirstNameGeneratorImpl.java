package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.NameGenerator;

import java.lang.annotation.Annotation;
import javax.lang.model.element.VariableElement;

public class FirstNameGeneratorImpl extends RandomFromCollectionGeneratorImpl<String> {

    public FirstNameGeneratorImpl() {
        super(NameGenerator.FIRST_NAMES);
    }

    public static String createInitializationString(VariableElement element, FirstNameGenerator annotation) {
        return "new FirstNameGeneratorImpl()";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return FirstNameGenerator.class;
    }
}
