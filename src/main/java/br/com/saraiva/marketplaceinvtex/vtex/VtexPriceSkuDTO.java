package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class VtexPriceSkuDTO {

    private BigDecimal listPrice;
    private BigDecimal costPrice;
    private BigDecimal basePrice;
    private BigDecimal markup;

}
