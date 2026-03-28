package com.g4vrk.fastTextFormatter;

import com.g4vrk.fastTextFormatter.colorizer.Colorizer;
import com.g4vrk.fastTextFormatter.colorizer.impl.LegacyColorizer;
import com.g4vrk.fastTextFormatter.colorizer.impl.MiniMessageColorizer;
import com.g4vrk.fastTextFormatter.colorizer.impl.MixedColorizer;
import com.g4vrk.fastTextFormatter.type.TextFormatType;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.g4vrk.fastTextFormatter.colorizer.impl.LegacyColorizer.LEGACY_SERIALIZER;

public class TextFormatter {

    private static final PlainTextComponentSerializer PLAIN_SERIALIZER =
            PlainTextComponentSerializer.plainText();

    private final TextFormatType type;
    private final boolean cache;

    private final Colorizer colorizer;

    private final Cache<String, Component> resultCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(Duration.ofMinutes(10))
            .build();

    private TextFormatter(Builder builder) {
        this.type = builder.type;
        this.cache = builder.cache;
        this.colorizer = switch (type) {
            case MINI_MESSAGE -> new MiniMessageColorizer();
            case LEGACY -> new LegacyColorizer();
            case MIXED -> new MixedColorizer();
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static TextFormatter textFormatter() {
        return builder()
                .type(TextFormatType.MIXED)
                .cache(true)
                .build();
    }

    public @NotNull Component format(@NotNull String input) {
        if (!cache) return colorizer.colorize(input);

        return resultCache.get(input, colorizer::colorize);
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

    public TextFormatType getType() {
        return this.type;
    }

    public boolean isCache() {
        return this.cache;
    }

    public static class Builder {

        private TextFormatType type = TextFormatType.MIXED;
        private boolean cache = true;

        public Builder type(TextFormatType type) {
            this.type = type;
            return this;
        }

        public Builder cache(boolean cache) {
            this.cache = cache;
            return this;
        }

        public TextFormatter build() {
            return new TextFormatter(this);
        }
    }
}