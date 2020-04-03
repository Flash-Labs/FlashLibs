package dev.flashlabs.flashlibs.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a pagination system of views supporting navigation. Pages are
 * defined using a base layout as a template and functions for computing values
 * dependent on the page number.
 */
public final class Page {

    public static final Element
            FIRST = Element.of(ItemStack.empty()),
            PREVIOUS = Element.of(ItemStack.empty()),
            CURRENT = Element.of(ItemStack.empty()),
            NEXT = Element.of(ItemStack.empty()),
            LAST = Element.of(ItemStack.empty());

    private final InventoryArchetype archetype;
    private final Function<Context, Text> title;
    private final ImmutableMap<Element, Function<Context, Element>> icons;
    private final Layout layout;
    private final List<View> views = Lists.newArrayList();
    private final PluginContainer container;

    private Page(Builder builder, PluginContainer container) {
        archetype = builder.archetype;
        title = builder.title;
        icons = ImmutableMap.copyOf(builder.icons);
        layout = builder.layout;
        this.container = container;
        define(Lists.newArrayList());
    }

    /**
     * Opens the first page for the player.
     */
    public void open(Player player) {
        views.get(0).open(player);
    }

    /**
     * Opens the given page for the player. Pages are numbered starting at 1 and
     * the page number is clamped to be a valid page.
     */
    public void open(Player player, int page) {
        views.get(page > 1 ? Math.min(page, views.size()) - 1 : 0).open(player);
    }

    /**
     * Sets the contents of the pages and creates the associated views. This
     * method currently renders all of the pages immediately, but in the future
     * will likely be refactored to lazily load pages on demand.
     */
    public Page define(List<Element> contents) {
        views.clear();
        int size = layout.getDimension().getRows() * layout.getDimension().getColumns() - layout.getElements().size();
        int pages = Math.max((contents.size() - 1) / size + 1, 1);
        for (int i = 1; i <= pages; i++) {
            Context context = new Context(i, pages);
            Map<Integer, Element> elements = Maps.newHashMap(layout.getElements());
            for (int index = (i - 1) * size, j = 0; j < layout.getElements().size() + size; j++) {
                Element element = elements.get(j);
                if (element == null && index < contents.size()) {
                    elements.put(j, contents.get(index++));
                } else if (icons.containsKey(element)) {
                    elements.put(j, icons.get(element).apply(context));
                }
            }
            views.add(View.builder(archetype)
                    .title(title.apply(context))
                    .build(container)
                    .define(Layout.builder(layout.getDimension().getRows(), layout.getDimension().getColumns())
                            .set(elements)
                            .build()));
        }
        return this;
    }

    /**
     * Represents the context for a specific page, primarily the current page
     * number and total number of pages.
     */
    public final class Context {

        private final int current;
        private final int total;

        private Context(int current, int total) {
            this.current = current;
            this.total = total;
        }

        public int getCurrent() {
            return current;
        }

        public int getTotal() {
            return total;
        }

        /**
         * A callback for opening the page through the context.
         */
        public void open(Player player, int page) {
            Page.this.open(player, page);
        }

    }

    /**
     * Creates a new builder for pages with the given archetype.
     */
    public static Builder builder(InventoryArchetype archetype) {
        return new Builder(archetype);
    }

    /**
     * A builder for creating {@link Page}s.
     */
    public static final class Builder {

        private static final Function<Context, Text> DEFAULT_TITLE = c -> Text.of("Page " + c.getCurrent());
        private static final ImmutableMap<Element, Function<Context, Element>> DEFAULT_ICONS = ImmutableMap.<Element, Function<Context, Element>>builder()
                .put(Page.FIRST, c -> icon(c, "First", 1))
                .put(Page.PREVIOUS, c -> icon(c, "Previous", Math.max(c.getCurrent() - 1, 1)))
                .put(Page.CURRENT, c -> icon(c, "Current", c.getCurrent()))
                .put(Page.NEXT, c -> icon(c, "Next", Math.min(c.getCurrent() + 1, c.getTotal())))
                .put(Page.LAST, c -> icon(c, "Last", c.getTotal()))
                .build();

        private static Element icon(Context context, String name, int target) {
            return Element.of(ItemStack.builder()
                    .itemType(context.getCurrent() == target ? ItemTypes.MAP : ItemTypes.PAPER)
                    .add(Keys.DISPLAY_NAME, Text.of(name, " (", target, ")"))
                    .quantity(context.getTotal() > 64 ? target : 1)
                    .build(), a -> a.callback(v -> context.open(a.getPlayer(), target)));
        }

        private final InventoryArchetype archetype;
        private Function<Context, Text> title = DEFAULT_TITLE;
        private final Map<Element, Function<Context, Element>> icons = Maps.newHashMap(DEFAULT_ICONS);
        private Layout layout = Layout.EMPTY;

        private Builder(InventoryArchetype archetype) {
            this.archetype = archetype;
        }

        /**
         * Sets the function for computing the title. The default title is
         * "Page #".
         */
        public Builder title(Function<Context, Text> function) {
            title = function;
            return this;
        }

        /**
         * Sets the function for computing the element for a given icon. This is
         * used to generate elements which are dependent on the page. Default
         * icons are provided for {@link Page#FIRST} through {@link Page#LAST}.
         */
        public Builder icon(Element icon, Function<Context, Element> function) {
            icons.put(icon, function);
            return this;
        }

        /**
         * Sets the layout used a template for pages.
         */
        public Builder layout(Layout layout) {
            this.layout = layout;
            return this;
        }

        /**
         * Creates a Page from this builder.
         */
        public Page build(PluginContainer container) {
            return new Page(this, container);
        }

    }

}
