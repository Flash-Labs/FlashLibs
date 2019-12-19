package dev.flashlabs.flashlibs.inventory;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.function.Consumer;

public final class Element {

    public static final Element EMPTY = builder().build();

    private final ItemStackSnapshot item;
    private final Consumer<Action.Click> onClick;

    private Element(Builder builder) {
        item = builder.item;
        onClick = builder.onClick;
    }

    public static Element of(ItemStack item) {
        return builder().item(item).build();
    }

    public static Element of(ItemStack item, Consumer<Action.Click> onClick) {
        return builder().item(item).onClick(onClick).build();
    }

    public ItemStackSnapshot getItem() {
        return item;
    }

    void onClick(Action.Click action) {
        onClick.accept(action);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private ItemStackSnapshot item = ItemStackSnapshot.NONE;
        private Consumer<Action.Click> onClick = a -> {};

        public Builder item(ItemStack item) {
            return item(item.createSnapshot());
        }

        public Builder item(ItemStackSnapshot item) {
            this.item = item;
            return this;
        }

        public Builder onClick(Consumer<Action.Click> onClick) {
            this.onClick = onClick;
            return this;
        }

        public Element build() {
            return new Element(this);
        }

    }

}
