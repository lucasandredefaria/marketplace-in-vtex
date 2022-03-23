package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VtexProductDTO {

    private Long id;
    private String name;
    private Long categoryId;
    private Long departmentId;
    private Long brandId;
    private boolean isActive;
    private boolean isVisible;
    private String linkId;
    private String refId;
    private String description;
    private String descriptionShort;
    private String releaseDate;
    private String keyWords;
    private String title;
    private String taxCode;
    private String metaTagDescription;
    private Integer supplierId;
    private boolean showWithoutStock;
    private String adWordsRemarketingCode;
    private String lomadeeCampaignCode;
    private Integer score;

}
