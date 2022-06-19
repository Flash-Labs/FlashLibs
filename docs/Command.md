# Command Library

 - Version: `v0.1.1` ([Source](https://github.com/Flash-Labs/FlashLibs/tree/master/src/main/java/dev/flashlabs/flashlibs/command))
 - Dependencies: None

The command library provides a service for creating and registering hierarchical
commands, as well as managing other common actions for commands.

## QuickStart

 - Note: Complete documentation is being worked on, and it will take us some
   time to write everything out. If you have any questions, feel free to ask
   in the `#flashlibs` channel on [Discord](https://discord.gg/zWqnAa9KRn)

The command library has two classes: `Command`, which represents an executable
command, and `CommandService`, which manages initialization and registration.

To create a command, we make a subclass of `Command` and then set information
such as aliases, permission, and children through the provided `Builder`

```java
public class CustomCommand extends Command {

    @Inject
    private CustomCommand(Builder builder) {
        super(builder
                .aliases("alias-one", "alias-two", "/primary-alias")
                .permission("plugin-id.command.custom.base")
                .children(SubcommandOne.class, SubcommandTwo.class)
        );
    }

}
```

Finally, we can register our custom command using the `CommandService`.

```java
PluginContainer container;
CommandService commands = CommandService.of(container);
commands.register(CustomCommand.class);
```

> The [Plugin](Plugin.md) library can automatically provide a `CommandService`
> for your plugin.

The remaining documentation hasn't been written yet, but it should be feasible
to piece together things using the javadocs. If you have any questions feel free
to ask on [Discord](https://discord.gg/zWqnAa9KRn). If you'd like to help write
documentation we would greatly appreciate it!
