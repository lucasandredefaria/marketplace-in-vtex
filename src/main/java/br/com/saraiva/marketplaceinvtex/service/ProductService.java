package br.com.saraiva.marketplaceinvtex.service;

import br.com.saraiva.marketplaceinvtex.database.IProductRepository;
import br.com.saraiva.marketplaceinvtex.database.IResponseErrorVtexRepository;
import br.com.saraiva.marketplaceinvtex.database.IProductResponseVtexRepository;
import br.com.saraiva.marketplaceinvtex.database.ISkuRepository;
import br.com.saraiva.marketplaceinvtex.model.Product;
import br.com.saraiva.marketplaceinvtex.model.SkuInserirRequestMessage;
import br.com.saraiva.marketplaceinvtex.vtex.ResponseProductVtexDTO;
import br.com.saraiva.marketplaceinvtex.vtex.VtexProductDTO;
import br.com.saraiva.marketplaceinvtex.vtex.ResponseErrorVtexDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
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
    private IResponseErrorVtexRepository responseErrorVtexRepository;

    @Autowired
    private ISkuRepository skuRepository;

    @Autowired
    private VtexService vtexService;

    public void processingProduct(Product product){

        saveSku(product.getSkus());
        saveProduct(product);
        sendProductVtex(product);

        log.info("FOI: " + product);
    }



    private void saveSku(SkuInserirRequestMessage sku) {
        log.info("Saving the log in mongo: {}", sku);
        sku.setDateProcessing(Calendar.getInstance().getTime());
        skuRepository.insert(sku);
        log.info("Log saved: {}", sku);
    }

    private void saveProduct(Product product) {
        log.info("Saving the log in mongo: {}", product);
        product.setDateProcessing(Calendar.getInstance().getTime());
        productRepository.insert(product);
        log.info("Log saved: {}", product);
    }

    private void updateProductVtex(Product product){
        log.info("Sent product update for VTEX: {}", product);
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Response response = vtexService.updateProduct(setProductToProductVtexVO(product));

            String body = response.body().string();
            byte[] bytes = body.getBytes();
            String code = String.valueOf(response.code());

            if (response.isSuccessful()){
                try {
                    ResponseProductVtexDTO responseProductVtex = objectMapper.readValue(bytes, ResponseProductVtexDTO.class);
                    responseProductVtex.setOperation(UPDATE);
                    productVtexRepository.insert(responseProductVtex);
                    log.info("Update uroduct sent to VTEX successfully. ResponseCode: {} - Message: {}", code, body);

                } catch (Exception e){
                    log.error(e.getMessage());
                }
            } else {
                log.info("Failed to send update product to VTEX. ResponseCode: {} - Message: {}", code, body);
                ResponseErrorVtexDTO vtexProductResponseErrorDTO = new ResponseErrorVtexDTO();
                vtexProductResponseErrorDTO.setCodeResponse(code);
                vtexProductResponseErrorDTO.setMessage(body);
                vtexProductResponseErrorDTO.setIdLojista(product.getIdLojista());
                vtexProductResponseErrorDTO.setSkuSaraiva(product.getSkus().getSkuSaraiva());
                vtexProductResponseErrorDTO.setSkuLojista(product.getSkus().getSkuLojista());
                vtexProductResponseErrorDTO.setEan(product.getSkus().getEan());
                vtexProductResponseErrorDTO.setIsbn(product.getSkus().getIsbn());
                vtexProductResponseErrorDTO.setDateProcessing(Calendar.getInstance().getTime());
                responseErrorVtexRepository.insert(vtexProductResponseErrorDTO);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }

    }

    private void sendProductVtex(Product product) {
        log.info("Sent new product for VTEX: {}", product);
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Response response = vtexService.createProduct(setProductToProductVtexVO(product));

            String body = response.body().string();
            byte[] bytes = body.getBytes();
            String code = String.valueOf(response.code());

            if (response.isSuccessful()){
                try {
                    ResponseProductVtexDTO responseProductVtex = objectMapper.readValue(bytes, ResponseProductVtexDTO.class);
                    responseProductVtex.setOperation(CREATE);
                    productVtexRepository.insert(responseProductVtex);

                    updateIdVtexMongo(product, responseProductVtex.getIdVtex());

                    log.info("Product sent to VTEX successfully. ResponseCode: {} - Message: {}", code, body);

                } catch (Exception e){
                    log.error(e.getMessage());
                }
            } else {
                log.info("Failed to send product to VTEX. ResponseCode: {} - Message: {}", code, body);
                ResponseErrorVtexDTO vtexProductResponseErrorDTO = new ResponseErrorVtexDTO();
                vtexProductResponseErrorDTO.setCodeResponse(code);
                vtexProductResponseErrorDTO.setMessage(body);
                vtexProductResponseErrorDTO.setIdLojista(product.getIdLojista());
                vtexProductResponseErrorDTO.setSkuSaraiva(product.getSkus().getSkuSaraiva());
                vtexProductResponseErrorDTO.setSkuLojista(product.getSkus().getSkuLojista());
                vtexProductResponseErrorDTO.setEan(product.getSkus().getEan());
                vtexProductResponseErrorDTO.setIsbn(product.getSkus().getIsbn());
                vtexProductResponseErrorDTO.setDateProcessing(Calendar.getInstance().getTime());
                responseErrorVtexRepository.insert(vtexProductResponseErrorDTO);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private VtexProductDTO setProductToProductVtexVO(Product product) {

        String refId = product.getSkus().getSkuSaraiva() == null ?
                "S" + Long.toString(getRandomNumberUsingLong()) : product.getSkus().getSkuSaraiva();

        VtexProductDTO vtexProductDTO = new VtexProductDTO();
        vtexProductDTO.setName(product.getNome());
        vtexProductDTO.setDepartmentId(product.getIdDepartamento());
        vtexProductDTO.setCategoryId(product.getIdCategoriaSaraiva());
        vtexProductDTO.setBrandId(product.getIdMarcaSaraiva());
        vtexProductDTO.setLinkId(createLinkId(product.getNome(), refId));
        vtexProductDTO.setVisible(isVisible(product.getSkus()));
        vtexProductDTO.setDescription(product.getDescricao());
        vtexProductDTO.setDescriptionShort(product.getDescricao());
        vtexProductDTO.setReleaseDate(product.getSkus().getReleaseDate());
        vtexProductDTO.setRefId(refId);
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

    private void updateIdVtexMongo(Product product, Integer id){

//        Product p = productRepository.findBySkuLojistaAndIsbn(
//                product.getSkus().getSkuLojista(), product.getSkus().getIsbn());
//
//        p.setId(String.valueOf(id));
//        productRepository.save(p);
    }
}
