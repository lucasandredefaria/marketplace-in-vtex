package br.com.saraiva.marketplaceinvtex.vtex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class VtexSuggestionResultDTO {

    @JsonIgnore
    private Long sellerId;

    @JsonIgnore
    private String refId;

    private String latestVersionId;
    private String status;
    private List<SuggestionMatch> matches;

    private static String STATUS_PENDING = "Pending";
    private static String STATUS_ACCEPTED = "Accepted";

}

@Data
@ToString
class SuggestionMatch {
    private String matchId;
    private String matcherId;
    private BigDecimal score;
    private String SkuRef;
    private MatchProduct product;
    private MatchSku sku;
}

@Data
class MatchProduct {
    private String name;
    private String description;
    private Long categoryId;
    private Long BrandId;
    private MatchProductEspecification Specifications;
}

class MatchProductEspecification{

}

@Data
class MatchSku {
    private String name;
    private Long ProductId;
    private List<String> eans;
    private String refId;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal weight;
    private BigDecimal length;
    private int unitMultiplier;
    private String measurementUnit;
    private String images;
}