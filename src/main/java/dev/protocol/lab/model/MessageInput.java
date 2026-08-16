package dev.protocol.lab.model;

import jakarta.validation.constraints.NotBlank;

public record MessageInput(
        @NotBlank String author,
        @NotBlank String content
) {
}
