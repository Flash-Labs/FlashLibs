package dev.flashlabs.flashlibs.inventory;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.function.Consumer;

public final class Element {

    public static final Element EMPTY = of(ItemStack.empty());

    private final ItemStackSnapshot item;
    private final Consumer<Action.Click> onClick;

    private Element(ItemStack item, Consumer<Action.Click> onClick) {
        this.item = item.createSnapshot();
        this.onClick = onClick;
    }

    public static Element of(ItemStack item) {
        return new Element(item, a -> {});
    }

    public static Element of(ItemStack item, Consumer<Action.Click> onClick) {
        return new Element(item, onClick);
    }

    public ItemStackSnapshot getItem() {
        return item;
    }

    void onClick(Action.Click action) {
        onClick.accept(action);
    }

}
