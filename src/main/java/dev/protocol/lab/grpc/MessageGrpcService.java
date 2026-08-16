package dev.protocol.lab.grpc;

import dev.protocol.lab.grpc.proto.CreateMessageRequest;
import dev.protocol.lab.grpc.proto.ListMessagesRequest;
import dev.protocol.lab.grpc.proto.ListMessagesResponse;
import dev.protocol.lab.grpc.proto.MessageReply;
import dev.protocol.lab.grpc.proto.MessageRpcGrpc;
import dev.protocol.lab.model.Message;
import dev.protocol.lab.model.MessageInput;
import dev.protocol.lab.service.MessageService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MessageGrpcService extends MessageRpcGrpc.MessageRpcImplBase {

    private final MessageService messageService;

    public MessageGrpcService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void listMessages(ListMessagesRequest request, StreamObserver<ListMessagesResponse> responseObserver) {
        ListMessagesResponse.Builder response = ListMessagesResponse.newBuilder();
        messageService.findAll().stream().map(this::toReply).forEach(response::addMessages);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createMessage(CreateMessageRequest request, StreamObserver<MessageReply> responseObserver) {
        Message message = messageService.create(new MessageInput(request.getAuthor(), request.getContent()));
        responseObserver.onNext(toReply(message));
        responseObserver.onCompleted();
    }

    private MessageReply toReply(Message message) {
        return MessageReply.newBuilder()
                .setId(message.id())
                .setAuthor(message.author())
                .setContent(message.content())
                .setCreatedAt(message.createdAt().toString())
                .build();
    }
}
