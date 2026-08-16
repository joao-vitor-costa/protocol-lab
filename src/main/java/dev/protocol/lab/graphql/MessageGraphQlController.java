package dev.protocol.lab.graphql;

import dev.protocol.lab.model.Message;
import dev.protocol.lab.model.MessageInput;
import dev.protocol.lab.service.MessageService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageGraphQlController {

    private final MessageService messageService;

    public MessageGraphQlController(MessageService messageService) {
        this.messageService = messageService;
    }

    @QueryMapping
    public List<Message> messages() {
        return messageService.findAll();
    }

    @MutationMapping
    public Message createMessage(@Argument MessageInput input) {
        return messageService.create(input);
    }
}
