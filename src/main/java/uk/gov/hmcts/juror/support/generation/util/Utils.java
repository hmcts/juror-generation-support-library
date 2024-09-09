package uk.gov.hmcts.juror.support.generation.util;

import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.DateTimeFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.EmailGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FormattedLocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FormattedLocalDateTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.IntRangeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.SequenceGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.StringSequenceGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.TimeFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.ValueGenerator;

import java.util.Locale;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

@SuppressWarnings({"PMD.AvoidThrowingRawExceptionTypes", "PMD.CouplingBetweenObjects"})//TODO use a proper exception
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

    public static Set<Class<? extends ValueGenerator>> getGenerators() {
        return Set.of(
            EmailGeneratorImpl.class,
            FirstNameGeneratorImpl.class,
            FixedValueGeneratorImpl.class,
            FormattedLocalDateGeneratorImpl.class,
            FormattedLocalDateTimeGeneratorImpl.class,
            IntRangeGeneratorImpl.class,
            LastNameGeneratorImpl.class,
            LocalDateGeneratorImpl.class,
            LocalDateTimeGeneratorImpl.class,
            LocalTimeGeneratorImpl.class,
            NullValueGeneratorImpl.class,
            RandomFromFileGeneratorImpl.class,
            RandomFromCollectionGeneratorImpl.class,
            RegexGeneratorImpl.class,
            SequenceGeneratorImpl.class,
            StringSequenceGeneratorImpl.class
        );//TODO make dynamic
    }

    public static String[] toStringArray(Object[] objectArray) {
        return java.util.Arrays.stream(objectArray)
            .map(Object::toString)
            .toArray(String[]::new);
    }

    public static String[] toStringArray(int[] objectArray) {
        return java.util.Arrays.stream(objectArray)
            .mapToObj(Integer::toString)
            .toArray(String[]::new);
    }

    public static String[] toStringArray(long[] objectArray) {
        return java.util.Arrays.stream(objectArray)
            .mapToObj(Long::toString)
            .toArray(String[]::new);
    }
}
