package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.NameGenerator;

import javax.lang.model.element.VariableElement;

public class LastNameGeneratorImpl extends RandomFromCollectionGeneratorImpl<String> {

    public LastNameGeneratorImpl() {
        super(NameGenerator.LAST_NAMES);
    }

    public static String createInitializationString(VariableElement element, LastNameGenerator annotation) {
        return "new LastNameGeneratorImpl()";
    }
}
