# Translation Library

 - Version: `v0.1.1` ([Source](https://github.com/Flash-Labs/FlashLibs/tree/master/src/main/java/dev/flashlabs/flashlibs/translation))
 - Dependencies: None

The translation library provides a service for loading translatable messages via
[ResourceBundle](https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html)s.

## QuickStart

 - Note: Complete documentation is being worked on, and it will take us some
   time to write everything out. If you have any questions, feel free to ask
   in the `#flashlibs` channel on [Discord](https://discord.gg/zWqnAa9KRn)

To create a `TranslationService`, provide a name used for the resource bundle
and a path to the directory containing messages.

```java
Path directory;
TranslationService translations = TranslationService.of("messages", path);
```

> The [Plugin](Plugin.md) library can automatically provide a `MessageService`
> for your plugin, which includes a `TranslationService`.

The remaining documentation hasn't been written yet, but it should be feasible
to piece together things using the javadocs. If you have any questions feel free
to ask on [Discord](https://discord.gg/zWqnAa9KRn). If you'd like to help write
documentation we would greatly appreciate it!
