package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.model.PriceProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPriceProductRepository extends MongoRepository<PriceProduct, String> {

        PriceProduct findBy_id(String id);
}
