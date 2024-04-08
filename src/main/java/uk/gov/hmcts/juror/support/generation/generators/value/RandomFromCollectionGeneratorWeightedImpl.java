package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.generators.code.ManualGenerator;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@ManualGenerator
public class RandomFromCollectionGeneratorWeightedImpl<T> extends AbstractValueGenerator<T> {

    private final NavigableMap<Double, T> values;
    private double total;

    public RandomFromCollectionGeneratorWeightedImpl(Map<T, Double> weightedItems) {
        super(false);
        this.values = new TreeMap<>();
        this.total = 0;
        weightedItems.forEach(this::add);
    }

    public void add(T item, double weight) {
        if (weight <= 0) {
            return;
        }
        total += weight;
        values.put(total, item);
    }

    @Override
    protected T generateValue() {
        double randomValue = RandomGenerator.nextDouble(0.0, total);
        return values.higherEntry(randomValue).getValue();
    }
}
