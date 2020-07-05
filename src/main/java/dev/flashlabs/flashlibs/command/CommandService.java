package dev.flashlabs.flashlibs.command;

import com.google.common.collect.MutableClassToInstanceMap;
import com.google.common.collect.Sets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.plugin.PluginContainer;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * Provides an interface for initializing and registering commands for a plugin.
 */
public final class CommandService {

    final PluginContainer container;
    private final MutableClassToInstanceMap<Command> commands = MutableClassToInstanceMap.create();
    private final Set<Class<? extends Command>> registered = Sets.newLinkedHashSet();
    private final Set<CommandMapping> mappings = Sets.newHashSet();

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
                Command command = constructor.newInstance(new Command.Builder(this));
                Sponge.getCommandManager().register(container, command.getSpec(), command.getAliases().stream()
                        .filter(a -> a.startsWith("/"))
                        .map(a -> a.substring(1))
                        .toArray(String[]::new))
                        .ifPresent(mappings::add);
                return command;
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException("Invalid command class.", e);
            }
        });
    }

    /**
     * Registers commands for the given classes to Sponge, as well as any child
     * commands with registrable aliases.
     *
     * @throws IllegalArgumentException if class is already registered
     */
    @SafeVarargs
    public final void register(Class<? extends Command>... classes) {
        for (Class<? extends Command> clazz : classes) {
            if (!registered.add(clazz)) {
                throw new IllegalArgumentException("Class " + clazz + " is already registered.");
            }
            Command command = get(clazz);
            Sponge.getCommandManager().register(container, command.getSpec(), command.getAliases().stream()
                    .filter(a -> !a.startsWith("/"))
                    .toArray(String[]::new))
                    .ifPresent(mappings::add);
        }
    }

    /**
     * Unregisters and removes all commands registered through this service.
     * Subsequent calls to {@link #get(Class)} will re-initialize the command.
     */
    public void unregister() {
        commands.clear();
        registered.clear();
        mappings.forEach(Sponge.getCommandManager()::removeMapping);
    }

    /**
     * Reloads all commands registered through this service, re-initializing and
     * re-registering each command.
     */
    public void reload() {
        Class[] classes = registered.toArray(new Class[0]);
        unregister();
        register(classes);
    }

}
