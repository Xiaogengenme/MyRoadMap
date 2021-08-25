package com.example.springinaction;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cyberxgg
 * @version 1.0
 * @date 2021/8/20 1:20 下午
 */
@Data
public class Taco {
    /**
     * 需要调整我们的领域对象来适应数据库开发
     */
    private long id;    // 一条Taco记录存入数据库时最好有一条字段作为对象的唯一标识
    private Date createAt;  // 增加一个字段作为Taco类创建的日期和时间

    @NotNull
//    @Size(min=5, message="name must have at least 5 characters")
    private String name;

    private List<Ingredient> ingredients;   // 一个Taco用到的原料

}
