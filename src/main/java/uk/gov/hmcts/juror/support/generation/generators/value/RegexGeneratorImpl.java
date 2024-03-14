package uk.gov.hmcts.juror.support.generation.generators.value;

import com.github.curiousoddman.rgxgen.RgxGen;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import javax.lang.model.element.VariableElement;

public class RegexGeneratorImpl extends AbstractValueGenerator<String> {

    final RgxGen regex;

    public RegexGeneratorImpl(boolean forceUnique, String regex) {
        super(forceUnique);
        this.regex =  RgxGen.parse(regex);
    }

    @Override
    protected String generateValue() {
        return regex.generate();
    }


    public static String createInitializationString(VariableElement element, RegexGenerator annotation) {
        return "new RegexGeneratorImpl(" + annotation.forceUnique() + ", \"" + Utils.escape(annotation.regex()) + "\")";
    }

    public static Class<? extends Annotation> getAnnotation() {
        return RegexGenerator.class;
    }
}
