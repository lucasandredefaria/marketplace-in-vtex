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

    @Value("${vtex.endpoint.eansku}")
    private String endpointEanSku;

    @Value("${vtex.endpoint.filesku}")
    private String endpointFileSku;

    @Value("${vtex.endpoint.stocksku}")
    private String endpointStockSku;

    @Value("${vtex.endpoint.pricesku}")
    private String endpointPriceSku;

    @Value("${vtex.endpoint.updatesku}")
    private String endpointUpdateSku;

    @Value("${vtex.endpoint.removefilesku}")
    private String endpointRemoveFileSku;

    @Value("${vtex.endpoint.skurefid}")
    private String endpointSkuRefId;

    @Value("${vtex.endpoint.productrefid}")
    private String endpointFindProduct;

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
    public Response createSku(VtexSkuDTO product) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(product);
        log.info("JSON: " + json);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpointSku)
                .post(RequestBody.create(JSON, json))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response createEanSku(String ean, Long idVtex) throws IOException {

        String endpoint = endpointEanSku.replace("{skuId}", String.valueOf(idVtex))
                                        .replace("{ean}", ean);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpoint)
                .post(RequestBody.create(null, new byte[0]))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response createFileSku(VtexFileSkuDTO vtexFileSkuDTO, Long idVtex) throws IOException {

        Gson gson = new Gson();
        String json = gson.toJson(vtexFileSkuDTO);
        log.info("JSON: " + json);

        String endpoint = endpointFileSku.replace("{skuId}", String.valueOf(idVtex));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpoint)
                .post(RequestBody.create(JSON, json))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();

    }

    @Override
    public Response sendStockSku(VtexStockSkuDTO vtexStockSkuDTO, Long idSku) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(vtexStockSkuDTO);
        log.info("JSON: " + json);

        String endpoint = endpointStockSku.replace("{skuId}", String.valueOf(idSku))
                .replace("{warehouseId}", "1");
        //warehouseId Estoque Principal

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpoint)
                .put(RequestBody.create(JSON, json))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response sendPriceSku(VtexPriceSkuDTO vtexpriceSkuDTO, Long idSku) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(vtexpriceSkuDTO);
        log.info("JSON: " + json);

        String endpoint = endpointPriceSku.replace("{itemId}", String.valueOf(idSku));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpoint)
                .put(RequestBody.create(JSON, json))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response getProduct(Long skuSaraiva) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpointFindProduct + skuSaraiva)
                .get()
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response getSku(Long skuId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpointSkuRefId + skuId)
                .get()
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response updateSku(VtexSkuDTO sku, Long IdSku) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(sku);
        log.info("JSON: " + json);

        String endpoint = endpointUpdateSku.replace("{skuId}", String.valueOf(IdSku));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpoint)
                .put(RequestBody.create(JSON, json))
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

    @Override
    public Response removeFileSku(Long idSkuVtex) throws IOException {

        String endpoint = endpointRemoveFileSku.replace("{skuId}", String.valueOf(idSkuVtex));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(endpointBaseURL + endpoint)
                .delete()
                .addHeader(ACCEPT, APP_JSON)
                .addHeader(CONTENT_TYPE, APP_JSON)
                .addHeader(VTEX_API_APP_KEY, endpointKey)
                .addHeader(VTEX_API_APP_TOKEN, endpointToken)
                .build();

        return client.newCall(request).execute();
    }

}
