package uk.gov.hmcts.juror.support.generation.generators.value;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.lang.model.element.VariableElement;

public class SequenceGeneratorImpl extends AbstractValueGenerator<Long> {

    private static final Map<String, AtomicLong> COUNTER_MAP;

    private final AtomicLong counter;

    static {
        COUNTER_MAP = new HashMap<>();
    }

    public SequenceGeneratorImpl(String id, long start) {
        super(true);
        this.counter = COUNTER_MAP.computeIfAbsent(id, k -> new AtomicLong(start));
    }

    @Override
    public Long generateValue() {
        return counter.getAndIncrement();
    }

    public static String createInitializationString(VariableElement element, SequenceGenerator annotation) {
        return "new SequenceGeneratorImpl(\"" + annotation.id() + "\", " + annotation.start() + "L)";
    }
}
