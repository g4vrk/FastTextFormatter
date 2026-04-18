package com.g4vrk.fastTextFormatter.placeholder;

import dev.by1337.plc.Placeholders;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.UnaryOperator;

public final class PlaceholderMap {

    private final Placeholders<OfflinePlayer> placeholders = Placeholders.create();

    public PlaceholderMap() {
    }

    public PlaceholderMap(
            @NotNull Map<String, String> initial
    ) {
        initial.forEach(placeholders::of);
    }

    public @NotNull Placeholders<OfflinePlayer> getResolver() {
        return placeholders;
    }

    public String apply(
            final @NotNull String input,
            final @Nullable OfflinePlayer player
    ) {
        if (input.isEmpty()) return input;

        return placeholders.setPlaceholders(input, player);
    }

    public @NotNull UnaryOperator<String> toUnaryOperator(
            final @Nullable Player player
    ) {
        return (s -> apply(s, player));
    }

    public @NotNull UnaryOperator<String> toUnaryOperator() {
        return toUnaryOperator(null);
    }
}