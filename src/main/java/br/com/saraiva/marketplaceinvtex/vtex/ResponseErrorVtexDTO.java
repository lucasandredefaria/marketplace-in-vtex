package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document(collection = "responseErrorVtex")
public class ResponseErrorVtexDTO {

    private String CodeResponse;
    private String message;
    private String skuLojista;
    private String skuSaraiva;
    private String ean;
    private String isbn;
    private Long idLojista;
    private Date dateProcessing;

}
