package uk.gov.hmcts.juror.support.generation.generators.value;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractValueGenerator<T> implements ValueGenerator<T> {

    private final Set<T> generatedItems;
    private final boolean forceUnique;

    protected AbstractValueGenerator(boolean forceUnique) {
        this.forceUnique = forceUnique;
        this.generatedItems = new HashSet<>();
    }

    protected abstract T generateValue();

    @Override
    public final T generate() {
        T value = generateValue();
        if (forceUnique) {
            while (isNotUnique(value)) {
                value = generateValue();
            }
            generatedItems.add(value);
        }
        return value;
    }

    private boolean isNotUnique(T value) {
        return generatedItems.contains(value);
    }
}
