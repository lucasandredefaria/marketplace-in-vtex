package br.com.saraiva.marketplaceinvtex.model;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Document(collection = "sku")
public class SkuInserirRequestMessage implements Serializable {

    @Id
    private ObjectId id;
    private String skuLojista;
    private String skuSaraiva;
    private String ean;
    private String isbn;
    private BigDecimal precoDe;
    private BigDecimal precoPor;
    private int estoque;
    private String tipo;
    private Double peso;
    private String status;
    private List<CaracteristicaSku> caracteristicas;
    private List<Image> imagens;

}
