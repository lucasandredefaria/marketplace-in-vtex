package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.vtex.ResponseErrorVtexDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IResponseErrorVtexRepository extends MongoRepository<ResponseErrorVtexDTO, String> {

    //Product findBy_id(String id);
}
