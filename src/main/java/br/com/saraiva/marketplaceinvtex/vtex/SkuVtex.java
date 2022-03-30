package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document(collection = "skuVtex")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkuVtex {

    private String CodeResponse;
    private String message;
    private Date dateProcessing;
    private String operation;

}