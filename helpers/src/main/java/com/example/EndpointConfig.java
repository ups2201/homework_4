package com.example;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfig {

    @Bean
    public HttpClient citrusHttpClient() {
        return CitrusEndpoints
            .http()
                .client()
                .requestUrl("http://localhost:8080")
            .build();
    }

    @Bean
    public WebServiceClient todoClient() {
        return CitrusEndpoints
            .soap()
            .client()
            .defaultUri("http://localhost:8080/services/ws/todolist")
            .build();
    }

    @Bean
    public JdbcServer jdbcServer() {
        return CitrusEndpoints.jdbc()
            .server()
            .host("localhost")
            .databaseName("testdb")
            .port(3306)
            .timeout(10000L)
            .autoStart(true)
            .build();
    }

    @Bean
    public SingleConnectionDataSource dataSource() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName(JdbcDriver.class.getName());
        dataSource.setUrl(String.format("jdbc:citrus:http://localhost:%s/testdb", DB_SERVER_PORT));
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }


    @Bean
    public EmbeddedKafkaServer embeddedKafkaServer() {
        return new EmbeddedKafkaServerBuilder()
            .kafkaServerPort(KAFKA_BROKER_PORT)
            .topics("todo.inbound", "todo.report")
            .build();
    }

    @Bean
    public KafkaEndpoint todoKafkaEndpoint() {
        return CitrusEndpoints
            .kafka()
            .asynchronous()
            .server(String.format("localhost:%s", KAFKA_BROKER_PORT))
            .topic("todo.inbound")
            .build();
    }

    @Bean
    public KafkaEndpoint todoReportEndpoint() {
        return CitrusEndpoints
            .kafka()
            .asynchronous()
            .server(String.format("localhost:%s", KAFKA_BROKER_PORT))
            .topic("todo.report")
            .offsetReset("earliest")
            .build();
    }

    @Bean
    public SimpleJsonSchema productSchema() {
        return new SimpleJsonSchema(
            new ClassPathResource("classpath:com/consol/citrus/validation/ProductsSchema.json"));
    }
}
