package dev.protocol.lab.rest;

import dev.protocol.lab.model.Message;
import dev.protocol.lab.model.MessageInput;
import dev.protocol.lab.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;

    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<Message> list() {
        return messageService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message create(@Valid @RequestBody MessageInput input) {
        return messageService.create(input);
    }
}
