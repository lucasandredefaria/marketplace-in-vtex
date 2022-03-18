package br.com.saraiva.marketplaceinvtex.service;

import br.com.saraiva.marketplaceinvtex.database.IStockProductRepository;
import br.com.saraiva.marketplaceinvtex.model.PriceProduct;
import br.com.saraiva.marketplaceinvtex.model.StockProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Slf4j
@Service
public class StockService {

    @Autowired
    private IStockProductRepository mongoDB;

    public void processingStock(StockProduct stockProduct){

        saveProcessing(stockProduct);

        sendPriceToVtex(stockProduct);

        log.info("FOI: " + stockProduct);
    }

    private void saveProcessing(StockProduct stockProduct) {
        log.info("Saving the log in mongo: {}", stockProduct);
        stockProduct.setDateProcessing(Calendar.getInstance().getTime());
        stockProduct.setSkuSaraiva("12321");
        mongoDB.insert(stockProduct);
        log.info("Log saved: {}", stockProduct);
    }

    private void sendPriceToVtex(StockProduct stockProduct){
        log.info("Sent price update for VTEX: {}", stockProduct);

        log.info("Update sent to VTEX: {}", stockProduct);
    }
}
