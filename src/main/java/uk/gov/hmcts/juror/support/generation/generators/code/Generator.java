package uk.gov.hmcts.juror.support.generation.generators.code;

import java.util.function.Consumer;

public abstract class Generator<T> {
    private Consumer<T> postGenerateConsumer;

    public abstract T generate();

    public final void setPostGenerate(Consumer<T> postGenerateConsumer) {
        this.postGenerateConsumer = postGenerateConsumer;
    }

    protected final void postGenerate(T generated) {
        if (postGenerateConsumer != null) {
            postGenerateConsumer.accept(generated);
        }
    }
}
