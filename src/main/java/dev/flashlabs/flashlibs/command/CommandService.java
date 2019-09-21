package dev.flashlabs.flashlibs.command;

import com.google.common.collect.MutableClassToInstanceMap;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.lang.reflect.Constructor;

/**
 * Provides an interface for initializing and registering commands for a plugin.
 */
public final class CommandService {

    final PluginContainer container;
    private final MutableClassToInstanceMap<Command> commands = MutableClassToInstanceMap.create();

    private CommandService(PluginContainer container) {
        this.container = container;
    }

    /**
     * Creates a service managing commands for the given plugin.
     */
    public static CommandService of(PluginContainer container) {
        return new CommandService(container);
    }

    /**
     * Returns the instance corresponding to the given class or creates one as
     * needed. Instances are created through constructor injection.
     *
     * @see Command#Command(Command.Builder)
     */
    public Command get(Class<? extends Command> clazz) {
        return commands.computeIfAbsent(clazz, c -> {
            try {
                Constructor<? extends Command> constructor = c.getDeclaredConstructor(Command.Builder.class);
                constructor.setAccessible(true);
                return constructor.newInstance(new Command.Builder(this));
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("Invalid command class.", e);
            }
        });
    }

    /**
     * Registers commands for the given classes to Sponge, as well as any child
     * commands with registrable aliases.
     */
    public void register(Class<? extends Command>... classes) {
        for (Class<? extends Command> clazz : classes) {
            get(clazz);
        }
    }

    /**
     * Unregisters and removes all commands registered through this service.
     * Subsequent calls to {@link #get(Class)} will re-initialize the command.
     */
    public void unregister() {
        commands.values().forEach(c -> c.mapping.ifPresent(Sponge.getCommandManager()::removeMapping));
        commands.clear();
    }

}
