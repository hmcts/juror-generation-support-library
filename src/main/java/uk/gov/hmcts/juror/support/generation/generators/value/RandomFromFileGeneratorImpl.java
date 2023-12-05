package uk.gov.hmcts.juror.support.generation.generators.value;

import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.generation.util.ResourceUtils;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import javax.lang.model.element.VariableElement;

public class RandomFromFileGeneratorImpl extends RandomFromCollectionGeneratorImpl<String> {


    public RandomFromFileGeneratorImpl(String fileLocation) {
        super(ResourceUtils.readLinesFromResourceFile(fileLocation));
    }
    
    public static String createInitializationString(VariableElement element, RandomFromFileGenerator annotation) {
        return "new RandomFromFileGeneratorImpl(\"" + Utils.escape(annotation.file()) + "\")";
    }
}
