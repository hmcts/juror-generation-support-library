package uk.gov.hmcts.juror.support.generation.generators.code;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Generator<T> {

    private final List<Consumer<T>> postGenerateList;

    protected Generator(){
        this.postGenerateList = new ArrayList<>();
    }
    public abstract T generate();

    public void addPostGenerate(Consumer<T> postGenerateConsumer){
        this.postGenerateList.add(postGenerateConsumer);
    }
    public void clearPostGenerate(Consumer<T> postGenerateConsumer){
        this.postGenerateList.clear();
    }

    public void postGenerate(T generated){
        postGenerateList.forEach(postGenerate -> postGenerate.accept(generated));
    }
}
