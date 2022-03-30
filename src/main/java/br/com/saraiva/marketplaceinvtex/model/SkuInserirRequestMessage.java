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
    private ObjectId _id;
    private Long idLojista;
    private Long idVtex;
    private String skuLojista;
    private String skuSaraiva;
    private String ean;
    private String isbn;
    private String releaseDate;
    private String title;
    private BigDecimal precoDe;
    private BigDecimal precoPor;
    private int estoque;
    private String tipo;
    private BigDecimal peso;
    private BigDecimal altura;
    private BigDecimal cumprimento;
    private BigDecimal largura;
    private String status;
    private List<CaracteristicaSku> caracteristicas;
    private List<Image> imagens;
    private Date dateProcessing;

}
