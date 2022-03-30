package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class VtexFileSkuDTO {

    private boolean isMain;
    private String label;
    private String name;
    private String text;
    private String url;

}
