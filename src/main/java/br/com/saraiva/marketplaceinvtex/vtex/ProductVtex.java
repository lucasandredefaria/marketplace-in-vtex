package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Document(collection = "productVtex")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVtex {

    private String CodeResponse;
    private String message;
    private Date dateProcessing;
    private String operation;

}
