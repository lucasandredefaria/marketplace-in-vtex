package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class VtexSkuDTO {

    private Long id;
    private Long ProductId;
    private boolean isActive;
    private String name;
    private String refId;
    private BigDecimal packagedHeight;
    private BigDecimal packagedLength;
    private BigDecimal packagedWidth;
    private BigDecimal packagedWeightKg;
    private BigDecimal height;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal weightKg;
    private BigDecimal cubicWeight;
    private boolean isKit;
    private String creationDate;
    private BigDecimal rewardValue;
    private String estimatedDateArrival;
    private String manufacturerCode;
    private Integer commercialConditionId;
    private String measurementUnit;
    private BigDecimal unitMultiplier;
    private String modalType;
    private boolean kitItensSellApart;

}
