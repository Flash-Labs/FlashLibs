# Message Library

 - Version: `v0.1.1` ([Source](https://github.com/Flash-Labs/FlashLibs/tree/master/src/main/java/dev/flashlabs/flashlibs/message))
 - Dependencies:
    - [Translation Library](Translation.md)

The message library provides a service for managing translatable messages that
support argument placeholders.

## QuickStart

- Note: Complete documentation is being worked on, and it will take us some
  time to write everything out. If you have any questions, feel free to ask
  in the `#flashlibs` channel on [Discord](https://discord.gg/zWqnAa9KRn)

This library has two classes: `MessageService` for retrieving messages, and
`MessageTemplate` for handling arguments. The format for arguments is
`${<format>@<key>}`, such as `${&a@player}`. Missing arguments will render as
`@key` in the appropriate style.

A `MessageService` requires a `TranslationService` from the
[Translation Library](Translation.md).

```java
TranslationService translations;
MessageService messages = MessageService.of(translations);
```

> The [Plugin](Plugin.md) library can automatically provide a `MessageService`
> for your plugin.


The remaining documentation hasn't been written yet, but it should be feasible
to piece together things using the javadocs. If you have any questions feel free
to ask on [Discord](https://discord.gg/zWqnAa9KRn). If you'd like to help write
documentation we would greatly appreciate it!
