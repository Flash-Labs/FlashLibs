# Database Library

 - Version: `v0.1.0` ([Source](https://github.com/Flash-Labs/FlashLibs/tree/master/src/main/java/dev/flashlabs/flashlibs/database))
 - Dependencies: None

The database library provides a service for interacting with databases such as
obtaining connections, executing statements, and managing transactions.

## QuickStart

 - Note: Complete documentation is being worked on, and it will take us some
   time to write everything out. If you have any questions, feel free to ask
   in the `#flashlibs` channel on [Discord](https://discord.gg/zWqnAa9KRn)

To start, create a `DatabaseService` with a provided `DataSource` or JDBC string
to be used by Sponge's SqlService.

```java
DatabaseService database = DatabaseService.of("jdbc:sqlite:config/plugin-id/database.db");
```

To quickly execute a single statement, use either the `update` or `query`
methods. These methods open a connection, execute a statement, and then close
the connection (preserving the results of a query).

```java
database.query("SELECT * FROM PluginData WHERE uuid = ?", uuid);
```

If you are executing multiple statements, you should reuse the connection. The
returned `Connection` object wraps around `java.sql.Connection` and provides
the same `update` and `query` methods, as well as a `getStatement` method for
reusing the `PreparedStatement`.

```java
try (Connection connection = database.getConnection()) {
    int friends = database.query("SELECT friends FROM PluginData WHERE uuid = ?", uuid).getInt(1);
    database.update("UPDATE PluginData SET friends = ? WHERE uuid = ?", freinds + 1, uuid);
}
```

The remaining documentation hasn't been written yet, but it should be feasible
to piece together things using the javadocs. If you have any questions feel free
to ask on [Discord](https://discord.gg/zWqnAa9KRn). If you'd like to help write
documentation we would greatly appreciate it!
