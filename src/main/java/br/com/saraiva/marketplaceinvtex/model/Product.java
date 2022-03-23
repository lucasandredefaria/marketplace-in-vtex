package br.com.saraiva.marketplaceinvtex.model;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Document(collection = "product")
public class Product implements Serializable {

    @Id
    private String _id;
    private Long idLojista;
    private String nome;
    private String descricao;
    private Long idCategoriaSaraiva;
    private Long idMarcaSaraiva;
    private Long idDepartamento;

    @DBRef
    private SkuInserirRequestMessage Skus;
    private Date dateProcessing;

}
