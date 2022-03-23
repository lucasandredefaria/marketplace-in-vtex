package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@ToString
@Document(collection = "responseProductVtex")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseProductVtexDTO implements Serializable {

    @JsonProperty("Id")
    private Integer idVtex;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("DepartmentId")
    private Integer departmentId;

    @JsonProperty("CategoryId")
    private Integer categoryId;

    @JsonProperty("BrandId")
    private Integer brandId;

    @JsonProperty("LinkId")
    private String linkId;

    @JsonProperty("RefId")
    private String refId;

    @JsonProperty("IsVisible")
    private boolean isVisible;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("DescriptionShort")
    private String descriptionShort;

    @JsonProperty("ReleaseDate")
    private String releaseDate;

    @JsonProperty("KeyWords")
    private String keyWords;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("IsActive")
    private boolean isActive;

    @JsonProperty("TaxCode")
    private String taxCode;

    @JsonProperty("MetaTagDescription")
    private String metaTagDescription;

    @JsonProperty("SupplierId")
    private Integer supplierId;

    @JsonProperty("ShowWithoutStock")
    private boolean showWithoutStock;

    @JsonProperty("AdWordsRemarketingCode")
    private String adWordsRemarketingCode;

    @JsonProperty("LomadeeCampaignCode")
    private String lomadeeCampaignCode;

    @JsonProperty("Score")
    private Integer score;

    private String operation;

}
