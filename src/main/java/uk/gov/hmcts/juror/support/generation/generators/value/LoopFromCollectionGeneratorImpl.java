package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.generators.code.ManualGenerator;

import java.util.Collection;
import java.util.List;

@ManualGenerator
public class LoopFromCollectionGeneratorImpl<T> extends AbstractValueGenerator<T> {

    private final List<T> values;

    private int index = 0;

    public LoopFromCollectionGeneratorImpl(Collection<T> items) {
        super(false);
        this.values = items.stream().toList();
    }

    @SafeVarargs
    public LoopFromCollectionGeneratorImpl(T... items) {
        this(List.of(items));
    }

    @Override
    protected synchronized T generateValue() {
        index++;
        if (index >= values.size()) {
            index = 0;
        }
        return values.get(index);
    }
}
