package br.com.saraiva.marketplaceinvtex.vtex;

public interface IVtexService {

    VtexMatchResultDTO ForceMatch(VtexSuggestionResultDTO suggestionResult);
    VtexSuggestionResultDTO GetSuggestion(Long lojistaId, String refId);
    boolean CreateSuggestion(VtexProductSuggestionDTO suggestion, Long lojistaId, String skuMarketplace);
    boolean RemoveSeller(VtexSkuDTO sku, String sellerId);
    VtexProductDTO GetProduct(Long productId);
    boolean UpdateProduct(VtexProductDTO product);
    Long GetSkuByRef(String skuRef);
    VtexSkuDTO GetSku(Long skuId);
    boolean DeleteEanFromSKU(VtexSkuDTO sku);
    boolean UpdateSku(VtexSkuDTO sku);
    VtexBrandDTO GetBrand(Long brandId);
    VtexCategoryDTO GetCategory(Long categoryId);
    String GetCategoryFullPath(Long categoryId);

}
