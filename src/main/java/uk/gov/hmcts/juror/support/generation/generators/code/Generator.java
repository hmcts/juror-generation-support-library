package uk.gov.hmcts.juror.support.generation.generators.code;

import java.util.function.Consumer;

public abstract class Generator<T> {
    private Consumer<T> postGenerate;
    public abstract T generate();

    public final void setPostGenerate(Consumer<T> postGenerate) {
        this.postGenerate = postGenerate;
    }
    protected final void postGenerate(T generated) {
        if (postGenerate != null) {
            postGenerate.accept(generated);
        }
    }
}
