package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.ResponseErrorVtexDTO;
import br.com.saraiva.marketplaceinvtex.vtex.ResponseProductVtexDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductResponseVtexRepository extends MongoRepository<ResponseProductVtexDTO, String> {

    //Product findBy_id(String id);
}
