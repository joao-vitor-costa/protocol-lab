package dev.protocol.lab.model;

import java.time.Instant;

public record Message(String id, String author, String content, Instant createdAt) {
}
