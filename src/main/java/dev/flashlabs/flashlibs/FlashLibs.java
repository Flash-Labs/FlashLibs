package dev.flashlabs.flashlibs;

import com.google.inject.Inject;
import dev.flashlabs.flashlibs.plugin.PluginInstance;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * The main plugin class for FlashLibs.
 */
@Plugin(id = "flashlibs")
public final class FlashLibs extends PluginInstance {

    private static FlashLibs instance;

    @Inject
    private FlashLibs(PluginContainer container) {
        super(container);
        instance = this;
    }

    public static FlashLibs get() {
        return instance;
    }

}
