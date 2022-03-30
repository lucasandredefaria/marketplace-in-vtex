package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document(collection = "skuEanVtex")
@JsonIgnoreProperties(ignoreUnknown = true)
public
class EanSkuVtex {

    private String CodeResponse;
    private String ean;
    private String idSkuVtex;
    private Date dateProcessing;
    private String operation;

}
