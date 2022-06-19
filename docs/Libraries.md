# Libraries

FlashLibs is composed of multiple, distinct libraries which provide common
functionality for creating plugins. Each library is independently versioned to
allow developers to better track updates based on the libraries in use. The
current libraries are:

 - [Command `v0.1.1`](Command.md)
    - A service for creating and registering hierarchical commands, as well as
      managing other common actions for commands.
 - [Database `v0.1.0`](Database.md)
    - A service for interacting with databases such  as obtaining connections,
      executing statements, and managing transactions.
 - [Inventory `v0.1.0`](Inventory.md)
    - A library for creating custom inventories and managing inventory actions
      to create menus.
 - [Message `v0.1.1`](Message.md)
    - A service for managing translatable messages with support for argument
      placeholders.
 - [Plugin `v0.1.0`](Plugin.md)
    - A framework providing the core structure/functionality for a plugin that
      also manages necessary resources like commands and messages.
 - [Translation `v0.1.1`](Translation.md)
    - A service for loading translatable messages via
      [ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html)s.
