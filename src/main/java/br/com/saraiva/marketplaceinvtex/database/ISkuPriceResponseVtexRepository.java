package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.SkuPriceVtex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISkuPriceResponseVtexRepository extends MongoRepository<SkuPriceVtex, String> {

}
