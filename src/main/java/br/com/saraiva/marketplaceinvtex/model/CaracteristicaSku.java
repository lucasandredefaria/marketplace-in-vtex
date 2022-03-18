package br.com.saraiva.marketplaceinvtex.model;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Document(collection = "skuCharacteristics")
public class CaracteristicaSku implements Serializable {

    @Id
    private ObjectId _id;
    private String id;
    private String valor;
    private Date dateProcessing;

}
