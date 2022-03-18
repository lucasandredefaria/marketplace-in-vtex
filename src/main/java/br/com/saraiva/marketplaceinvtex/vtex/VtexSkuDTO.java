package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;

@Data
public class VtexSkuDTO {

    private Long id;
    private String name;
    private String nameComplete;
    private String skuName;
    public boolean isActive;
    public Long productId;
    private String productName;
    private String productRefId;

}
