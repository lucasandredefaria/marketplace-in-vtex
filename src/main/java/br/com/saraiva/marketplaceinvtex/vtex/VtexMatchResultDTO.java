package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VtexMatchResultDTO {

    private String operation;
    private String message;
    private String details;
    private String productId;
    private String skuId;
    private String suggestionStatus;
    private String error;

}
