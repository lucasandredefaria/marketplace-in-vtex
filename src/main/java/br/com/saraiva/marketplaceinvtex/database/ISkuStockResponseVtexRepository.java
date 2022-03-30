package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.SkuFileVtex;
import br.com.saraiva.marketplaceinvtex.vtex.SkuStockVtex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISkuStockResponseVtexRepository extends MongoRepository<SkuStockVtex, String> {

}
