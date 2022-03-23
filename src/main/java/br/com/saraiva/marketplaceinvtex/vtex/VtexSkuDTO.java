package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class VtexSkuDTO {

    private Long id;
    private String name;
    private String nameComplete;
    private String skuName;
    private boolean isActive;
    private Long productId;
    private String productName;
    private String productRefId;

    @JsonIgnore
    private List<SkuSeller> skuSellers;

}

@Data
class SkuSeller{
    private String sellerId;
    private Long stockKeepingUnitId;
    private String sellerStockKeepingUnitId;
}
