package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class VtexProductSuggestionDTO {

    private String brandName;
    private String categoryFullPath;
    private String productName;
    private String skuName;
    private String productDescription;
    private String height;
    private String width;
    private String length;
    private String weight;
    private String ean;
    private String refId;
    private String sellerStockKeepingUnitId;
    private List<ProductSpecification> productSpecifications;
    private List<SkuSpecification> skuSpecifications;
    private List<Image> images;

}

@Data
class ProductSpecification {
    private String fieldName;
    private String fieldValue;
}

@Data
class SkuSpecification {
    private String fieldName;
    private String fieldValue;
}

@Data
class Image {
    private String imageName;
    private String imageUrl;
}


