package com.bigDragon.dataStructure.tree;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author: bigDragon
 * @create: 2022/6/28
 * @Description:
 */

@Data
@NoArgsConstructor
public class Menu {
    private String id;
    private String parentId;
    private String text;
    private String url;
    private String yxbz;
    private List<Menu> children;

    public Menu(String id,String parentId,String text,String url,String yxbz) {
        this.id=id;
        this.parentId=parentId;
        this.text=text;
        this.url=url;
        this.yxbz=yxbz;
    }
}
