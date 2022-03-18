package br.com.saraiva.marketplaceinvtex.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.product}")
    private String queueProduct;

    @Value("${rabbitmq.queue.price}")
    private String queuePrice;

    @Value("${rabbitmq.queue.stock}")
    private String queueStock;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.host}")
    private String host;

    @Bean
    public Queue ProductQueue() {
        return new Queue(queueProduct, true);
    }

    @Bean
    public Queue PriceQueue() {
        return new Queue(queuePrice, true);
    }

    @Bean
    public Queue StockQueue() {
        return new Queue(queueStock, true);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

}
