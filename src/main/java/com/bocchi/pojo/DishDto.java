package com.bocchi.pojo;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;


/**
 * 数据传输对象 DTO
 * 将某一属性封装成pojo
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();//一个实体类中有另一个实体类作为属性

    private String categoryName;

    private Integer copies;
}
