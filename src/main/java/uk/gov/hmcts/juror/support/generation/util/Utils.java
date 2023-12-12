package uk.gov.hmcts.juror.support.generation.util;

import uk.gov.hmcts.juror.support.generation.generators.code.MaanualGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.DateTimeFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.TimeFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.ValueGenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")//TODO use a proper exception
public final class Utils {

    private Utils() {

    }

    public static String getLocalDateStringFromDateFilter(DateFilter dateFilter) {
        String localDateGeneratorString = "java.time.LocalDate.now()";
        return switch (dateFilter.mode()) {
            case PLUS -> localDateGeneratorString + ".plus(" + dateFilter.value() + "L,"
                + " java.time.temporal.ChronoUnit." + dateFilter.unit().name() + ")";
            case MINUS -> localDateGeneratorString + ".minus(" + dateFilter.value() + "L, "
                + "java.time.temporal.ChronoUnit." + dateFilter.unit().name() + ")";
        };
    }

    public static String getLocalDateTimeStringFromTimeFilter(TimeFilter dateTimeFilter) {
        String localDateGeneratorString = "java.time.LocalTime.now()";
        return switch (dateTimeFilter.mode()) {
            case PLUS -> localDateGeneratorString + ".plus(" + dateTimeFilter.value() + "L,"
                + " java.time.temporal.ChronoUnit." + dateTimeFilter.unit().name() + ")";
            case MINUS -> localDateGeneratorString + ".minus(" + dateTimeFilter.value() + "L, "
                + "java.time.temporal.ChronoUnit." + dateTimeFilter.unit().name() + ")";
        };
    }

    public static String getLocalDateTimeStringFromDateTimeFilter(DateTimeFilter dateTimeFilter) {
        return "java.time.LocalDateTime.of("
            + getLocalDateStringFromDateFilter(dateTimeFilter.dateFilter())
            + ", "
            + getLocalDateTimeStringFromTimeFilter(dateTimeFilter.timeFilter())
            + ')';
    }

    public static String escape(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }

    public static String getFieldType(VariableElement field) {
        String fieldType = field.asType().toString();
        return switch (fieldType) {
            case "int" -> Integer.class.getName();
            case "boolean" -> Boolean.class.getName();
            case "long" -> Long.class.getName();
            case "double" -> Double.class.getName();
            case "float" -> Float.class.getName();
            case "short" -> Short.class.getName();
            case "byte" -> Byte.class.getName();
            case "char" -> Character.class.getName();
            default -> fieldType;
        };
    }

    public static String capitalise(String text) {
        return text.substring(0, 1).toUpperCase(Locale.getDefault()) + text.substring(1);
    }

    public static boolean isPrimitive(VariableElement field) {
        String fieldType = field.asType().toString();
        return switch (fieldType) {
            case "int", "boolean", "long", "double", "float", "short", "byte", "char" -> true;
            default -> false;
        };
    }

    public static boolean hasSuperClass(Element element) {
        return element instanceof TypeElement typeElement
            && typeElement.getSuperclass() != null
            && typeElement.getSuperclass() instanceof DeclaredType superClassDeclaredType
            && superClassDeclaredType.asElement() instanceof TypeElement superClassTypeElement
            && !Object.class.getName()
            .equals(getClassNameFromTypeElement(superClassTypeElement));
    }

    public static String getClassNameFromTypeElement(TypeElement typeElement) {
        return typeElement.getEnclosingElement().toString() + "."
            + typeElement.getSimpleName().toString();
    }

    public static TypeElement getSuperClassTypeElement(Element element) {
        if (hasSuperClass(element)) {
            return (TypeElement) ((DeclaredType) ((TypeElement) element).getSuperclass()).asElement();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<? extends ValueGenerator>> getGenerators() {
        return findAllClassesUsingClassLoader("uk.gov.hmcts.juror.support.generation.generators.value")
            .stream()
            .filter(ValueGenerator.class::isAssignableFrom)
            .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
            .filter(clazz -> !clazz.isAnnotationPresent(MaanualGenerator.class))
            .map(clazz -> (Class<? extends ValueGenerator>) clazz)
            .collect(Collectors.toSet());
    }

    @SuppressWarnings("PMD.UseProperClassLoader")//False positive as annotation processor
    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        try (InputStream stream = Utils.class.getClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Failed to find classes in package: " + packageName, e);
        }
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to get class: " + packageName + "." + className, e);
        }
    }
}
