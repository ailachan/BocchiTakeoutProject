package com.bocchi.pojo;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;


/**
 * 数据传输对象 DTO
 * 网络中传输的某一属性含有多个取值,这是就可以用dto来存储,不需要每个取值单独new一个实体对象一个个传
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();//dto对象,用于用这一个dto对象在网络中存储传输接收多个值

    private String categoryName;

    private Integer copies;
}
