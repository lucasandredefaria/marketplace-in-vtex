package br.com.saraiva.marketplaceinvtex.service;

import br.com.saraiva.marketplaceinvtex.database.*;
import br.com.saraiva.marketplaceinvtex.model.CaracteristicaSku;
import br.com.saraiva.marketplaceinvtex.model.Image;
import br.com.saraiva.marketplaceinvtex.model.Product;
import br.com.saraiva.marketplaceinvtex.model.SkuInserirRequestMessage;
import br.com.saraiva.marketplaceinvtex.vtex.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SkuService {

    private static String CREATE = "create";
    private static String UPDATE = "update";

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ISkuRepository skuRepository;

    @Autowired
    private ISkuResponseVtexRepository skuVtexRepository;

    @Autowired
    private ISkuEanResponseVtexRepository skuEanVtexRepository;

    @Autowired
    private ISkuFileResponseVtexRepository skuFileVtexRepository;

    @Autowired
    private ISkuStockResponseVtexRepository skuStockVtexRepository;

    @Autowired
    private ISkuPriceResponseVtexRepository skuPriceVtexRepository;

    @Autowired
    private VtexService vtexService;

    public void processingSku(Product product){

        if (!skuExistVtex(product)){
            log.info("SKU does not exist in VTEX, creating a new SKU: {}", product.getNome());

            createSkuVtex(product);
            sendSkuEanVtex(product);
            sendSkuStockVtex(product.getSkus().get_id());
            sendSkuPriceVtex(product.getSkus().get_id());

            product.getSkus().getImagens().forEach(image -> {

                //TODO Baixar as imagens para o FTP Saraiva e fazer as validações

                sendSkuFilesVtex(image, product.getSkus().get_id());
            });

            //Enviar a ativação do SKU
            updateSkuVtex(product);

        } else {
            log.info("SKU exists on VTEX, updating the SKU: {}", product.getNome());

            updateSkuVtex(product);
            sendSkuEanVtex(product);
            sendSkuStockVtex(product.getSkus().get_id());
            sendSkuPriceVtex(product.getSkus().get_id());

            //TODO Deletar as imagens existentes no FTP Saraiva

            removeSkuFilesVtex(product.getSkus().get_id());

            product.getSkus().getImagens().forEach(image -> {
                sendSkuFilesVtex(image, product.getSkus().get_id());
            });
        }

    }

    protected void saveSku(SkuInserirRequestMessage sku) {
        log.info("Saving the log in mongo: {}", sku);

        SkuInserirRequestMessage resultMongo =
                skuRepository.findBySkuSaraivaAndIdLojista(sku.getSkuSaraiva(), sku.getIdLojista());

        try {
            if (resultMongo != null && !"".equals(resultMongo)){
                SkuInserirRequestMessage s = setSkuToSkuMongo(sku, resultMongo);
                skuRepository.save(s);
                log.info("Log saved: {}", resultMongo);

            } else {
                int count = 0;
                for (Image image : sku.getImagens()) {
                    count = count + 1;
                    image.setName("image" + count);
                }
                sku.setDateProcessing(Calendar.getInstance().getTime());
                skuRepository.insert(sku);
                log.info("Log saved: {}", sku);
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }

    }

    private boolean skuExistVtex(Product product){
        Response response = null;
        try {
            response = vtexService.getSku(product.getRefId());

            String code = String.valueOf(response.code());
            String body = response.body().string();
            if (code.equals("200") && !body.equals("null")){
                JSONObject object = new JSONObject(body);
                product.getSkus().setIdVtex(Long.valueOf(object.getString("Id")));

                updateIdVtexMongo(product, body);

                return true;
            }

        } catch (Exception e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
        return false;
    }

    private void createSkuVtex(Product product) {
        log.info("Sent new sku for VTEX: {}", product);
        try {
            Product p = productRepository.findBy_id(product.get_id());

            Response response = vtexService.createSku(setSkuToSkuVtexVO(p, p.getSkus(), false));

            String body = response.body().string();
            String code = String.valueOf(response.code());

            try {
                SkuVtex skuVtex = new SkuVtex();
                skuVtex.setCodeResponse(code);
                skuVtex.setMessage(body);
                skuVtex.setDateProcessing(Calendar.getInstance().getTime());
                skuVtex.setOperation(CREATE);
                skuVtexRepository.insert(skuVtex);
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

    private void sendSkuEanVtex(Product product){
        log.info("Send SKU EAN to VTEX: {}", product);
        try {
            SkuInserirRequestMessage sku =
                    skuRepository.findBy_id(String.valueOf(product.getSkus().get_id()));

            Response response = vtexService.createEanSku(sku.getEan(), sku.getIdVtex());

            String code = String.valueOf(response.code());

            try {
                EanSkuVtex eanSkuVtex = new EanSkuVtex();
                eanSkuVtex.setCodeResponse(code);
                eanSkuVtex.setEan(sku.getEan());
                eanSkuVtex.setIdSkuVtex(String.valueOf(sku.getIdVtex()));
                eanSkuVtex.setDateProcessing(Calendar.getInstance().getTime());
                eanSkuVtex.setOperation(CREATE);
                skuEanVtexRepository.insert(eanSkuVtex);
            } catch (Exception e){
                log.error(e.getMessage());
            }

            if (code.equals("200")){
                log.info("EAN linked to SKU on VTEX successfully");
            } else {
                log.info("Failed to link EAN to SKU in VTEX");
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private void removeSkuFilesVtex(ObjectId idSkuMongo){
        log.info("Remove FILEs SKU to VTEX: {}", idSkuMongo);
        try {

            SkuInserirRequestMessage sku =
                    skuRepository.findBy_id(String.valueOf(idSkuMongo));

            Response response = vtexService.removeFileSku(sku.getIdVtex());

            String body = response.body().string();
            String code = String.valueOf(response.code());

            try {
                SkuFileVtex skuFileVtex = new SkuFileVtex();
                skuFileVtex.setCodeResponse(code);
                skuFileVtex.setMessage(body);
                skuFileVtex.setDateProcessing(Calendar.getInstance().getTime());
                skuFileVtex.setOperation(UPDATE);
                skuFileVtexRepository.insert(skuFileVtex);
            } catch (Exception e){
                log.error(e.getMessage());
            }

            if (code.equals("200")){
                log.info("Files product removed from VTEX successfully. ResponseCode: {} - Message: {}", code, body);

            } else {
                log.info("Failed to remove files product to VTEX. ResponseCode: {} - Message: {}", code, body);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private void sendSkuFilesVtex(Image image, ObjectId idSkuMongo){
        log.info("Send FILE SKU to VTEX: {}", idSkuMongo);
        try {

            SkuInserirRequestMessage sku =
                    skuRepository.findBy_id(String.valueOf(idSkuMongo));


            Response response = vtexService.createFileSku(setFileSkuToFileVtexVO(image), sku.getIdVtex());

            String body = response.body().string();
            String code = String.valueOf(response.code());

            try {
                SkuFileVtex skuFileVtex = new SkuFileVtex();
                skuFileVtex.setCodeResponse(code);
                skuFileVtex.setMessage(body);
                skuFileVtex.setDateProcessing(Calendar.getInstance().getTime());
                skuFileVtex.setOperation(CREATE);
                skuFileVtexRepository.insert(skuFileVtex);
            } catch (Exception e){
                log.error(e.getMessage());
            }

            if (response.isSuccessful()){
                log.info("Product sent to VTEX successfully. ResponseCode: {} - Message: {}", code, body);

            } else {
                log.info("Failed to send product to VTEX. ResponseCode: {} - Message: {}", code, body);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private void sendSkuStockVtex(ObjectId idSkuMongo){
        log.info("Send Stock SKU to VTEX: {}", idSkuMongo);
        try {

            SkuInserirRequestMessage sku =
                    skuRepository.findBy_id(String.valueOf(idSkuMongo));

            Response response = vtexService.sendStockSku(
                    setStockSkuToFileVtexVO(sku.getEstoque()), sku.getIdVtex());

            String body = response.body().string();
            String code = String.valueOf(response.code());

            try {
                SkuStockVtex skuStockVtex = new SkuStockVtex();
                skuStockVtex.setCodeResponse(code);
                skuStockVtex.setMessage(body);
                skuStockVtex.setDateProcessing(Calendar.getInstance().getTime());
                skuStockVtex.setOperation(CREATE);
                skuStockVtexRepository.insert(skuStockVtex);
            } catch (Exception e){
                log.error(e.getMessage());
            }

            if (code.equals("200")){
                log.info("Stock product sent to VTEX successfully. ResponseCode: {} - Message: {}", code, body);

            } else {
                log.info("Failed to send stock product to VTEX. ResponseCode: {} - Message: {}", code, body);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private void sendSkuPriceVtex(ObjectId idSkuMongo){
        log.info("Send price SKU to VTEX: {}", idSkuMongo);
        try {

            SkuInserirRequestMessage sku =
                    skuRepository.findBy_id(String.valueOf(idSkuMongo));

            Response response = vtexService.sendPriceSku(
                    setPriceSkuToFileVtexVO(sku.getPrecoDe(),sku.getPrecoPor()), sku.getIdVtex());

            String body = response.body().string();
            String code = String.valueOf(response.code());

            try {
                SkuPriceVtex skuPriceVtex = new SkuPriceVtex();
                skuPriceVtex.setCodeResponse(code);
                skuPriceVtex.setMessage(body);
                skuPriceVtex.setDateProcessing(Calendar.getInstance().getTime());
                skuPriceVtex.setOperation(CREATE);
                skuPriceVtexRepository.insert(skuPriceVtex);
            } catch (Exception e){
                log.error(e.getMessage());
            }

            if (code.equals("200")){
                log.info("Price product sent to VTEX successfully. ResponseCode: {} - Message: {}", code, body);

            } else {
                log.info("Failed to send price product to VTEX. ResponseCode: {} - Message: {}", code, body);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private void updateSkuVtex(Product product) {
        log.info("Sent a update sku for VTEX: {}", product.getNome());
        try {

            Product p = productRepository.findBy_id(product.get_id());

            SkuInserirRequestMessage sku =
                    skuRepository.findBy_id(String.valueOf(p.getSkus().get_id()));

            Response response = vtexService.updateSku(
                    setSkuToSkuVtexVO(p, sku, isActive(sku)), sku.getIdVtex());

            String body = response.body().string();
            String code = String.valueOf(response.code());

            try {
                SkuVtex skuVtex = new SkuVtex();
                skuVtex.setCodeResponse(code);
                skuVtex.setMessage(body);
                skuVtex.setDateProcessing(Calendar.getInstance().getTime());
                skuVtex.setOperation(UPDATE);
                skuVtexRepository.insert(skuVtex);
            } catch (Exception e){
                log.error(e.getMessage());
            }

            if (code.equals("200")){
                log.info("Update product sent to VTEX successfully. ResponseCode: {} - Message: {}", code, body);
            } else {
                log.info("Failed to send update product to VTEX. ResponseCode: {} - Message: {}", code, body);
            }

        } catch (IOException e) {
            log.error("Failed to make the call to VTEX: " + e.getMessage());
        }
    }

    private void updateIdVtexMongo(Product product, String body) {
        try {
            JSONObject object = new JSONObject(body);
            String id = object.getString("Id");
            String refId = object.getString("RefId");
            log.info("Sku ID created  in VTEX: " + id);

            SkuInserirRequestMessage sku =
                        skuRepository.findBy_id(String.valueOf(product.getSkus().get_id()));

            sku.setIdVtex(Long.valueOf(id));
            skuRepository.save(sku);
            log.info("Updated sku product. {} - {}", id,  sku.getTitle());
        } catch (JSONException e) {
            log.info("Sku product update failure at Saraiva. " + e.getMessage());
        }
    }

    private boolean isActive(SkuInserirRequestMessage sku){
        if (sku.getStatus().toUpperCase().equals("ATIVO")) {
            return true;
        }
        return false;
    }

    private VtexSkuDTO setSkuToSkuVtexVO(Product product, SkuInserirRequestMessage sku, boolean isActive) {

        VtexSkuDTO vtexSkuDTO = new VtexSkuDTO();
        vtexSkuDTO.setId(sku.getIdVtex());
        vtexSkuDTO.setProductId(product.getIdVtex());
        vtexSkuDTO.setActive(isActive);
        vtexSkuDTO.setName(product.getNome());
        vtexSkuDTO.setRefId(String.valueOf(product.getRefId()));
        vtexSkuDTO.setPackagedHeight(sku.getAltura());
        vtexSkuDTO.setPackagedLength(sku.getCumprimento());
        vtexSkuDTO.setPackagedWidth(sku.getLargura());
        vtexSkuDTO.setPackagedWeightKg(sku.getPeso());
        vtexSkuDTO.setKit(false);
        vtexSkuDTO.setManufacturerCode(String.valueOf(sku.getIdLojista()));
        vtexSkuDTO.setCommercialConditionId(1);
        vtexSkuDTO.setMeasurementUnit("un");
        vtexSkuDTO.setUnitMultiplier(BigDecimal.valueOf(1));
        vtexSkuDTO.setKitItensSellApart(false);
        return vtexSkuDTO;

    }

    private VtexFileSkuDTO setFileSkuToFileVtexVO(Image image) {
        VtexFileSkuDTO vtexFileSkuDTO = new VtexFileSkuDTO();
        vtexFileSkuDTO.setMain(image.isCapa());
        vtexFileSkuDTO.setName(image.getName());
        vtexFileSkuDTO.setLabel("");
        vtexFileSkuDTO.setText("");
        vtexFileSkuDTO.setUrl(image.getUrlInterna());
        return vtexFileSkuDTO;
    }

    private VtexStockSkuDTO setStockSkuToFileVtexVO(int qtde) {
        VtexStockSkuDTO vtexStockSkuDTO = new VtexStockSkuDTO();
        vtexStockSkuDTO.setUnlimitedQuantity(false);
        vtexStockSkuDTO.setDateUtcOnBalanceSystem(null);
        vtexStockSkuDTO.setQuantity(qtde);
        return vtexStockSkuDTO;
    }

    private VtexPriceSkuDTO setPriceSkuToFileVtexVO(BigDecimal basePrice, BigDecimal costPrice) {
        VtexPriceSkuDTO vtexPriceSkuDTO = new VtexPriceSkuDTO();
        vtexPriceSkuDTO.setListPrice(null);
        vtexPriceSkuDTO.setCostPrice(costPrice);
        vtexPriceSkuDTO.setBasePrice(basePrice);
        vtexPriceSkuDTO.setMarkup(null);
        return vtexPriceSkuDTO;
    }

    private SkuInserirRequestMessage setSkuToSkuMongo(SkuInserirRequestMessage currentSku, SkuInserirRequestMessage skuMongo) {
        skuMongo.setSkuLojista(currentSku.getSkuLojista());
        skuMongo.setSkuSaraiva(currentSku.getSkuSaraiva());
        skuMongo.setEan(currentSku.getEan());
        skuMongo.setIsbn(currentSku.getIsbn());
        skuMongo.setReleaseDate(currentSku.getReleaseDate());
        skuMongo.setTitle(currentSku.getTitle());
        skuMongo.setPrecoDe(currentSku.getPrecoDe());
        skuMongo.setPrecoPor(currentSku.getPrecoPor());
        skuMongo.setEstoque(currentSku.getEstoque());
        skuMongo.setTipo(currentSku.getTipo());
        skuMongo.setPeso(currentSku.getPeso());
        skuMongo.setAltura(currentSku.getAltura());
        skuMongo.setCumprimento(currentSku.getCumprimento());
        skuMongo.setLargura(currentSku.getLargura());
        skuMongo.setStatus(currentSku.getStatus());

        List<CaracteristicaSku> listCaracteristicaSkuToRemore =  skuMongo.getCaracteristicas();
        List<CaracteristicaSku> listCaracteristicaSkuToAdd =  currentSku.getCaracteristicas();
        skuMongo.getCaracteristicas().removeAll(listCaracteristicaSkuToRemore);
        skuMongo.setCaracteristicas(listCaracteristicaSkuToAdd);

        List<Image> listImageToRemore =  skuMongo.getImagens();
        List<Image> listImageToAdd =  currentSku.getImagens();
        skuMongo.getImagens().removeAll(listImageToRemore);
        skuMongo.setImagens(listImageToAdd);

        int count = 0;
        for (Image image : skuMongo.getImagens()) {
            count = count + 1;
            image.setName("image_" + count);
        }
        skuMongo.setDateProcessing(Calendar.getInstance().getTime());
        return skuMongo;
    }

}
