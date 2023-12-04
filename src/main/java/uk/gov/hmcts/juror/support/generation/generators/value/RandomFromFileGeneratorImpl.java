package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.generation.util.ResourceUtils;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.util.List;
import javax.lang.model.element.VariableElement;

public class RandomFromFileGeneratorImpl extends AbstractValueGenerator<String> {

    private final List<String> values;

    public RandomFromFileGeneratorImpl(String fileLocation) {
        super(false);
        this.values = ResourceUtils.readLinesFromResourceFile(fileLocation);
    }

    @Override
    protected String generateValue() {
        return values.get(RandomGenerator.nextInt(0, values.size()));
    }

    public static String createInitializationString(VariableElement element, RandomFromFileGenerator annotation) {
        return "new RandomFromFileGeneratorImpl(\"" + Utils.escape(annotation.file()) + "\")";
    }
}
