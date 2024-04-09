package uk.gov.hmcts.juror.support.generation.generators.code;


import lombok.SneakyThrows;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.ValueGenerator;
import uk.gov.hmcts.juror.support.generation.util.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes("uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SuppressWarnings({
    "PMD.ExcessiveImports",
    "PMD.CouplingBetweenObjects",
    "PMD.TooManyMethods"
})
public class GeneratorProcessor extends AbstractProcessor {

    public static final String GENERATED_CLASS_SUFFIX = "Generator";
    private static final Map<Class<? extends Annotation>, BiFunction<VariableElement, Annotation, String>>
        ANNOTATION_TO_GENERATOR;

    private static final Map<Class<? extends Annotation>, Class<? extends ValueGenerator>>
        ANNOTATION_TO_GENERATOR_CLASS;

    @SuppressWarnings("unchecked")
    private static void addGenerator(Class<? extends ValueGenerator> clazz) {
        System.out.println("Adding generator: " + clazz);
        try {
            Method getAnnotationTypeMethod = clazz.getMethod("getAnnotation");
            Class<? extends Annotation> annotationType = (Class<? extends Annotation>) getAnnotationTypeMethod
                .invoke(null);
            Method createInitializationStringMethod = clazz.getMethod("createInitializationString",
                VariableElement.class, annotationType);

            ANNOTATION_TO_GENERATOR_CLASS.put(annotationType, clazz);
            ANNOTATION_TO_GENERATOR.put(annotationType, (element, annotation) -> {
                try {
                    return (String) createInitializationStringMethod.invoke(null, element, annotation);
                } catch (Exception e) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load generator: " + clazz.getName());
        }
    }

    static {
        ANNOTATION_TO_GENERATOR_CLASS = new HashMap<>();
        ANNOTATION_TO_GENERATOR = new HashMap<>();
        Utils.getGenerators().forEach(GeneratorProcessor::addGenerator);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.forEach(typeElement ->
            roundEnv.getElementsAnnotatedWith(typeElement)
                .forEach(this::processGenerateGenerationConfigAnnotation));
        return true;
    }

    @SneakyThrows
    private void processGenerateGenerationConfigAnnotation(Element element) {
        final String className = element.getSimpleName().toString();
        final String packageName = element.getEnclosingElement().toString();
        GenerationClass generationClass = new GenerationClass(className + GENERATED_CLASS_SUFFIX, packageName);

        boolean hasSuperClass = Utils.hasSuperClass(element);
        if (element.getModifiers().contains(Modifier.ABSTRACT)) {
            generationClass.setAbstract(true);
            generationClass.addClassGeneric("T extends " + className);
            if (!hasSuperClass) {
                generationClass.addImport(Generator.class);
                generationClass.addExtends(GENERATED_CLASS_SUFFIX, "T");
            }
        } else {
            if (!hasSuperClass) {
                generationClass.addImport(Generator.class);
                generationClass.addExtends(GENERATED_CLASS_SUFFIX, className);
            }
        }

        processElement(generationClass, element);
        if (hasSuperClass) {
            TypeElement superClassTypeElement = Utils.getSuperClassTypeElement(element);
            assert superClassTypeElement != null;
            generationClass.addExtends(Utils.getClassNameFromTypeElement(superClassTypeElement)
                    + GENERATED_CLASS_SUFFIX,
                generationClass.isAbstract() ? "T" : className
            );
        }
        addStandardMethods(generationClass, className);
        generationClass.build(processingEnv);
    }

    private void processElement(GenerationClass generationClass, Element element) {
        ElementFilter.fieldsIn(element.getEnclosedElements()).forEach(field -> processField(field, generationClass));
    }


    private void addStandardMethods(GenerationClass generationClass, String className) {
        addPopulateMethod(generationClass, className);
        if (!generationClass.isAbstract()) {
            addGenerateMethod(generationClass, className);
        }
    }

    private void addGenerateMethod(GenerationClass generationClass, String className) {
        GenerationClass.GenerationMethod generationMethod = new GenerationClass.GenerationMethod(
            GenerationClass.Visibility.PUBLIC,
            "generate",
            className,
            className + " generated = new " + className + "();"
                + GenerationClass.NEW_LINE
                + GenerationClass.NEW_LINE + "return populate(generated);",
            null
        );
        generationClass.addMethod(generationMethod);
    }


    private void addPopulateMethod(GenerationClass generationClass, String className) {
        GenerationClass.GenerationMethod populateMethod = new GenerationClass.GenerationMethod(
            GenerationClass.Visibility.PUBLIC,
            "populate",
            className,
            (generationClass.hasSuperClass() ? "super.populate(generated);" + GenerationClass.NEW_LINE : "")
                + generationClass.getFields().stream()
                .map(field -> "generated.set" + Utils.capitalise(field.name())
                    + "(this." + field.name() + ".generate()" + ");")
                .reduce((s1, s2) -> s1 + GenerationClass.NEW_LINE + s2)
                .orElse("")
                + (generationClass.isAbstract() ? "" : GenerationClass.NEW_LINE + "postGenerate(generated);")
                + GenerationClass.NEW_LINE + "return generated;",
            Map.of(className, "generated")
        );
        generationClass.addMethod(populateMethod);
    }


    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")//Required
    private void processField(VariableElement field, GenerationClass generationClass) {
        if (generationClass.hasField(field.getSimpleName().toString())) {
            return;
        }
        boolean annotationFound = false;
        for (Map.Entry<Class<? extends Annotation>, BiFunction<VariableElement, Annotation, String>> generatorAnnotation
            : ANNOTATION_TO_GENERATOR.entrySet()) {
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
            if (Utils.isPrimitive(field)) {
                addPrimitiveField(field, generationClass);
            } else {
                addNullField(field, generationClass);
            }
        }
        generationClass.addImport(ValueGenerator.class);
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

    @SuppressWarnings("PMD.CyclomaticComplexity")
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
