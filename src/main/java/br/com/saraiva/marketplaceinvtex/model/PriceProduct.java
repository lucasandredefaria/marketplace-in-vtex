package br.com.saraiva.marketplaceinvtex.model;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@Document(collection = "price")
public class PriceProduct implements Serializable {

    @Id
    private ObjectId _id;
    private Long idLojista;
    private String skuLojista;
    private String skuSaraiva;
    private String ean;
    private String isbn;
    private BigDecimal precoDe;
    private BigDecimal precoPor;
    private Date dateProcessing;

}
