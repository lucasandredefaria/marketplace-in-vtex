package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.model.Product;
import br.com.saraiva.marketplaceinvtex.model.SkuInserirRequestMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISkuRepository extends MongoRepository<SkuInserirRequestMessage, String> {

        //Product findBy_id(String id);
}
