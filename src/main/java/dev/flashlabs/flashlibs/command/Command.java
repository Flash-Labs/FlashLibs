package dev.flashlabs.flashlibs.command;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * Represents a command that is registered with Sponge. Commands are created
 * through the {@link CommandService} by injecting into a constructor taking a
 * {@link Command.Builder} parameter. Commands are registered automatically upon
 * initialization.
 *
 * @see CommandSpec
 */
public abstract class Command implements CommandExecutor {

    private final CommandSpec spec;
    private final ImmutableList<String> aliases;

    /**
     * Initializes this command with the values set in the builder and registers
     * it to Sponge.
     */
    protected Command(Builder builder) {
        spec = builder.spec.build();
        aliases = builder.aliases.build();
        Sponge.getCommandManager().register(builder.service.container, spec, aliases.stream()
                .filter(a -> a.startsWith("/"))
                .map(a -> a.substring(1))
                .toArray(String[]::new));
    }

    /**
     * A builder for commands that creates the internal {@link CommandSpec} and
     * aliases used for registration with Sponge.
     */
    protected static class Builder {

        private final CommandService service;
        private final CommandSpec.Builder spec = CommandSpec.builder();
        private final ImmutableList.Builder<String> aliases = ImmutableList.builder();

        Builder(CommandService service) {
            this.service = service;
        }

        /**
         * Adds the given aliases to this command. Aliases that start with
         * {@code '/'} will be registered with Sponge; all others are used as
         * aliases for child commands.
         */
        public Builder aliases(String... aliases) {
            this.aliases.add(aliases);
            return this;
        }

        /**
         * Sets the description for this command.
         */
        public Builder description(Text description) {
            spec.description(description);
            return this;
        }

        /**
         * Sets the permission for this command.
         */
        public Builder permission(String permission) {
            spec.permission(permission);
            return this;
        }

        /**
         * Adds the given commands as children to this command. Each child is
         * provided by the {@link CommandService}.
         */
        public Builder children(Class<? extends Command>... children) {
            for (Class<? extends Command> child : children) {
                Command command = service.get(child);
                spec.child(command.spec, command.aliases.stream()
                        .filter(s -> !s.startsWith("/"))
                        .toArray(String[]::new));
            }
            return this;
        }

        /**
         * Sets the elements used for parsing arguments.
         */
        public Builder elements(CommandElement... elements) {
            spec.arguments(elements);
            return this;
        }

    }

}
