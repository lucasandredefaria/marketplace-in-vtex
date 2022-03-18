package br.com.saraiva.marketplaceinvtex.queue;

import br.com.saraiva.marketplaceinvtex.model.PriceProduct;
import br.com.saraiva.marketplaceinvtex.model.Product;
import br.com.saraiva.marketplaceinvtex.model.StockProduct;
import br.com.saraiva.marketplaceinvtex.service.PriceService;
import br.com.saraiva.marketplaceinvtex.service.ProductService;
import br.com.saraiva.marketplaceinvtex.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class RabbitMQListener {

    @Autowired
    private PriceService priceService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "${rabbitmq.queue.price}")
    public void recievedMessagePrice(@NotNull Message msg) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = msg.getBody();
        PriceProduct priceProduct = objectMapper.readValue(bytes, PriceProduct.class);
        log.info("Recieved Message From RabbitMQ: {}", priceProduct);
        priceService.processingPrice(priceProduct);
    }

    @RabbitListener(queues = "${rabbitmq.queue.stock}")
    public void recievedMessageStock(@NotNull Message msg) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = msg.getBody();
        StockProduct stockProduct = objectMapper.readValue(bytes, StockProduct.class);
        log.info("Recieved Message From RabbitMQ: {}", stockProduct);
        stockService.processingStock(stockProduct);
    }

    @RabbitListener(queues = "${rabbitmq.queue.product}")
    public void recievedMessageProduct(@NotNull Message msg) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = msg.getBody();
        Product product = objectMapper.readValue(bytes, Product.class);
        log.info("Recieved Message From RabbitMQ: {}", product);
        productService.processingProduct(product);
    }
}
