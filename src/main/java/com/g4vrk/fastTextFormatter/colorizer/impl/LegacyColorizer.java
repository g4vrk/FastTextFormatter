package com.g4vrk.fastTextFormatter.colorizer.impl;

import com.g4vrk.fastTextFormatter.colorizer.Colorizer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class LegacyColorizer implements Colorizer {

    public static final LegacyComponentSerializer LEGACY_SERIALIZER =
            LegacyComponentSerializer.builder()
                    .character(LegacyComponentSerializer.SECTION_CHAR)
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();

    public LegacyColorizer() {
    }

    @Override
    public @NotNull Component colorize(@NotNull String raw) {
        int ampersandIndex = raw.indexOf(LegacyComponentSerializer.AMPERSAND_CHAR);
        int sectionIndex = raw.indexOf(LegacyComponentSerializer.SECTION_CHAR);

        if (ampersandIndex == -1 && sectionIndex == -1) {
            return Component.text(raw);
        }

        if (ampersandIndex != -1) {
            raw = raw.replace(LegacyComponentSerializer.AMPERSAND_CHAR, LegacyComponentSerializer.SECTION_CHAR);
        }

        return LEGACY_SERIALIZER.deserialize(raw);
    }
}