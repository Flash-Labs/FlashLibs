# Inventory Library

 - Version: `v0.1.0` ([Source](https://github.com/Flash-Labs/FlashLibs/tree/master/src/main/java/dev/flashlabs/flashlibs/inventory))
 - Dependencies: None

The inventory library provides a system for creating custom inventories and
managing inventory actions to create menus.

## QuickStart

 - Note: Complete documentation is being worked on, and it will take us some
   time to write everything out. If you have any questions, feel free to ask
   in the `#flashlibs` channel on [Discord](https://discord.gg/zWqnAa9KRn)

The core of the inventory library is the `View` class, which represents an
inventory that can displayed to players, and the `Element` class, which
represents the item and click action of a slot in the inventory.

A simple `Element` can be created a follows:

```java
Element element = Element.of(ItemTypes.BLAZE_POWDER, action -> {
    action.getPlayer().sendMessage(Text.of("Elements are awesome!"));
});
```

To create a `View`, we can use the `View.builder` method.

```java
View view = View.builder(InventoryArchetypes.CHEST)
        .title(Text.of("Views are awesome!")
        .build(container);
```

From here, to position `Element`s in the view we can use the view's `define`
method with the help of a `Layout`:

```java
Layout layout = Layout.builder(3, 9)
        .border(Element.of(ItemStack.of(ItemTypes.STONE, 1)))
        .fill(Element.of(ItemStack.of(ItemTypes.DIRT, 2)))
        .set(Element.of(ItemStack.of(ItemTypes.GRASS, 3)), 1)
        .build();
```

![View Example](https://cdn.discordapp.com/attachments/438315658355146763/440344173162397697/unknown.png)

## Pages

The `Page` class is used for paginating contents in an inventory which supports
navigation.

The remaining documentation hasn't been written yet, but it should be feasible
to piece together things using the javadocs. If you have any questions feel free
to ask on [Discord](https://discord.gg/zWqnAa9KRn). If you'd like to help write
documentation we would greatly appreciate it!
