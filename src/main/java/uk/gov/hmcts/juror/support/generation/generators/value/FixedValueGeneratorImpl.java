package uk.gov.hmcts.juror.support.generation.generators.value;

import javax.lang.model.element.VariableElement;

public class FixedValueGeneratorImpl implements ValueGenerator {
    private final Object fixedValue;
    private final Class<?> type;

    public FixedValueGeneratorImpl(Object fixedValue, Class<?> type) {
        this.fixedValue = fixedValue;
        this.type = type;
    }


    public static String createInitializationString(VariableElement element, FixedValueGenerator annotation) {
        return createInitializationStringWithValue(annotation.value(), element.asType().toString());
    }

    public static String createInitializationStringWithValue(String value, String type) {
        if (type.equals("java.lang.String")) {
            return "new FixedValueGeneratorImpl(\"" + value + "\", " + type + ".class)";
        }
        return "new FixedValueGeneratorImpl(" + value + ", " + type + ".class)";
    }

    @Override
    public Object generate() {
        return type.cast(fixedValue);
    }
}
