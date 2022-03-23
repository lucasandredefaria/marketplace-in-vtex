package br.com.saraiva.marketplaceinvtex.service;

import br.com.saraiva.marketplaceinvtex.vtex.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class VtexService implements IVtexService {

    @Value("${vtex.endpoint.product}")
    private String endpointProduct;

    @Value("${vtex.endpoint.sku}")
    private String endpointSku;

    @Value("${vtex.endpoint.updatesku}")
    private String endpointUpdateSku;

    @Value("${vtex.endpoint.category}")
    private String endpointCategory;

    @Value("${vtex.endpoint.brand}")
    private String endpointBrand;

    @Value("${vtex.endpoint.removematch}")
    private String endpointRemoveMatch;

    @Value("${vtex.endpoint.forcematch}")
    private String endpointForceMatch;

    @Value("${vtex.endpoint.suggestion}")
    private String endpointSuggestion;

    @Value("${vtex.endpoint.deleteeanfromsku}")
    private String endpointDeleteEanFromSku;

    @Value("${vtex.endpoint.skurefid}")
    private String endpointSkuRefId;

    @Value("${vtex.api.baseurl}")
    private String endpointBaseURL;

    @Value("${vtex.api.token}")
    private String endpointToken;

    @Value("${vtex.api.key}")
    private String endpointKey;

    private static String ACCEPT = "Accept";
    private static String APP_JSON = "application/json";
    private static String CONTENT_TYPE = "Content-Type";
    private static String VTEX_API_APP_KEY = "X-VTEX-API-AppKey";
    private static String VTEX_API_APP_TOKEN = "X-VTEX-API-AppToken";
    public static MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @Override
    public Response createProduct(VtexProductDTO product) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(product);
        log.info("JSON: " + json);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpointProduct)
                .post(RequestBody.create(JSON, json))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response updateProduct(VtexProductDTO product) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(product);
        log.info("JSON: " + json);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpointProduct + "/" + product)
                .post(RequestBody.create(JSON, json))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public VtexMatchResultDTO forceMatch(VtexSuggestionResultDTO suggestionResult) {
        return null;
    }

    @Override
    public VtexSuggestionResultDTO getSuggestion(Long lojistaId, String refId) {
        return null;
    }

    @Override
    public boolean createSuggestion(VtexProductSuggestionDTO suggestion, Long lojistaId, String skuMarketplace) {
        return false;
    }

    @Override
    public boolean removeSeller(VtexSkuDTO sku, String sellerId) {
        return false;
    }

    @Override
    public VtexProductDTO getProduct(Long productId) {
        return null;
    }

    @Override
    public Long getSkuByRef(String skuRef) {
        return null;
    }

    @Override
    public VtexSkuDTO getSku(Long skuId) {
        return null;
    }

    @Override
    public boolean deleteEanFromSKU(VtexSkuDTO sku) {
        return false;
    }

    @Override
    public boolean updateSku(VtexSkuDTO sku) {
        return false;
    }

    @Override
    public VtexBrandDTO getBrand(Long brandId) {
        return null;
    }

    @Override
    public VtexCategoryDTO getCategory(Long categoryId) {
        return null;
    }

    @Override
    public String getCategoryFullPath(Long categoryId) {
        return null;
    }
}
