package uk.gov.hmcts.juror.support.generation.generators.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Time;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LocalTimeGenerator {
    TimeFilter minInclusive();
    TimeFilter maxExclusive();
}
