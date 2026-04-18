package com.g4vrk.fastTextFormatter.colorizer.impl;

import com.g4vrk.fastTextFormatter.colorizer.Colorizer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class MiniMessageColorizer implements Colorizer {

    private static final char MINI_MESSAGE_TAG_START = '<';
    private static final char MINI_MESSAGE_TAG_END = '>';

    public static final MiniMessage MINI_MESSAGE =
            MiniMessage.builder().build();

    public MiniMessageColorizer() {
    }

    @Override
    public @NotNull Component colorize(@NotNull String raw) {
        if (raw.indexOf(MINI_MESSAGE_TAG_START) == -1 || raw.indexOf(MINI_MESSAGE_TAG_END) == -1) return Component.text(raw);

        return MINI_MESSAGE.deserialize(raw);
    }
}
