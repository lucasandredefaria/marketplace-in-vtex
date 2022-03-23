package br.com.saraiva.marketplaceinvtex.vtex;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VtexCategoryDTO {

    private Long id;
    private Long parentId;
    private String name;
    private String title;

}
