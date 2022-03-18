package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.model.StockProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IStockProductRepository extends MongoRepository<StockProduct, String> {

        StockProduct findBy_id(String id);
}
