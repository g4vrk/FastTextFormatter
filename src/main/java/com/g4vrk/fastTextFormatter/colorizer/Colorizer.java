package com.g4vrk.fastTextFormatter.colorizer;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Colorizer {
    @NotNull Component colorize(@NotNull String raw);
}
