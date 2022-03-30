package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.SkuFileVtex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISkuFileResponseVtexRepository extends MongoRepository<SkuFileVtex, String> {

}
