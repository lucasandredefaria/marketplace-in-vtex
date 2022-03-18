package br.com.saraiva.marketplaceinvtex.database;

import br.com.saraiva.marketplaceinvtex.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IProductRepository extends MongoRepository<Product, String> {

        Product findBy_id(String id);
}
