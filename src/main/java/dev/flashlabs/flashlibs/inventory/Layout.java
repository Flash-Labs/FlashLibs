package dev.flashlabs.flashlibs.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.spongepowered.api.item.inventory.property.InventoryDimension;

import java.util.Map;

public final class Layout {

    public static final Layout EMPTY = Layout.builder(0, 0).build();

    private final InventoryDimension dimension;
    private final ImmutableMap<Integer, Element> elements;

    private Layout(Builder builder) {
        dimension = InventoryDimension.of(builder.columns, builder.rows);
        elements = ImmutableMap.copyOf(builder.elements);
    }

    public InventoryDimension getDimension() {
        return dimension;
    }

    public ImmutableMap<Integer, Element> getElements() {
        return elements;
    }

    public static Builder builder(int rows, int columns) {
        return new Builder(rows, columns);
    }

    public static final class Builder {

        private final Map<Integer, Element> elements = Maps.newHashMap();
        private final int rows;
        private final int columns;

        private Builder(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
        }

        public Builder set(Element element, int index) {
            elements.put(index, element);
            return this;
        }

        public Builder set(Element element, int... indices) {
            for (int index : indices) {
                set(element, index);
            }
            return this;
        }

        public Void set(Element element) throws NoSuchMethodException {
            throw new NoSuchMethodException();
        }

        public Builder set(Map<Integer, Element> elements) {
            elements.forEach((i, e) -> set(e, i));
            return this;
        }

        public Builder row(Element element, int index) {
            for (int i = 0; i < columns; i++) {
                set(element, index * columns + i);
            }
            return this;
        }

        public Builder column(Element element, int index) {
            for (int i = index; i < rows * columns; i += columns) {
                set(element, index);
            }
            return this;
        }

        public Builder border(Element element) {
            for (int i = 0; i < columns; i++) {
                set(element, i, rows * columns - i - 1);
            }
            for (int i = columns; i < rows * columns; i += columns) {
                set(element, i, i - 1);
            }
            return this;
        }

        public Builder fill(Element element) {
            for (int i = 0; i < rows * columns; i++) {
                elements.putIfAbsent(i, element);
            }
            return this;
        }

        public Layout build() {
            return new Layout(this);
        }

    }

}
