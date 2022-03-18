package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;

@Data
public class VtexCategoryDTO {

    private Long id;
    private String name;
    private Long categoryId;
    private Long brandId;
    private boolean isActive;
    private boolean isVisible;

}
