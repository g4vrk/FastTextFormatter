package com.g4vrk.fastTextFormatter.function;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TextPostProcessor extends Function<Component, Component> {
    static @NotNull TextPostProcessor of(@NotNull Function<Component, Component> function) {
        return function::apply;
    }
}
