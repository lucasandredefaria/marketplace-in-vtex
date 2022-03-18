package br.com.saraiva.marketplaceinvtex.service;

import br.com.saraiva.marketplaceinvtex.database.IProductRepository;
import br.com.saraiva.marketplaceinvtex.database.IStockProductRepository;
import br.com.saraiva.marketplaceinvtex.model.Product;
import br.com.saraiva.marketplaceinvtex.model.StockProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private IProductRepository mongoDB;

    public void processingProduct(Product product){

        saveProcessing(product);

        sendPriceToVtex(product);

        log.info("FOI: " + product);
    }

    private void saveProcessing(Product product) {
        log.info("Saving the log in mongo: {}", product);
        product.setDateProcessing(Calendar.getInstance().getTime());
        mongoDB.insert(product);
        log.info("Log saved: {}", product);
    }

    private void sendPriceToVtex(Product product){
        log.info("Sent price update for VTEX: {}", product);

        log.info("Update sent to VTEX: {}", product);
    }
}
