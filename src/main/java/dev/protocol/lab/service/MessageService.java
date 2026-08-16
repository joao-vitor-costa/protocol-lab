package dev.protocol.lab.service;

import dev.protocol.lab.model.Message;
import dev.protocol.lab.model.MessageInput;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessageService {

    private final List<Message> messages = new CopyOnWriteArrayList<>(List.of(
            new Message("welcome-rest", "REST", "Recurso simples por HTTP/JSON", Instant.parse("2026-01-01T10:00:00Z")),
            new Message("welcome-graphql", "GraphQL", "Consulta flexível no mesmo endpoint", Instant.parse("2026-01-01T10:01:00Z")),
            new Message("welcome-grpc", "gRPC", "Contrato Protobuf binário e performático", Instant.parse("2026-01-01T10:02:00Z")),
            new Message("welcome-ws", "WebSocket", "Canal bidirecional em tempo real", Instant.parse("2026-01-01T10:03:00Z"))
    ));

    public List<Message> findAll() {
        return new ArrayList<>(messages);
    }

    public Message create(MessageInput input) {
        Message message = new Message(UUID.randomUUID().toString(), input.author(), input.content(), Instant.now());
        messages.add(message);
        return message;
    }
}
