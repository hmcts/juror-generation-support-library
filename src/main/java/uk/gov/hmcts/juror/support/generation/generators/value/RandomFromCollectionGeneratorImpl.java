package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.util.Collection;
import java.util.List;
import javax.lang.model.element.VariableElement;

public class RandomFromCollectionGeneratorImpl<T> extends AbstractValueGenerator<T> {

    private final List<T> values;

    public RandomFromCollectionGeneratorImpl(Collection<T> items) {
        super(false);
        this.values = items.stream().toList();
    }

    @Override
    protected T generateValue() {
        return values.get(RandomGenerator.nextInt(0, values.size()));
    }
}
