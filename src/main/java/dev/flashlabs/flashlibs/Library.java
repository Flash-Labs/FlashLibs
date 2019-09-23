package dev.flashlabs.flashlibs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Identifies packages that represent libraries in FlashLibs. The annotation
 * defines the library id and version, as well as other useful information. This
 * is documentation based and not enforced at runtime.
 */
@Documented
@Target({ElementType.PACKAGE})
public @interface Library {

    /**
     * The library id, lowercase.
     */
    String id();

    /**
     * The library version, which meets standard project versioning (a more
     * restrictive version of SemVer's major.minor.patch). This is independent
     * of the project version and only applies to the library + dependencies.
     */
    String version();

    /**
     * Required dependencies on other libraries, listed by id. It is preferable
     * to avoid mutually-dependent libraries, but not guaranteed.
     */
    String[] dependencies() default {};

    /**
     * Whether this library is designed to support asynchronous use outside of
     * the main server thread.
     */
    boolean async() default false;

}
