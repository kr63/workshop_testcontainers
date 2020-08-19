package com.example.demo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:tc:postgresql:10-alpine://testcontainers/workshop",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
@ContextConfiguration(
        initializers = AbstractIntegrationTest.Initializer.class
)
public abstract class AbstractIntegrationTest {

//    static {
//        GenericContainer<?> redis = new GenericContainer<>("redis:3-alpine")
//                .withExposedPorts(6379);
////        redis.start();
//
//        System.setProperty("spring.redis.host", redis.getContainerIpAddress());
//        System.setProperty("spring.redis.port", redis.getFirstMappedPort() + "");
//
////        kafka.start();
//        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
//    }

    protected RequestSpecification requestSpecification;
    @LocalServerPort
    protected int localServerPort;

    @Before
    public void setUpAbstractIntegrationTest() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpecification = new RequestSpecBuilder()
                .setPort(localServerPort)
                .addHeader(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static GenericContainer<?> redis = new GenericContainer<>("redis:3-alpine")
                .withExposedPorts(6379);
        static KafkaContainer kafka = new KafkaContainer();

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

            Startables.deepStart(Stream.of(redis, kafka)).join();

            TestPropertyValues.of(
                    "spring.redis.host=" + redis.getContainerIpAddress(),
                    "spring.redis.port=" + redis.getMappedPort(6379),
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()

            ).applyTo(applicationContext);

        }
    }
}
