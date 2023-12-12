package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import javax.lang.model.element.VariableElement;

public class NullValueGeneratorImpl<T> implements ValueGenerator<T> {


    public static String createInitializationString(VariableElement element, FixedValueGenerator annotation) {
        return "new NullValueGeneratorImpl<" + Utils.getFieldType(element) + ">()";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return FixedValueGenerator.class;
    }

    @Override
    public T generate() {
        return null;
    }
}
