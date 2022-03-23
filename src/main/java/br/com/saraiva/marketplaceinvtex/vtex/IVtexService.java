package br.com.saraiva.marketplaceinvtex.vtex;

import okhttp3.Response;

import java.io.IOException;

public interface IVtexService {

    Response createProduct(VtexProductDTO product) throws IOException;
    VtexMatchResultDTO forceMatch(VtexSuggestionResultDTO suggestionResult);
    VtexSuggestionResultDTO getSuggestion(Long lojistaId, String refId);
    boolean createSuggestion(VtexProductSuggestionDTO suggestion, Long lojistaId, String skuMarketplace);
    boolean removeSeller(VtexSkuDTO sku, String sellerId);
    VtexProductDTO getProduct(Long productId);
    Response updateProduct(VtexProductDTO product) throws IOException;
    Long getSkuByRef(String skuRef);
    VtexSkuDTO getSku(Long skuId);
    boolean deleteEanFromSKU(VtexSkuDTO sku);
    boolean updateSku(VtexSkuDTO sku);
    VtexBrandDTO getBrand(Long brandId);
    VtexCategoryDTO getCategory(Long categoryId);
    String getCategoryFullPath(Long categoryId);

}
