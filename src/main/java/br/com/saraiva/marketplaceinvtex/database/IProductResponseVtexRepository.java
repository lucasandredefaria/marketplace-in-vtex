package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.ProductVtex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductResponseVtexRepository extends MongoRepository<ProductVtex, String> {

}
