package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VtexStockSkuDTO {

    private boolean unlimitedQuantity;
    private String dateUtcOnBalanceSystem;
    private int quantity;

}
