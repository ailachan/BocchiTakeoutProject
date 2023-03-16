package com.bocchi;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {

    @TableId
    private Long id;
    private String name;
    private String phone;
    private String sex;
    private String id_number;
    private String avatar;
    private Integer status;
};