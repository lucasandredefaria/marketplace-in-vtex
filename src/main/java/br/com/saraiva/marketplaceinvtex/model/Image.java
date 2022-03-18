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
@Document(collection = "image")
public class Image implements Serializable {

    @Id
    private ObjectId id;
    private String urlInterna;
    private boolean capa;
    private Date dateProcessing;

}
