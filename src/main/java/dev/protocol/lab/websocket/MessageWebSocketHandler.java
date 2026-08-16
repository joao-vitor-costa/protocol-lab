package dev.protocol.lab.websocket;

import dev.protocol.lab.model.Message;
import dev.protocol.lab.model.MessageInput;
import dev.protocol.lab.service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {

    private final MessageService messageService;
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public MessageWebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        session.sendMessage(new TextMessage("Conectado ao canal /ws/messages. Envie texto para publicar um evento."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        Message message = messageService.create(new MessageInput("WebSocket", textMessage.getPayload()));
        TextMessage outbound = new TextMessage("%s: %s".formatted(message.author(), message.content()));
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(outbound);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
