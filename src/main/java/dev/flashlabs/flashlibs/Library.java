package dev.flashlabs.flashlibs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Identifies packages that represent libraries in FlashLibs. The annotation
 * defines the library id, version, and dependencies on other libraries.
 */
@Documented
@Target({ElementType.PACKAGE})
public @interface Library {

    String id();
    String version();
    String[] dependencies() default {};

}
