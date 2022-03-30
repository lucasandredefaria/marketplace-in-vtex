package br.com.saraiva.marketplaceinvtex.service;

import br.com.saraiva.marketplaceinvtex.database.IProductRepository;
import br.com.saraiva.marketplaceinvtex.database.IProductResponseVtexRepository;
import br.com.saraiva.marketplaceinvtex.database.ISkuRepository;
import br.com.saraiva.marketplaceinvtex.model.Product;
import br.com.saraiva.marketplaceinvtex.model.SkuInserirRequestMessage;
import br.com.saraiva.marketplaceinvtex.vtex.ProductVtex;
import br.com.saraiva.marketplaceinvtex.vtex.VtexProductDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

@Slf4j
@Service
public class ProductService {

    private static String CREATE = "create";
    private static String UPDATE = "update";

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductResponseVtexRepository productVtexRepository;

    @Autowired
    private ISkuRepository skuRepository;

    @Autowired
    private VtexService vtexService;

    @Autowired
    private SkuService skuService;

    public void processingProductAndSku(Product p){

        skuService.saveSku(p.getSkus());
        Product product = saveProduct(p);
        if (!productExistVtex(product)){
            log.info("Product does not exist on VTEX {} - {}", product.getRefId(), product.getNome());
            sendProductVtex(product);

            //Processa e envia o Sku para VTEX
            skuService.processingSku(product);

        } else {
            log.info("Product exist on VTEX {} - {}", product.getRefId(), product.getNome());

            //TODO criar processo de atualização de product na VTEX

            //Processa e envia o Sku para VTEX
            skuService.processingSku(product);
        }

    }

    private Product saveProduct(Product product) {
        log.info("Saving the log in mongo: {}", product);

        Product pdt = new Product();
        
        try {
            String param = product.getRefId() == null ?
                    product.getSkus().getSkuSaraiva() : String.valueOf(product.getRefId());

            Product p = productRepository.findByRefId(Long.valueOf(param));

            if (p != null && !"".equals(p)){
                Product productValues = setProductToProductMongo(product, p);
                productValues.setDateProcessing(Calendar.getInstance().getTime());
                pdt = productRepository.save(productValues);
                log.info("Log saved: {}", productValues.getNome());

            } else {
                product.setDateProcessing(Calendar.getInstance().getTime());

                //Setar refId para uma possivel consulta na vtex posteriormente
                String refId = product.getSkus().getSkuSaraiva() == null ?
                        "S" + Long.toString(getRandomNumberUsingLong()) : product.getSkus().getSkuSaraiva();

                product.setRefId(Long.valueOf(refId));
                pdt = productRepository.insert(product);
                log.info("Log saved: {}", product);
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
        return pdt;
    }

    private boolean productExistVtex(Product product){
        Response response = null;
        try {
            response = vtexService.getProduct(product.getRefId());
            String code = String.valueOf(response.code());
            String body = response.body().string();
            if (code.equals("200") && !body.equals("null")){
                JSONObject object = new JSONObject(body);
                product.setIdVtex(Long.valueOf(object.getString("Id")));

                updateIdVtexMongo(product, body);

                return true;
            }
        } catch (Exception e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
        return false;
    }

    private void sendProductVtex(Product product) {
        log.info("Sent new product for VTEX: {}", product);
        try {
            Product p = productRepository.findBy_id(product.get_id());

            Response response = vtexService.createProduct(setProductToProductVtexVO(p));

            String body = response.body().string();
            String code = String.valueOf(response.code());

            try {
                ProductVtex productVtex = new ProductVtex();
                productVtex.setCodeResponse(code);
                productVtex.setMessage(body);
                productVtex.setDateProcessing(Calendar.getInstance().getTime());
                productVtex.setOperation(CREATE);
                productVtexRepository.insert(productVtex);
            } catch (Exception e){
                log.error(e.getMessage());
            }

            if (response.isSuccessful()){
                log.info("Product sent to VTEX successfully. ResponseCode: {} - Message: {}", code, body);
                updateIdVtexMongo(product, body);
            } else {
                log.info("Failed to send product to VTEX. ResponseCode: {} - Message: {}", code, body);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private Product setProductToProductMongo(Product currentProduct, Product productMongo) {
        productMongo.setNome(currentProduct.getNome());
        productMongo.setIdCategoriaSaraiva(currentProduct.getIdCategoriaSaraiva());
        productMongo.setIdMarcaSaraiva(currentProduct.getIdMarcaSaraiva());
        productMongo.setDescricao(currentProduct.getDescricao());
        return productMongo;
    }

    private VtexProductDTO setProductToProductVtexVO(Product product) {

        VtexProductDTO vtexProductDTO = new VtexProductDTO();
        vtexProductDTO.setName(product.getNome());
        vtexProductDTO.setCategoryId(product.getIdCategoriaSaraiva());
        vtexProductDTO.setBrandId(product.getIdMarcaSaraiva());
        vtexProductDTO.setLinkId(createLinkId(product.getNome(), String.valueOf(product.getRefId())));
        vtexProductDTO.setVisible(isVisible(product.getSkus()));
        vtexProductDTO.setDescription(product.getDescricao());
        vtexProductDTO.setDescriptionShort(product.getDescricao());
        vtexProductDTO.setReleaseDate(product.getSkus().getReleaseDate());
        vtexProductDTO.setRefId(String.valueOf(product.getRefId()));
        vtexProductDTO.setKeyWords("");
        vtexProductDTO.setTitle(product.getSkus().getTitle());
        vtexProductDTO.setActive(isActive(product.getSkus()));
        vtexProductDTO.setTaxCode("");
        vtexProductDTO.setMetaTagDescription("");
        vtexProductDTO.setSupplierId(1);
        vtexProductDTO.setShowWithoutStock(false);
        vtexProductDTO.setAdWordsRemarketingCode(null);
        vtexProductDTO.setLomadeeCampaignCode(null);
        vtexProductDTO.setScore(1);
        return vtexProductDTO;
    }

    private boolean isVisible(SkuInserirRequestMessage sku){
        if (sku.getStatus().toLowerCase() == "ATIVO") {
            return true;
        }
        return false;
    }

    private boolean isActive(SkuInserirRequestMessage sku){
        if (sku.getStatus().toLowerCase() == "ATIVO") {
            return true;
        }
        return false;
    }

    private Long getRandomNumberUsingLong() {
        Random random = new Random();
        return random.longs(1000, 1000000000)
                .findFirst()
                .getAsLong();
    }

    private String createLinkId(String name, String refId){
        String result = name.replaceAll("\\s+","-");
        return result+"-"+refId;
    }

    private void updateIdVtexMongo(Product product, String body) {
        try {
            JSONObject object = new JSONObject(body);
            String id = object.getString("Id");
            String refId = object.getString("RefId");
            log.info("ID created in VTEX: " + id);

            Product p = productRepository.findBy_id(product.get_id());
            p.setIdVtex(Long.valueOf(id));
            p.setRefId(Long.valueOf(refId));
            productRepository.save(p);
            log.info("Updated product. {} - {}", id,  p.getNome());
        } catch (JSONException e) {
            log.info("Product update failure at Saraiva. " + e.getMessage());
        }
    }
}
