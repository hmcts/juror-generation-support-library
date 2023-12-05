package uk.gov.hmcts.juror.support.generation.util;

import java.util.Collections;
import java.util.List;

public final class NameGenerator {

    public static final List<String> FIRST_NAMES;
    public static final List<String> LAST_NAMES;

    private NameGenerator() {
    }

    static {
        FIRST_NAMES = Collections.unmodifiableList(ResourceUtils.readLinesFromResourceFile("data/first_names.txt"));
        LAST_NAMES = Collections.unmodifiableList(ResourceUtils.readLinesFromResourceFile("data/last_names.txt"));
    }

    public static String generateFirstAndName() {
        return generateFirstName() + " " + generateLastName();
    }

    public static String generateFirstName() {
        return FIRST_NAMES.get(
            RandomGenerator.nextInt(0, FIRST_NAMES.size()));
    }

    public static String generateLastName() {
        return LAST_NAMES.get(
            RandomGenerator.nextInt(0, FIRST_NAMES.size()));
    }
}
