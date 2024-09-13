package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.VariableElement;

public class RandomFromCollectionGeneratorImpl<T> extends AbstractValueGenerator<T> {

    private final List<T> values;

    public RandomFromCollectionGeneratorImpl(Collection<T> items) {
        super(false);
        this.values = items.stream().toList();
    }

    @SafeVarargs
    public RandomFromCollectionGeneratorImpl(T... items) {
        this(List.of(items));
    }

    @Override
    protected T generateValue() {
        return values.get(RandomGenerator.nextInt(0, values.size()));
    }

    public static String createInitializationString(VariableElement element, RandomFromCollectionGenerator annotation) {
        final String values;
        if (annotation.stringValues() != null && annotation.stringValues().length > 0) {
            values = "\"" + String.join("\", \"", annotation.stringValues()) + "\"";
        } else if (annotation.integerValues() != null && annotation.integerValues().length > 0) {
            values = String.join(", ", Utils.toStringArray(annotation.integerValues()));
        } else if (annotation.longValues() != null && annotation.longValues().length > 0) {
            values = String.join(", ", Utils.toStringArray(annotation.longValues()));
        } else {
            values = "";
        }
        return "new RandomFromCollectionGeneratorImpl(" + values + ")";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return RandomFromCollectionGenerator.class;
    }
}
