package com.g4vrk.fastTextFormatter.colorizer.impl;

import com.g4vrk.fastTextFormatter.colorizer.Colorizer;
import com.g4vrk.fastTextFormatter.util.Legacy2MiniMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class MixedColorizer implements Colorizer {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    @Override
    public @NotNull Component colorize(@NotNull String raw) {

        boolean hasLegacy = raw.indexOf('&') != -1 || raw.indexOf('§') != -1;
        boolean hasMini = raw.indexOf('<') != -1 && raw.indexOf('>') != -1;

        if (hasLegacy && !hasMini) {
            return AMPERSAND_SERIALIZER.deserialize(raw);
        }

        if (!hasLegacy && hasMini) {
            return MINI_MESSAGE.deserialize(raw);
        }

        if (!hasLegacy) {
            return Component.text(raw);
        }

        return MINI_MESSAGE.deserialize(Legacy2MiniMessage.convert(raw));
    }
}