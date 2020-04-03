package dev.flashlabs.flashlibs.inventory;

import com.google.common.collect.Maps;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.AbstractInventoryProperty;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Represents an inventory which can be displayed to multiple players. Events
 * that are outside of this inventory (such as the hotbar) will not be canceled.
 */
public final class View {

    private final Inventory inventory;
    private final Map<Integer, Element> elements = Maps.newHashMap();
    private final PluginContainer container;

    private View(Builder builder, PluginContainer container) {
        Inventory.Builder b = Inventory.builder()
                .of(builder.archetype)
                .property(InventoryTitle.of(builder.title))
                .listener(ClickInventoryEvent.class, this::onClick);
        builder.listeners.forEach((c, a) -> b.listener(c, e -> onEvent(e, a)));
        inventory = b.build(container);
        this.container = container;
    }

    /**
     * Opens this view for the player.
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Sets all elements in the given layout to the corresponding slot in the
     * inventory. Undefined indices are considered to be {@link Element#EMPTY}.
     */
    public View define(Layout layout) {
        for (int i = 0; i < inventory.capacity(); i++) {
            set(layout.getElements().getOrDefault(i, Element.EMPTY), i);
        }
        return this;
    }

    /**
     * Sets all elements in the given layout to the corresponding slot in the
     * inventory. Undefined indices will be left unchanged.
     */
    public View update(Layout layout) {
        layout.getElements().forEach((i, e) -> set(e, i));
        return this;
    }

    /**
     * Sets the element for a slot, modifying the display item and click action.
     */
    public void set(Element element, int index) {
        if (element == Element.EMPTY) {
            elements.remove(index);
        } else {
            elements.put(index, element);
        }
        set(element.getItem().createStack(), index);
    }

    /**
     * Sets the display item of a slot without modifying the click action.
     */
    public void set(ItemStack item, int index) {
        inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(index))).set(item);
    }

    private void onEvent(InteractInventoryEvent event, Consumer<Action> action) {
        event.getCause().first(Player.class).ifPresent(p -> action.accept(new Action<>(event, p, this)));
    }

    private void onClick(ClickInventoryEvent event) {
        event.getCause().first(Player.class).ifPresent(p -> {
            List<SlotTransaction> transactions = event.getTransactions().stream()
                    .filter(t -> inventory.containsInventory(t.getSlot().transform()))
                    .collect(Collectors.toList());
            if (!transactions.isEmpty()) {
                event.setCancelled(true);
                transactions.forEach(t -> t.getSlot().getInventoryProperty(SlotIndex.class)
                        .map(AbstractInventoryProperty::getValue)
                        .filter(elements::containsKey)
                        .ifPresent(i -> elements.get(i).onClick(new Action.Click(event, p, this, t, i))));
            }
        });
    }

    void execute(Consumer<View> callback) {
        Task.builder().execute(() -> callback.accept(this)).submit(container);
    }

    /**
     * Creates a new builder for views with the given archetype.
     */
    public static Builder builder(InventoryArchetype archetype) {
        return new Builder(archetype);
    }

    /**
     * A builder for creating {@link View}s.
     */
    public static class Builder {

        private final InventoryArchetype archetype;
        private Text title = Text.EMPTY;
        private final Map<Class<? extends InteractInventoryEvent>, Consumer<Action>> listeners = Maps.newHashMap();

        private Builder(InventoryArchetype archetype) {
            this.archetype = archetype;
        }

        /**
         * Sets the title of the inventory. The default is {@link Text#EMPTY}.
         */
        public Builder title(Text title) {
            this.title = title;
            return this;
        }

        /**
         * Adds an action for when the view is opened.
         */
        public Builder onOpen(Consumer<Action<InteractInventoryEvent.Open>> onOpen) {
            listeners.put(InteractInventoryEvent.Open.class, (Consumer<Action>) (Object) onOpen);
            return this;
        }

        /**
         * Adds an action for when the view is closed.
         */
        public Builder onClose(Consumer<Action<InteractInventoryEvent.Close>> onClose) {
            listeners.put(InteractInventoryEvent.Close.class, (Consumer<Action>) (Object) onClose);
            return this;
        }

        /**
         * Creates a View from this builder.
         */
        public View build(PluginContainer container) {
            return new View(this, container);
        }

    }

}
