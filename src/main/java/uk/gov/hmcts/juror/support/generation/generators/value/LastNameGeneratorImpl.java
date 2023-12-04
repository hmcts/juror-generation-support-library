package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.NameGenerator;

import javax.lang.model.element.VariableElement;

public class LastNameGeneratorImpl extends AbstractValueGenerator<String> {

    public LastNameGeneratorImpl() {
        super(false);
    }

    @Override
    protected String generateValue() {
        return NameGenerator.generateLastName();
    }

    public static String createInitializationString(VariableElement element, LastNameGenerator annotation) {
        return "new LastNameGeneratorImpl()";
    }
}
