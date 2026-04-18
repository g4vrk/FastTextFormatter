package com.g4vrk.fastTextFormatter.type;

import com.g4vrk.fastTextFormatter.TextFormatter;

/**
 * Данный класс является типом
 * формата текста, используется в {@link TextFormatter}
 */
public enum TextFormatType {

    /**
     * Только legacy -> {@link net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer}
     * <p>
     * Форматы: &c &#RRGGBB &x&R&R&G&G&B&B и такие же, но с '§'
     */
    LEGACY,

    /**
     * Только MiniMessage -> {@link net.kyori.adventure.text.minimessage.MiniMessage}
     * <p>
     * Форматы: Все которые поддерживает MiniMessage
     */
    MINI_MESSAGE,

    /**
     * Legacy + MiniMessage вместе
     */
    MIXED,

// Не представлено в этом проекте.
//
//    /**
//     * Автоматически выбирает нужный тип форматировки.
//     * <p>
//     * Если сервер ниже 1.18.2 - Legacy,
//     * Если выше - Mixed
//     */
//    AUTO
}

