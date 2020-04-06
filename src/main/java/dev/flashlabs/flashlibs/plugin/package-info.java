/**
 * The plugin library is a framework providing the core structure and
 * functionality for a plugin and managing necessary resources such as messages.
 */
@NonnullByDefault
@Library(id = "plugin", version = "0.1.0", dependencies = {"command", "message"})
package dev.flashlabs.flashlibs.plugin;

import dev.flashlabs.flashlibs.Library;
import org.spongepowered.api.util.annotation.NonnullByDefault;
