package uk.gov.hmcts.juror.support.generation.generators.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TimeFilter {
    Mode mode();

    long value();

    ChronoUnit unit();

    enum Mode {
        PLUS,
        MINUS
    }
}
