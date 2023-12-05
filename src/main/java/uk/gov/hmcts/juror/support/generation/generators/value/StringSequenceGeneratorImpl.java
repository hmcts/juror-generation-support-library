package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.Utils;

import javax.lang.model.element.VariableElement;

public class StringSequenceGeneratorImpl extends AbstractValueGenerator<String> {


    private final String format;
    private final SequenceGeneratorImpl sequenceGenerator;

    public StringSequenceGeneratorImpl(String format, String id, long start) {
        super(false);
        this.format = format;
        this.sequenceGenerator = new SequenceGeneratorImpl(id, start);
    }

    @Override
    public String generateValue() {
        return String.format(format, sequenceGenerator.generate());
    }

    public static String createInitializationString(VariableElement element, StringSequenceGenerator annotation) {
        return "new StringSequenceGeneratorImpl(\"" + Utils.escape(annotation.format()) + "\", "
            + "\"" + annotation.sequenceGenerator().id() + "\", "
            + annotation.sequenceGenerator().start() + "L)";
    }
}
