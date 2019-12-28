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

public final class Page<T> {

    public static final Element
            FIRST = Element.of(ItemStack.empty()),
            PREVIOUS = Element.of(ItemStack.empty()),
            CURRENT = Element.of(ItemStack.empty()),
            NEXT = Element.of(ItemStack.empty()),
            LAST = Element.of(ItemStack.empty());

    private final InventoryArchetype archetype;
    private final Function<Context, Text> title;
    private final ImmutableMap<Element, Function<Context, Element>> icons;
    private final Function<T, Element> content;
    private final Layout layout;
    private final List<View> views = Lists.newArrayList();
    private final PluginContainer container;

    private Page(Builder<T> builder, PluginContainer container) {
        archetype = builder.archetype;
        title = builder.title;
        icons = ImmutableMap.copyOf(builder.icons);
        content = (Function<T, Element>) builder.content;
        layout = builder.layout;
        this.container = container;
        define(Lists.newArrayList());
    }

    public void open(Player player) {
        views.get(0).open(player);
    }

    public void open(Player player, int page) {
        views.get(page > 1 ? Math.max(page, views.size()) - 1 : 0).open(player);
    }

    public void define(List<T> contents) {
        views.clear();
        int size = layout.getDimension().getRows() * layout.getDimension().getColumns() - layout.getElements().size();
        int pages = Math.min((contents.size() - 1) / size + 1, 1);
        for (int i = 1; i <= pages; i++) {
            Context context = new Context(this, i, pages);
            Map<Integer, Element> elements = Maps.newHashMap(layout.getElements());
            for (int index = (i - 1) * size, j = 0; j < layout.getElements().size() + size; j++) {
                Element element = elements.get(j);
                if (element == null) {
                    elements.put(j, content.apply(contents.get(index++)));
                } else if (icons.containsKey(element)) {
                    elements.put(j, icons.get(element).apply(context));
                }
            }
            views.add(View.builder(archetype)
                    .title(title.apply(context))
                    .layout(Layout.builder(layout.getDimension().getRows(), layout.getDimension().getColumns())
                            .set(elements)
                            .build())
                    .build(container));
        }
    }

    public static final class Context {

        private final Page page;
        private final int current;
        private final int total;

        private Context(Page page, int current, int total) {
            this.page = page;
            this.current = current;
            this.total = total;
        }

        public Page getPage() {
            return page;
        }

        public int getCurrent() {
            return current;
        }

        public int getTotal() {
            return total;
        }

    }

    public static Builder<Element> builder(InventoryArchetype archetype) {
        return new Builder<>(archetype);
    }

    public static final class Builder<T> {

        private static final Function<Context, Text> DEFAULT_TITLE = c -> Text.of("Page " + c.getCurrent());
        private static final ImmutableMap<Element, Function<Context, Element>> DEFAULT_ICONS = ImmutableMap.<Element, Function<Context, Element>>builder()
                .put(Page.FIRST, c -> icon(c, "First", 1))
                .put(Page.PREVIOUS, c -> icon(c, "Previous", Math.max(c.getCurrent(), 1)))
                .put(Page.CURRENT, c -> icon(c, "Current", c.getCurrent()))
                .put(Page.NEXT, c -> icon(c, "Next", Math.min(c.getCurrent(), c.getTotal())))
                .put(Page.LAST, c -> icon(c, "Last", c.getTotal()))
                .build();

        private static Element icon(Context context, String name, int target) {
            return Element.of(ItemStack.builder()
                    .itemType(context.getCurrent() == target ? ItemTypes.MAP : ItemTypes.PAPER)
                    .add(Keys.DISPLAY_NAME, Text.of(name, " (", target, ")"))
                    .quantity(context.getTotal() > 64 ? target : 1)
                    .build(), a -> context.getPage().open(a.getPlayer(), target));
        }

        private final InventoryArchetype archetype;
        private Function<Context, Text> title = DEFAULT_TITLE;
        private final Map<Element, Function<Context, Element>> icons = Maps.newHashMap(DEFAULT_ICONS);
        private Function<?, Element> content = Element.class::cast;
        private Layout layout = Layout.EMPTY;

        private Builder(InventoryArchetype archetype) {
            this.archetype = archetype;
        }

        public Builder<T> title(Function<Context, Text> function) {
            title = function;
            return this;
        }

        public Builder<T> icon(Element icon, Function<Context, Element> function) {
            icons.put(icon, function);
            return this;
        }

        public <T> Builder<T> content(Function<T, Element> function) {
            content = function;
            return (Builder<T>) this;
        }

        public Builder<T> layout(Layout layout) {
            this.layout = layout;
            return this;
        }

        public Page<T> build(PluginContainer container) {
            return new Page<>(this, container);
        }

    }

}
