package dev.flashlabs.flashlibs.text;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a reusable template for messages containing arguments. The
 * template can be applied to a series of arguments.
 *
 * <p>Arguments are defined inside of {@code ${}}, as in string interpolation,
 * which may be escaped with a leading backslash such as in {@code \${}}. The
 * brackets contain three parts: the format, a literal {@code @} symbol, and the
 * argument key. The format is optional {@code &}-style codes, and the key is
 * any kebab-case string (lowercase and hyphen only). Some examples are {@code
 * ${@player}}, {@code ${&6&l@player}}, and {@code ${&a@player-location}}.
 */
public final class MessageTemplate {

    private static final Pattern ARGUMENT = Pattern.compile("(?<!\\\\)\\$\\{(.*?)}");
    private static final Pattern PLACEHOLDER = Pattern.compile("((?:&[0-9a-fk-or])*)@([a-z-]+)");

    private final TextTemplate template;

    private MessageTemplate(TextTemplate template) {
        this.template = template;
    }

    /**
     * Creates a template from the given string following the format described
     * in the class javadoc. If an argument is defined with {@code ${}} but the
     * contents are invalid, the contents are included directly in the result.
     *
     * @see MessageTemplate
     */
    public static MessageTemplate of(String string) {
        List<Object> elements = Lists.newArrayList();
        Matcher argument = ARGUMENT.matcher(string);
        int index = 0;
        while (argument.find()) {
            if (index < argument.start()) {
                elements.add(TextSerializers.FORMATTING_CODE.deserialize(string.substring(index, argument.start())));
            }
            Matcher placeholder = PLACEHOLDER.matcher(argument.group(1));
            if (placeholder.matches()) {
                TextTemplate.Arg.Builder builder = TextTemplate.arg(placeholder.group(2));
                if (placeholder.group(1) != null) {
                    builder.format(TextSerializers.FORMATTING_CODE.deserialize(placeholder.group(1) + "?").getFormat());
                }
                elements.add(builder);
            } else {
                elements.add(Text.of(argument.group(1)));
            }
            index = argument.end();
        }
        if (index < string.length()) {
            elements.add(TextSerializers.FORMATTING_CODE.deserialize(string.substring(index)));
        }
        return new MessageTemplate(TextTemplate.of(elements.toArray()));
    }

    /**
     * Applies the given arguments to this template using the given map, adding
     * any missing arguments to the map with {@code @key}. The {@param args} map
     * is mutated in the process.
     */
    private Text apply(Map<String, Object> args) {
        template.getArguments().keySet().forEach(k -> args.putIfAbsent(k, "@" + k));
        return template.apply(args).build();
    }

    /**
     * Gets this message with the given arguments applied. Unused arguments are
     * ignored and uses of undefined arguments are replaced with {@code @key}.
     */
    public Text get(Map<String, Object> args) {
        return apply(Maps.newHashMap(args));
    }

    /**
     * Gets this message with the given argument applied. Arguments are supplied
     * by key-value pairs, raising an exception for an incomplete pair. Unused
     * arguments are ignored and uses of undefined arguments are replaced with
     * {@code @key}.
     *
     * @throws IllegalArgumentException If a given argument is missing a value.
     */
    public Text get(Object... args) {
        if (args.length % 2 == 1) {
            throw new IllegalArgumentException("Argument " + args[args.length - 1] + " is missing a value.");
        }
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 1; i < args.length; i += 2) {
            map.put(String.valueOf(args[i - 1]), args[i]);
        }
        return apply(map);
    }

}
