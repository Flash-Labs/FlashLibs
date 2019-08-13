package dev.flashlabs.flashlibs;

import com.google.inject.Inject;
import org.spongepowered.api.plugin.Plugin;

/**
 * The main plugin class for FlashLibs.
 */
@Plugin(id = "flashlibs")
public final class FlashLibs {

    private static FlashLibs instance;

    @Inject
    private FlashLibs() {
        instance = this;
    }

    public static FlashLibs get() {
        return instance;
    }

}
