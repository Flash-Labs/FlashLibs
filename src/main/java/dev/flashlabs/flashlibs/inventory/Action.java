package dev.flashlabs.flashlibs.inventory;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;

import java.util.function.Consumer;

public class Action<T extends InteractInventoryEvent> {

    private final T event;
    private final Player player;
    private final View view;

    Action(T event, Player player, View view) {
        this.event = event;
        this.player = player;
        this.view = view;
    }

    public final T getEvent() {
        return event;
    }

    public final Player getPlayer() {
        return player;
    }

    public void callback(Consumer<View> callback) {
        view.execute(callback);
    }

    public static final class Click extends Action<ClickInventoryEvent> {

        private final SlotTransaction slot;
        private final int index;

        Click(ClickInventoryEvent event, Player player, View view, SlotTransaction slot, int index) {
            super(event, player, view);
            this.index = index;
            this.slot = slot;
        }

        public SlotTransaction getSlot() {
            return slot;
        }

        public int getIndex() {
            return index;
        }

    }

}
