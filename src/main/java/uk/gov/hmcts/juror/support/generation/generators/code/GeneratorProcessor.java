package uk.gov.hmcts.juror.support.generation.generators.code;


import uk.gov.hmcts.juror.support.generation.generators.value.EmailGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.EmailGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.IntRangeGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.IntRangeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateTimeGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalTimeGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.StringSequenceGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.StringSequenceGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.ValueGenerator;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes("uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class GeneratorProcessor extends AbstractProcessor {

    private static final Map<Class<? extends Annotation>, BiFunction<VariableElement, Annotation, String>>
        ANNOTATION_TO_GENERATOR;

    private static final Map<Class<? extends Annotation>, Class<? extends ValueGenerator>>
        ANNOTATION_TO_GENERATOR_CLASS;

    public static void addGenerator(Class<? extends Annotation> annotation,
                                    Class<? extends ValueGenerator> generatorClass,
                                    BiFunction<VariableElement, Annotation, String> generator) {
        ANNOTATION_TO_GENERATOR_CLASS.put(annotation, generatorClass);
        ANNOTATION_TO_GENERATOR.put(annotation, generator);
    }

    static {
        ANNOTATION_TO_GENERATOR_CLASS = new HashMap<>();
        ANNOTATION_TO_GENERATOR = new HashMap<>();
        addGenerator(
            FirstNameGenerator.class, FirstNameGeneratorImpl.class,
            (element, annotation) -> FirstNameGeneratorImpl.createInitializationString(element,
                (FirstNameGenerator) annotation));
        addGenerator(
            LastNameGenerator.class, LastNameGeneratorImpl.class,
            (element, annotation) -> LastNameGeneratorImpl.createInitializationString(element,
                (LastNameGenerator) annotation));
        addGenerator(FixedValueGenerator.class, FixedValueGeneratorImpl.class,
            (element, annotation) -> FixedValueGeneratorImpl.createInitializationString(element,
                (FixedValueGenerator) annotation));
        addGenerator(IntRangeGenerator.class, IntRangeGeneratorImpl.class,
            (element, annotation) -> IntRangeGeneratorImpl.createInitializationString(element,
                (IntRangeGenerator) annotation));
        addGenerator(RegexGenerator.class, RegexGeneratorImpl.class,
            (element, annotation) -> RegexGeneratorImpl.createInitializationString(element,
                (RegexGenerator) annotation));
        addGenerator(SequenceGenerator.class, SequenceGeneratorImpl.class,
            (element, annotation) -> SequenceGeneratorImpl.createInitializationString(element,
                (SequenceGenerator) annotation));
        addGenerator(StringSequenceGenerator.class, StringSequenceGeneratorImpl.class,
            (element, annotation) -> StringSequenceGeneratorImpl.createInitializationString(element,
                (StringSequenceGenerator) annotation));
        addGenerator(LocalDateGenerator.class, LocalDateGeneratorImpl.class,
            (element, annotation) -> LocalDateGeneratorImpl.createInitializationString(element,
                (LocalDateGenerator) annotation));
        addGenerator(LocalTimeGenerator.class, LocalTimeGeneratorImpl.class,
            (element, annotation) -> LocalTimeGeneratorImpl.createInitializationString(element,
                (LocalTimeGenerator) annotation));
        addGenerator(LocalDateTimeGenerator.class, LocalDateTimeGeneratorImpl.class,
            (element, annotation) -> LocalDateTimeGeneratorImpl.createInitializationString(element,
                (LocalDateTimeGenerator) annotation));
        addGenerator(RandomFromFileGenerator.class, RandomFromFileGeneratorImpl.class,
            (element, annotation) -> RandomFromFileGeneratorImpl.createInitializationString(element,
                (RandomFromFileGenerator) annotation));
        addGenerator(EmailGenerator.class, EmailGeneratorImpl.class,
            (element, annotation) -> EmailGeneratorImpl.createInitializationString(element,
                (EmailGenerator) annotation));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(typeElement ->
            roundEnv.getElementsAnnotatedWith(typeElement)
                .forEach(this::processGenerateGenerationConfigAnnotation));
        return true;
    }

    private void processGenerateGenerationConfigAnnotation(Element element) {
        try {
            final String className = element.getSimpleName().toString();
            final String packageName = element.getEnclosingElement().toString();

            GenerationClass generationClass = new GenerationClass(className + "Generator", packageName);
            generationClass.addInterface("uk.gov.hmcts.juror.support.generation.generators.code.Generator", packageName + "." + className);
            List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
            fields.forEach(field -> processField(field, generationClass));

            buildGenerateMethod(generationClass, className);

            generationClass.build(processingEnv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void buildGenerateMethod(GenerationClass generationClass, String className) {
        GenerationClass.GenerationMethod generationMethod = new GenerationClass.GenerationMethod(
            GenerationClass.Visibility.PUBLIC,
            "generate",
            className,
            className + " generated = new " + className + "();" + GenerationClass.NEW_LINE
                + generationClass.getFields().stream()
                .map(field -> "generated.set" + GenerationClass.capitalise(field.name())
                    + "(this." + field.name() + ".generate()" + ");")
                .reduce((s1, s2) -> s1 + GenerationClass.NEW_LINE + s2)
                .orElse("") + GenerationClass.NEW_LINE + "return generated;",
            null
        );
        generationClass.addMethod(generationMethod);
    }

    private void processField(VariableElement field, GenerationClass generationClass) {
        boolean annotationFound = false;
        for (Map.Entry<Class<? extends Annotation>, BiFunction<VariableElement, Annotation, String>> generatorAnnotation : ANNOTATION_TO_GENERATOR.entrySet()) {
            Annotation annotation = field.getAnnotation(generatorAnnotation.getKey());
            if (annotation == null) {
                continue;
            }
            if (annotationFound) {
                throw new IllegalArgumentException("Only one generator annotation is allowed per field");
            }
            String initializationString = generatorAnnotation.getValue().apply(field, annotation);

            GenerationClass.GenerationField generationField = new GenerationClass.GenerationField(
                GenerationClass.Visibility.PRIVATE,
                field.getSimpleName().toString(),
                "ValueGenerator<" + Utils.getFieldType(field) + ">",
                initializationString
            );
            generationClass.addField(generationField, true, true);
            generationClass.addImport(ANNOTATION_TO_GENERATOR_CLASS.get(generatorAnnotation.getKey()));
            annotationFound = true;
        }
        if (!annotationFound) {
            if (isPrimitive(field)) {
                addPrimitiveField(field, generationClass);
            } else {
                addNullField(field, generationClass);
            }
        }
        generationClass.addImport(ValueGenerator.class);
    }

    private boolean isPrimitive(VariableElement field) {
        String fieldType = field.asType().toString();
        return switch (fieldType) {
            case "int", "boolean", "long", "double", "float", "short", "byte", "char" -> true;
            default -> false;
        };
    }

    private void addPrimitiveField(VariableElement field, GenerationClass generationClass) {
        GenerationClass.GenerationField generationField = new GenerationClass.GenerationField(
            GenerationClass.Visibility.PRIVATE,
            field.getSimpleName().toString(),
            "ValueGenerator<" + Utils.getFieldType(field) + ">",
            FixedValueGeneratorImpl.createInitializationStringWithValue(
                getPrimitiveDefaultValue(field.asType().toString()), Utils.getFieldType(field))
        );
        generationClass.addField(generationField, true, true);
        generationClass.addImport(FixedValueGeneratorImpl.class);
    }

    private String getPrimitiveDefaultValue(String string) {
        return switch (string) {
            case "int" -> "(java.lang.Integer) 0";
            case "short" -> "(java.lang.Short) 0";
            case "byte" -> "(java.lang.Byte) 0";
            case "boolean" -> "(java.lang.Boolean) false";
            case "long" -> "(java.lang.Long) 0L";
            case "double" -> "(java.lang.Double) 0.0";
            case "float" -> "(java.lang.Float) 0.0f";
            case "char" -> "'\u0000'";
            default -> throw new IllegalArgumentException("Not a primitive type");
        };
    }

    private void addNullField(VariableElement field, GenerationClass generationClass) {
        GenerationClass.GenerationField generationField = new GenerationClass.GenerationField(
            GenerationClass.Visibility.PRIVATE,
            field.getSimpleName().toString(),
            "ValueGenerator<" + Utils.getFieldType(field) + ">",
            NullValueGeneratorImpl.createInitializationString(field, null)
        );
        generationClass.addField(generationField, true, true);
        generationClass.addImport(NullValueGeneratorImpl.class);
    }
}
