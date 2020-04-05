package dev.flashlabs.flashlibs.plugin;

import dev.flashlabs.flashlibs.command.CommandService;
import dev.flashlabs.flashlibs.message.MessageService;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

/**
 * A superclass for plugin instances that manages common resources such as
 * messages. This class assumes information about the location of assets,
 * configuration files, and other structural decisions.
 *
 * <p>The provided {@link MessageService} assumes the base messages config is
 * the asset {@code messages/messages.conf} and files are stored in the
 * {@code messages} folder within the plugin directory.</p>
 */
public abstract class PluginInstance {

    protected final PluginContainer container;
    protected final Logger logger;
    protected final Path directory;
    protected final CommandService commands;
    protected final MessageService messages;

    /**
     * Creates a new PluginInstance using the given container, which should be
     * obtained through constructor injection in the subclass.
     */
    protected PluginInstance(PluginContainer container) {
        this.container = container;
        logger = container.getLogger();
        directory = Sponge.getConfigManager().getPluginConfig(container).getDirectory();
        commands = CommandService.of(container);
        try {
            Path path = Files.createDirectories(directory.resolve("messages"));
            container.getAsset("messages/messages.conf").get().copyToDirectory(path);
            messages = MessageService.of("messages", path);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public final PluginContainer getContainer() {
        return container;
    }

    public final Logger getLogger() {
        return logger;
    }

    /**
     * @see MessageService#get(String, Locale, Object...)
     */
    public final Text getMessage(String key, Locale locale, Object... args) {
        return messages.get(key, locale, args);
    }

    /**
     * @see MessageService#send(CommandSource, String, Object...) 
     */
    public final void sendMessage(CommandSource source, String key, Object... args) {
        messages.send(source, key, args);
    }

}
