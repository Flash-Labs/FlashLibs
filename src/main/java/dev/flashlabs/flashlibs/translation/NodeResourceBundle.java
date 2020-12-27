package dev.flashlabs.flashlibs.translation;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import sun.util.ResourceBundleEnumeration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.annotation.Nullable;

/**
 * Represents a {@link ResourceBundle} loaded from an {@link ConfigurationNode}.
 * All values are registered under the path to the node, consisting of all keys
 * joined with {@code '.'}.
 */
final class NodeResourceBundle extends ResourceBundle {

    static final Control CONTROL = new Control();

    private final Map<String, Object> map = Maps.newHashMap();

    private NodeResourceBundle(ConfigurationNode node) {
        loadValues(node);
    }

    private void loadValues(ConfigurationNode node) {
        if (node.hasListChildren()) {
            node.getChildrenList().forEach(this::loadValues);
        } else if (node.hasMapChildren()) {
            node.getChildrenMap().values().forEach(this::loadValues);
        } else {
            map.put(Joiner.on('.').join(node.getPath()), node.getValue());
        }
    }

    @Override
    protected Object handleGetObject(String key) {
        return map.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return new ResourceBundleEnumeration(map.keySet(), parent != null ? parent.getKeys() : null);
    }

    /**
     * Custom {@link ResourceBundle.Control} implementation. This supports Hocon
     * (.conf), Json (.json), and Yaml (.yaml) via Configurate, as well as UTF-8
     * encoded properties files .
     */
    private static final class Control extends ResourceBundle.Control {

        private static final List<String> FORMATS = ImmutableList.of("conf", "json", "yaml", "properties");

        @Override
        public List<String> getFormats(String baseName) {
            return FORMATS;
        }

        @Override
        @Nullable
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            URL url = loader.getResource(toResourceName(toBundleName(baseName, locale), format));
            if (url != null) {
                switch (format) {
                    case "conf":
                        return new NodeResourceBundle(HoconConfigurationLoader.builder().setURL(url).build().load());
                    case "json":
                        return new NodeResourceBundle(GsonConfigurationLoader.builder().setURL(url).build().load());
                    case "yaml":
                        return new NodeResourceBundle(YAMLConfigurationLoader.builder().setURL(url).build().load());
                    case "properties":
                        URLConnection connection = url.openConnection();
                        connection.setUseCaches(!reload);
                        try (InputStream stream = connection.getInputStream()) {
                            return new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                        }
                    default: throw new AssertionError(format);
                }
            }
            return null;
        }

    }

}
