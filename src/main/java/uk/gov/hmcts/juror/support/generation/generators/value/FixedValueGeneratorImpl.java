package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.Utils;

import javax.lang.model.element.VariableElement;

public class FixedValueGeneratorImpl<T> implements ValueGenerator<T> {
    private final T fixedValue;

    public FixedValueGeneratorImpl(T fixedValue) {
        this.fixedValue = fixedValue;
    }


    public static String createInitializationString(VariableElement element, FixedValueGenerator annotation) {
        return createInitializationStringWithValue(annotation.value(), Utils.getFieldType(element));
    }

    public static String createInitializationStringWithValue(String value, String type) {
        if ("java.lang.String".equals(type)) {
            return "new FixedValueGeneratorImpl<" + type + ">(\"" + value + "\")";
        }
        return "new FixedValueGeneratorImpl<" + type + ">(" + value + ")";
    }

    @Override
    public T generate() {
        return fixedValue;
    }
}
