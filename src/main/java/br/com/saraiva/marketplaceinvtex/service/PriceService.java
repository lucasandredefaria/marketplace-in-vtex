package br.com.saraiva.marketplaceinvtex.service;

import br.com.saraiva.marketplaceinvtex.database.IPriceProductRepository;
import br.com.saraiva.marketplaceinvtex.model.PriceProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Slf4j
@Service
public class PriceService {

    @Autowired
    private IPriceProductRepository mongoDB;

    public void processingPrice(PriceProduct priceProduct){

        saveProcessing(priceProduct);

        sendPriceToVtex(priceProduct);

        log.info("FOI: " + priceProduct);
    }

    private void saveProcessing(PriceProduct priceProduct) {
        log.info("Saving the log in mongo: {}", priceProduct);
        priceProduct.setDateProcessing(Calendar.getInstance().getTime());
        priceProduct.setSkuSaraiva("12321");
        mongoDB.insert(priceProduct);
        log.info("Log saved: {}", priceProduct);
    }

    private void sendPriceToVtex(PriceProduct priceProduct){
        log.info("Sent price update for VTEX: {}", priceProduct);

        log.info("Update sent to VTEX: {}", priceProduct);
    }
}
