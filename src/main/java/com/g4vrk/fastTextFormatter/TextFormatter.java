package com.g4vrk.fastTextFormatter;

import com.g4vrk.fastTextFormatter.colorizer.Colorizer;
import com.g4vrk.fastTextFormatter.colorizer.impl.LegacyColorizer;
import com.g4vrk.fastTextFormatter.colorizer.impl.MiniMessageColorizer;
import com.g4vrk.fastTextFormatter.colorizer.impl.MixedColorizer;
import com.g4vrk.fastTextFormatter.function.TextPostProcessor;
import com.g4vrk.fastTextFormatter.function.TextPreProcessor;
import com.g4vrk.fastTextFormatter.type.TextFormatType;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.g4vrk.fastTextFormatter.colorizer.impl.LegacyColorizer.LEGACY_SERIALIZER;

public class TextFormatter {

    private static final TextFormatter DEFAULT_INSTANCE = builder()
            .type(TextFormatType.MIXED)
            .cache(true)
            .build();

    private static final PlainTextComponentSerializer PLAIN_SERIALIZER =
            PlainTextComponentSerializer.plainText();

    private final TextFormatType type;
    private final boolean cache;

    private final List<TextPreProcessor> preProcessors;
    private final List<TextPostProcessor> postProcessors;

    private final Colorizer colorizer;

    private final Cache<String, Component> resultCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();

    private TextFormatter(@NotNull Builder builder) {
        this.type = builder.type;
        this.cache = builder.cache;
        this.preProcessors = builder.preProcessors;
        this.postProcessors = builder.postProcessors;
        this.colorizer = switch (type) {
            case MINI_MESSAGE -> new MiniMessageColorizer();
            case LEGACY -> new LegacyColorizer();
            case MIXED -> new MixedColorizer();
        };
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Contract(pure = true)
    public static TextFormatter textFormatter() {
        return DEFAULT_INSTANCE;
    }

    public @NotNull Component formatWithPreProcessors(@NotNull String input, @NotNull Iterable<TextPreProcessor> preProcessors) {
        return format(preProcess(input, preProcessors));
    }

    public @NotNull Component formatWithPostProcessors(@NotNull String input, @NotNull Iterable<TextPostProcessor> postProcessors) {
        return postProcess(format(input), postProcessors);
    }

    public @NotNull Component format(@NotNull String input) {
        if (!cache) {
            return apply(input, colorizer::colorize);
        }

        return resultCache.get(input, string -> apply(string, colorizer::colorize));
    }

    public @NotNull String preProcess(@NotNull String in, @NotNull Iterable<TextPreProcessor> preProcessors) {
        for (final TextPreProcessor preProcessor : preProcessors) {
            in = preProcessor.apply(in);
        }

        return in;
    }

    public @NotNull Component postProcess(@NotNull Component in, @NotNull Iterable<TextPostProcessor> postProcessors) {
        for (final TextPostProcessor postProcessor : postProcessors) {
            in = postProcessor.apply(in);
        }

        return in;
    }

    public @NotNull String legacy(@NotNull String input) {
        return legacy(format(input));
    }

    public @NotNull String legacy(@NotNull Component input) {
        return LEGACY_SERIALIZER.serialize(input);
    }

    public @NotNull String plain(@NotNull String input) {
        return plain(format(input));
    }

    public @NotNull String plain(@NotNull Component input) {
        return PLAIN_SERIALIZER.serialize(input);
    }

    private @NotNull Component apply(
            final @NotNull String in,
            final @NotNull Function<String, Component> mapper
    ) {
        final Component out = mapper.apply(preProcess(in, preProcessors));

        return postProcess(out, postProcessors);
    }

    public @NotNull TextFormatType getType() {
        return this.type;
    }

    public boolean isCache() {
        return this.cache;
    }

    public static class Builder {

        private TextFormatType type = TextFormatType.MIXED;
        private boolean cache = true;

        private final List<TextPreProcessor> preProcessors = new ObjectArrayList<>();
        private final List<TextPostProcessor> postProcessors = new ObjectArrayList<>();

        public @NotNull Builder type(
                final @NotNull TextFormatType type
        ) {
            this.type = type;
            return this;
        }

        public @NotNull Builder cache(
                final boolean cache
        ) {
            this.cache = cache;
            return this;
        }

        public @NotNull Builder preProcessor(final @NotNull TextPreProcessor preProcessor) {
            this.preProcessors.add(preProcessor);
            return this;
        }

        public @NotNull Builder postProcessor(final @NotNull TextPostProcessor postProcessor) {
            this.postProcessors.add(postProcessor);
            return this;
        }

        public @NotNull Builder postProcessors(final @NotNull Collection<TextPostProcessor> postProcessors) {
            this.postProcessors.addAll(postProcessors);
            return this;
        }

        public @NotNull Builder preProcessors(final @NotNull Collection<TextPreProcessor> preProcessors) {
            this.preProcessors.addAll(preProcessors);
            return this;
        }

        public @NotNull TextFormatter build() {
            return new TextFormatter(this);
        }
    }
}