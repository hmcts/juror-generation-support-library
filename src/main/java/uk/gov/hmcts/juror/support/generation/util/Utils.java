package uk.gov.hmcts.juror.support.generation.util;

import org.apache.commons.text.StringEscapeUtils;
import uk.gov.hmcts.juror.support.generation.generators.value.DateFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.DateTimeFilter;
import uk.gov.hmcts.juror.support.generation.generators.value.TimeFilter;

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
        String localDateGeneratorString = "java.time.LocalDateTime.of(";
        localDateGeneratorString += getLocalDateStringFromDateFilter(dateTimeFilter.dateFilter()) + ", ";
        localDateGeneratorString += getLocalDateTimeStringFromTimeFilter(dateTimeFilter.timeFilter()) + ")";
        return localDateGeneratorString;
    }

    public static String escape(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }
}
