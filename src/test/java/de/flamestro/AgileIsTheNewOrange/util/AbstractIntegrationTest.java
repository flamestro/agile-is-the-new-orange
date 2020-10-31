package de.flamestro.AgileIsTheNewOrange.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class AbstractIntegrationTest {

    @Container
    private static final MongoDBContainer MONGO_DB = new MongoDBContainer("mongo:4.0.4");

    @DynamicPropertySource
    private static void mongoDBProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_DB::getUri);
    }

}
