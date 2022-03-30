package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.EanSkuVtex;
import br.com.saraiva.marketplaceinvtex.vtex.SkuVtex;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISkuEanResponseVtexRepository extends MongoRepository<EanSkuVtex, String> {

}
