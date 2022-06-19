# FlashLibs

[Ore](https://ore.spongepowered.org/FlashLabs/FlashLibs) |
[Source](https://github.com/Flash-Labs/flashlibs) |
[Discord](https://discord.gg/zWqnAa9KRn) |
[FlashLabs](https://flashlabs.dev)

FlashLibs is a library for Sponge development providing common functionality for
creating plugins. FlashLibs itself contains multiple libraries, each of which is
independently versioned to allow developers to better track updates based on the
libraries in use. The current libraries are:

- [Command `v0.1.1`](docs/Command.md)
    - A service for creating and registering hierarchical commands, as well as
      managing other common actions for commands.
- [Database `v0.1.0`](docs/Database.md)
    - A service for interacting with databases such  as obtaining connections,
      executing statements, and managing transactions.
- [Inventory `v0.1.0`](docs/Inventory.md)
    - A library for creating custom inventories and managing inventory actions
      to create menus.
- [Message `v0.1.1`](docs/Message.md)
    - A service for managing translatable messages with support for argument
      placeholders.
- [Plugin `v0.1.0`](docs/Plugin.md)
    - A framework providing the core structure/functionality for a plugin that
      also manages necessary resources like commands and messages.
- [Translation `v0.1.1`](docs/Translation.md)
    - A service for loading translatable messages via
      [ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html)s.

## Contributing

Contributions to FlashLibs are highly encouraged. First, we ask that before
starting you discuss the changes either through an issue, on
[Discord](https://discord.gg/zWqnAa9KRn), or through another method. This is to
ensure we know what is currently being worked on to help us administrate the
project effectively. When in doubt, create an issue for discussion first.

The full contribution guidelines are available on
[GitHub](https://github.com/Flash-Labs/FlashLibs/blob/master/CONTRIBUTING.md).
