package dev.flashlabs.flashlibs.inventory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.AbstractInventoryProperty;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class View {

    private final Inventory inventory;
    private final Map<Integer, Element> elements = Maps.newHashMap();
    private final PluginContainer container;

    private View(Builder builder, PluginContainer container) {
        Inventory.Builder b = Inventory.builder().of(builder.archetype);
        builder.properties.forEach(b::property);
        builder.listeners.forEach((c, a) -> b.listener(c, e -> onEvent(e, a)));
        inventory = b.listener(ClickInventoryEvent.class, this::onClick).build(container);
        this.container = container;
        define(builder.layout);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void define(Layout layout) {
        for (int i = 0; i < inventory.capacity(); i++) {
            set(layout.getElements().getOrDefault(i, Element.EMPTY), i);
        }
    }

    public void update(Layout layout) {
        layout.getElements().forEach((i, e) -> set(e, i));
    }

    public void set(Element element, int index) {
        if (element == Element.EMPTY) {
            elements.remove(index);
        } else {
            elements.put(index, element);
        }
        set(element.getItem().createStack(), index);
    }

    public void set(ItemStack item, int index) {
        inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(index))).set(item);
    }

    private void onEvent(InteractInventoryEvent event, Consumer<Action> action) {
        event.getCause().first(Player.class).ifPresent(p -> action.accept(new Action<>(event, p, this)));
    }

    private void onClick(ClickInventoryEvent event) {
        event.getCause().first(Player.class).ifPresent(p -> {
            List<SlotTransaction> transactions = event.getTransactions().stream()
                    .filter(s -> inventory.containsInventory(s.getSlot()))
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

    public static Builder builder(InventoryArchetype archetype) {
        return new Builder(archetype);
    }

    public static class Builder {

        private final InventoryArchetype archetype;
        private final List<InventoryProperty> properties = Lists.newArrayList();
        private final Map<Class<? extends InteractInventoryEvent>, Consumer<Action>> listeners = Maps.newHashMap();
        private Layout layout = Layout.EMPTY;

        private Builder(InventoryArchetype archetype) {
            this.archetype = archetype;
        }

        public Builder property(InventoryProperty property) {
            properties.add(property);
            return this;
        }

        public Builder onOpen(Consumer<Action<InteractInventoryEvent.Open>> onOpen) {
            listeners.put(InteractInventoryEvent.Open.class, (Consumer<Action>) (Object) onOpen);
            return this;
        }

        public Builder onClose(Consumer<Action<InteractInventoryEvent.Close>> onClose) {
            listeners.put(InteractInventoryEvent.Close.class, (Consumer<Action>) (Object) onClose);
            return this;
        }

        public Builder layout(Layout layout) {
            this.layout = layout;
            return this;
        }

        public View build(PluginContainer container) {
            return new View(this, container);
        }

    }

}
