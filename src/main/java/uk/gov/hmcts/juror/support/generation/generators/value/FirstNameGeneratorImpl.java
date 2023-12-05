package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.NameGenerator;

import javax.lang.model.element.VariableElement;

public class FirstNameGeneratorImpl extends AbstractValueGenerator<String> {

    public FirstNameGeneratorImpl() {
        super(false);
    }

    @Override
    public String generateValue() {
        return NameGenerator.generateFirstName();
    }

    public static String createInitializationString(VariableElement element, FirstNameGenerator annotation) {
        return "new FirstNameGeneratorImpl()";
    }
}
