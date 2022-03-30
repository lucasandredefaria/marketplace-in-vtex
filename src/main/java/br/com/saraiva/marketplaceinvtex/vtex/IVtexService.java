package br.com.saraiva.marketplaceinvtex.vtex;

import okhttp3.Response;

import java.io.IOException;

public interface IVtexService {

    Response createProduct(VtexProductDTO product) throws IOException;
    Response createSku(VtexSkuDTO product) throws IOException;
    Response createEanSku(String ean, Long idVtex) throws IOException;
    Response createFileSku(VtexFileSkuDTO vtexFileSkuDTO, Long idVtex) throws IOException;
    Response sendStockSku(VtexStockSkuDTO vtexStockSkuDTO, Long idVtex) throws IOException;
    Response sendPriceSku(VtexPriceSkuDTO vtexpriceSkuDTO, Long idSku) throws IOException;
    Response getProduct(Long skuSaraiva) throws IOException;
    Response getSku(Long skuId) throws IOException;
    Response updateSku(VtexSkuDTO sku, Long IdSku) throws IOException;
    Response removeFileSku(Long idSkuVtex) throws IOException;

}
