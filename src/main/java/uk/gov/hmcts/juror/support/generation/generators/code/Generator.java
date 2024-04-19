package uk.gov.hmcts.juror.support.generation.generators.code;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Generator<T> {

    private final List<Function<T, T>> postGenerateList;

    protected Generator() {
        this.postGenerateList = new ArrayList<>();
    }

    public abstract T generate();

    public List<T> generate(long count) {
        List<T> generated = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            generated.add(generate());
        }
        return generated;
    }

    public Generator<T> addPostGenerate(Function<T, T> postGenerateConsumer) {
        this.postGenerateList.add(postGenerateConsumer);
        return this;
    }

    public Generator<T> addPostGenerate(Consumer<T> postGenerateConsumer) {
        this.addPostGenerate(t -> {
            postGenerateConsumer.accept(t);
            return t;
        });
        return this;
    }

    public Generator<T> clearPostGenerate() {
        this.postGenerateList.clear();
        return this;
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    public T postGenerate(T generated) {
        for (Function<T, T> postGenerate : postGenerateList) {
            generated = postGenerate.apply(generated);
        }
        return generated;
    }
}
