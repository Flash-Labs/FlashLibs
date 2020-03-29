package dev.flashlabs.flashlibs.inventory;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.function.Consumer;

/**
 * Represents a slot in an inventory, consisting of a display item and a click
 * action which is executed when the slot is clicked.
 */
public final class Element {

    public static final Element EMPTY = of(ItemStack.empty());

    private final ItemStackSnapshot item;
    private final Consumer<Action.Click> onClick;

    private Element(ItemStack item, Consumer<Action.Click> onClick) {
        this.item = item.createSnapshot();
        this.onClick = onClick;
    }

    /**
     * Creates an Element with the given item and no action.
     */
    public static Element of(ItemStack item) {
        return new Element(item, a -> {});
    }

    /**
     * Creates an Element with the given item and click action.
     */
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
