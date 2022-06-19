# Plugin Library

 - Version: `v0.1.0` ([Source](https://github.com/Flash-Labs/FlashLibs/tree/master/src/main/java/dev/flashlabs/flashlibs/plugin))
 - Dependencies:
    - [Command Library](Command.md)
    - [Message Library](Message.md)

The plugin library is a framework providing the core structure and functionality
for a plugin and managing necessary resources such as messages.

## QuickStart

 - Note: Complete documentation is being worked on, and it will take us some
   time to write everything out. If you have any questions, feel free to ask
   in the `#flashlibs` channel on [Discord](https://discord.gg/zWqnAa9KRn)

Extend the `@Plugin` class with `PluginInstance` and provide the container
through injection, as below. Generally, the plugin instance is made available
statically through `MyPlugin.get()`, which allows anywhere in the plugin to
easily access the plugin's container, logger, and messages.

```java
@Plugin(id = "plugin-id")
public class MyPlugin extends PluginInstance {

    private static MyPlugin instance;

    @Inject
    private MyPlugin(PluginContainer container) {
        super(container);
        instance = this;
    }

    public static MyPlugin get() {
        return instance;
    }

}
```

> Remember to reload the provided `CommandService` and `MessageService`! The
> library will likely do this automatically in the future, but for now it needs
> to be done manually on reload.

The remaining documentation hasn't been written yet, but it should be feasible
to piece together things using the javadocs. If you have any questions feel free
to ask on [Discord](https://discord.gg/zWqnAa9KRn). If you'd like to help write
documentation we would greatly appreciate it!
