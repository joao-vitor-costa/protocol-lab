package dev.protocol.lab;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "grpc.server.inProcessName=test")
class ProtocolLabApplicationTests {

    @Test
    void contextLoads() {
    }
}
