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

Teste o endpoint listando e criando mensagens:

```bash
curl http://localhost:8080/api/messages
curl -X POST http://localhost:8080/api/messages \
  -H 'Content-Type: application/json' \
  -d '{"author":"REST","content":"Mensagem via REST"}'
```

### GraphQL

Teste a consulta abaixo ou abra `http://localhost:8080/graphiql` no navegador:

```bash
curl -X POST http://localhost:8080/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"query { messages { id author content createdAt } }"}'
```

### gRPC/RPC

No Windows, instale o `grpcurl` pelo `winget` (use o ID retornado pela pesquisa):

```powershell
winget search grpcurl
winget install --id <ID_DO_GRPCURL> -e
grpcurl --version
```

Com `grpcurl` instalado, liste os serviços, consulte mensagens e crie uma nova:

```powershell
grpcurl -plaintext localhost:9090 list
grpcurl -plaintext localhost:9090 describe MessageRpc
grpcurl -plaintext -import-path src/main/proto -proto message.proto -d '{}' localhost:9090 MessageRpc/ListMessages
grpcurl -plaintext -import-path src/main/proto -proto message.proto -d '{"author":"gRPC","content":"Mensagem via RPC"}' localhost:9090 MessageRpc/CreateMessage
```

O contrato está em `src/main/proto/message.proto`.

### WebSocket

No Windows, instale o `websocat` pelo `winget` (use o ID retornado pela pesquisa):

```powershell
winget search websocat
winget install --id <ID_DO_WEBSOCAT> -e
websocat --version
```

Abra dois terminais e conecte ambos ao endpoint:

```powershell
websocat ws://localhost:8080/ws/messages
```

Digite qualquer texto em um terminal. A mensagem deverá aparecer no outro, demonstrando o broadcast aos clientes conectados.

## Estrutura

- `rest`: controladores HTTP/JSON.
- `graphql`: resolvers GraphQL e schema em `src/main/resources/graphql/schema.graphqls`.
- `grpc`: serviço gRPC gerado a partir de Protobuf.
- `websocket`: handler WebSocket nativo do Spring.
- `service` e `model`: domínio compartilhado para comparar protocolos sem mudar regra de negócio.
