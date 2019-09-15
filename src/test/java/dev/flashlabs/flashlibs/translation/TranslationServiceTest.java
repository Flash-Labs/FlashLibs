package dev.flashlabs.flashlibs.translation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.spongepowered.api.text.translation.locale.Locales;

import java.util.Locale;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class TranslationServiceTest {

    private static final TranslationService SERVICE = TranslationService.of("translation.messages", ClassLoader.getSystemClassLoader());

    private static Stream<Arguments> test() {
        return Stream.of(
                Arguments.arguments("greeting.hello", Locales.DEFAULT, "Hello there!"),
                Arguments.arguments("greeting.hello", Locales.EN_US, "Hello there!"),
                Arguments.arguments("greeting.hello", Locales.EN_PT, "Ahoy matey!"),
                Arguments.arguments("absent-key", Locales.DEFAULT, "absent-key")
        );
    }

    @ParameterizedTest
    @MethodSource
    void test(String key, Locale locale, String expected) {
        Assertions.assertEquals(expected, SERVICE.getString(key, locale));
    }

}
