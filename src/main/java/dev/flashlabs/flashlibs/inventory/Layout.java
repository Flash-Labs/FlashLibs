package dev.flashlabs.flashlibs.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.spongepowered.api.item.inventory.property.InventoryDimension;

import java.util.Map;

/**
 * Represents the position of elements in the inventory by index. The
 * {@link Layout.Builder} class contains utility methods for common patterns.
 */
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

    /**
     * Creates a new builder for layouts with the given dimensions.
     */
    public static Builder builder(int rows, int columns) {
        return new Builder(rows, columns);
    }

    /**
     * A builder for creating {@link Layout}s.
     */
    public static final class Builder {

        private final Map<Integer, Element> elements = Maps.newHashMap();
        private final int rows;
        private final int columns;

        private Builder(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
        }

        /**
         * Sets the element at the given index.
         */
        public Builder set(Element element, int index) {
            elements.put(index, element);
            return this;
        }

        /**
         * Sets the element at the given indices.
         */
        public Builder set(Element element, int... indices) {
            for (int index : indices) {
                set(element, index);
            }
            return this;
        }

        /**
         * The method {@link #set(Element, int...)} may not be called without
         * any indices. This method exists to identify this statically.
         */
        @Deprecated
        public Void set(Element element) throws NoSuchMethodException {
            throw new NoSuchMethodException();
        }

        /**
         * Updates the layout with the given elements.
         */
        public Builder set(Map<Integer, Element> elements) {
            elements.forEach((i, e) -> set(e, i));
            return this;
        }

        /**
         * Sets the element in all slots corresponding to the given row.
         */
        public Builder row(Element element, int index) {
            for (int i = 0; i < columns; i++) {
                set(element, index * columns + i);
            }
            return this;
        }

        /**
         * Sets the element in all slots corresponding to the given column.
         */
        public Builder column(Element element, int index) {
            for (int i = index; i < rows * columns; i += columns) {
                set(element, index);
            }
            return this;
        }

        /**
         * Sets the element in all indices located on the edge of the layout.
         */
        public Builder border(Element element) {
            for (int i = 0; i < columns; i++) {
                set(element, i, rows * columns - i - 1);
            }
            for (int i = columns; i < rows * columns; i += columns) {
                set(element, i, i - 1);
            }
            return this;
        }

        /**
         * Sets this element at all indices currently undefined.
         */
        public Builder fill(Element element) {
            for (int i = 0; i < rows * columns; i++) {
                elements.putIfAbsent(i, element);
            }
            return this;
        }

        /**
         * Creates a Layout from this builder.
         */
        public Layout build() {
            return new Layout(this);
        }

    }

}
