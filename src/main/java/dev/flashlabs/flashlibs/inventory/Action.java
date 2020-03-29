package dev.flashlabs.flashlibs.inventory;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;

import java.util.function.Consumer;

/**
 * Represents an active inventory event connected to a {@link View}, such as
 * opening/closing the inventory or clicking a slot.
 */
public class Action<T extends InteractInventoryEvent> {

    private final T event;
    private final Player player;
    private final View view;

    Action(T event, Player player, View view) {
        this.event = event;
        this.player = player;
        this.view = view;
    }

    /**
     * Returns the inventory event, which is initially cancelled for
     * {@link Click}s.
     */
    public final T getEvent() {
        return event;
    }

    /**
     * Returns the player causing this event.
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Adds a callback which is executed after this event has completed. The
     * callback is given access to the view linked to this event.
     *
     * Any actions which can cause an inventory event, such as modifying a slot
     * or opening/closing an inventory, must be done through a callback.
     */
    public final void callback(Consumer<View> callback) {
        view.execute(callback);
    }

    /**
     * An action for clicks that provides the slot transaction and index. The
     * incoming event is always cancelled, but can be uncanceled to allow the
     * event to proceed.
     */
    public static final class Click extends Action<ClickInventoryEvent> {

        private final SlotTransaction slot;
        private final int index;

        Click(ClickInventoryEvent event, Player player, View view, SlotTransaction slot, int index) {
            super(event, player, view);
            this.slot = slot;
            this.index = index;
        }

        /**
         * Returns the slot transaction corresponding to this click. The result
         * can only be modified if the event is not cancelled.
         */
        public SlotTransaction getSlot() {
            return slot;
        }

        /**
         * Returns the index of the slot corresponding to the linked view.
         */
        public int getIndex() {
            return index;
        }

    }

}
