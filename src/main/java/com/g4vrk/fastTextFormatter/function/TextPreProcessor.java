package com.g4vrk.fastTextFormatter.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TextPreProcessor extends Function<String, String> {
    static @NotNull TextPreProcessor of(@NotNull Function<String, String> function) {
        return function::apply;
    }
}
