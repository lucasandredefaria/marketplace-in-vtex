package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document(collection = "skuPriceVtex")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkuPriceVtex {

    private String codeResponse;
    private String message;
    private Date dateProcessing;
    private String operation;

}