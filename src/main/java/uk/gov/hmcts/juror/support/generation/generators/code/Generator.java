package uk.gov.hmcts.juror.support.generation.generators.code;

import java.util.function.Consumer;

public interface Generator<T> {
    T generate();

    void setPostGenerate(Consumer<T> postGenerateConsumer);

    void postGenerate(T generated);
}
