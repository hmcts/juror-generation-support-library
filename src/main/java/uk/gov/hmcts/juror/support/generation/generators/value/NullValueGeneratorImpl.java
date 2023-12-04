package uk.gov.hmcts.juror.support.generation.generators.value;

import javax.lang.model.element.VariableElement;

public class NullValueGeneratorImpl implements ValueGenerator {


    public static String createInitializationString(VariableElement element, FixedValueGenerator annotation) {
        return "new NullValueGeneratorImpl()";
    }

    @Override
    public Object generate() {
        return null;
    }
}
