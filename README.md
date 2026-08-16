# protocol-lab

POC (Proof of Concept) em Java e Spring Boot para explorar e comparar REST, GraphQL, gRPC/RPC e WebSocket usando o mesmo domínio: mensagens.

## O que este laboratório demonstra

| Protocolo | Endpoint/porta | Quando usar | Principal diferença na POC |
| --- | --- | --- | --- |
| REST | `GET/POST /api/messages` | CRUD simples, cache HTTP, integração ampla | Recursos HTTP com JSON e status codes |
| GraphQL | `POST /graphql` e UI `/graphiql` | Clientes que precisam escolher campos e reduzir over/under-fetching | Uma query/mutation em schema tipado |
| gRPC/RPC | porta `9090`, serviço `MessageRpc` | Comunicação interna performática e contrato-first | Protobuf binário com métodos RPC |
| WebSocket | `/ws/messages` | Comunicação bidirecional em tempo real | Conexão persistente que transmite eventos para todos os clientes conectados |

## Requisitos

- Java 21+
- Maven 3.9+

## Executar

```bash
mvn spring-boot:run
```

A aplicação HTTP sobe em `http://localhost:8080` e o servidor gRPC em `localhost:9090`.

## Exemplos rápidos

### REST

```bash
curl http://localhost:8080/api/messages
curl -X POST http://localhost:8080/api/messages \
  -H 'Content-Type: application/json' \
  -d '{"author":"REST","content":"Mensagem via REST"}'
```

### GraphQL

```bash
curl -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"query { messages { id author content createdAt } }"}'
```

Também é possível abrir `http://localhost:8080/graphiql` no navegador.

### gRPC/RPC

Com `grpcurl` instalado:

```bash
grpcurl -plaintext -import-path src/main/proto -proto message.proto -d '{}' localhost:9090 MessageRpc/ListMessages
grpcurl -plaintext -import-path src/main/proto -proto message.proto -d '{"author":"gRPC","content":"Mensagem via RPC"}' localhost:9090 MessageRpc/CreateMessage
```

O contrato está em `src/main/proto/message.proto`.

### WebSocket

Com `websocat` instalado:

```bash
websocat ws://localhost:8080/ws/messages
```

Digite qualquer texto no terminal para publicar uma mensagem em broadcast aos clientes conectados.

## Estrutura

- `rest`: controladores HTTP/JSON.
- `graphql`: resolvers GraphQL e schema em `src/main/resources/graphql/schema.graphqls`.
- `grpc`: serviço gRPC gerado a partir de Protobuf.
- `websocket`: handler WebSocket nativo do Spring.
- `service` e `model`: domínio compartilhado para comparar protocolos sem mudar regra de negócio.
