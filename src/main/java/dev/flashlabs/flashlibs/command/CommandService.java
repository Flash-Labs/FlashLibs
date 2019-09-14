package dev.flashlabs.flashlibs.command;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.lang.reflect.Constructor;

/**
 * Provides an interface for initializing and registering commands for a plugin.
 */
public final class CommandService {

    final PluginContainer container;
    private final LoadingCache<Class<? extends Command>, Command> cache = Caffeine.newBuilder().build(c -> {
        Constructor<? extends Command> constructor = c.getDeclaredConstructor(Command.Builder.class);
        constructor.setAccessible(true);
        return constructor.newInstance(new Command.Builder(this));
    });

    private CommandService(PluginContainer container) {
        this.container = container;
    }

    /**
     * Creates a service managing commands for the given plugin. This service
     * presumes it is the controller for all commands registered by the plugin.
     */
    public static CommandService of(PluginContainer container) {
        return new CommandService(container);
    }

    /**
     * Returns the instance corresponding to the given class or creates one as
     * needed. Instances are created through constructor injection.
     *
     * @throws java.util.concurrent.CompletionException If the class does not
     *         have a valid constructor or could not be instantiated
     * @see Command#Command(Command.Builder)
     */
    public Command get(Class<? extends Command> clazz) {
        return cache.get(clazz);
    }

    /**
     * Registers commands for the given classes to Sponge, as well as any child
     * commands with primary aliases.
     */
    public void register(Class<? extends Command>... classes) {
        for (Class<? extends Command> clazz : classes) {
            get(clazz);
        }
    }

    /**
     * Unregisters all commands owned by the backing plugin and invalidates any
     * instances loaded by this service.
     */
    public void unregister() {
        Sponge.getCommandManager().getOwnedBy(container).forEach(Sponge.getCommandManager()::removeMapping);
        cache.invalidateAll();
    }

}
