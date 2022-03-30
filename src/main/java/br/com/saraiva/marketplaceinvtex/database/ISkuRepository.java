package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.model.Product;
import br.com.saraiva.marketplaceinvtex.model.SkuInserirRequestMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISkuRepository extends MongoRepository<SkuInserirRequestMessage, String> {

        SkuInserirRequestMessage findBy_id(String id);
        SkuInserirRequestMessage findByIdVtex(Long id);
        SkuInserirRequestMessage findBySkuSaraivaAndIdLojista(String skuSaraiva, Long idLojista);
}
