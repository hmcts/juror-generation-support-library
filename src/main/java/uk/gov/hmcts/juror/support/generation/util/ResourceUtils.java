package uk.gov.hmcts.juror.support.generation.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public final class ResourceUtils {

    private ResourceUtils() {

    }

    public static List<String> readLinesFromResourceFile(String file) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
            if (inputStream == null) {
                throw new IOException("File not found: '" + file + "'");
            }
            try (InputStreamReader isr = new InputStreamReader(inputStream)) {
                BufferedReader reader = new BufferedReader(isr);
                return reader.lines().toList();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
