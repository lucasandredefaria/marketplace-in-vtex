package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.ProductVtex;
import br.com.saraiva.marketplaceinvtex.vtex.SkuVtex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISkuResponseVtexRepository extends MongoRepository<SkuVtex, String> {

}
