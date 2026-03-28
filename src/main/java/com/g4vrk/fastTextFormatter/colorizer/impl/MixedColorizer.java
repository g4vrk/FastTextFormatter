package com.g4vrk.fastTextFormatter.colorizer.impl;

import com.g4vrk.fastTextFormatter.colorizer.Colorizer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import static com.g4vrk.fastTextFormatter.colorizer.impl.MiniMessageColorizer.MINI_MESSAGE;

public class MixedColorizer implements Colorizer {

    private final LegacyColorizer legacyColorizer = new LegacyColorizer();
    private final MiniMessageColorizer miniMessageColorizer = new MiniMessageColorizer();

    @Override
    public @NotNull Component colorize(@NotNull String raw) {
        final boolean hasLegacy =
                raw.indexOf(LegacyComponentSerializer.AMPERSAND_CHAR) != -1 || raw.indexOf(LegacyComponentSerializer.SECTION_CHAR) != -1;

        final boolean hasMini =
                raw.indexOf('<') != -1 && raw.indexOf('>') != -1;

        if (!hasLegacy && !hasMini) return Component.text(raw);

        if (!hasLegacy) return miniMessageColorizer.colorize(raw);

        if (!hasMini) return legacyColorizer.colorize(raw);

        final Component legacyComponent = legacyColorizer.colorize(raw);

        return MINI_MESSAGE.deserialize(
                MINI_MESSAGE.serialize(legacyComponent)
        );
    }
}