package com.example.springinaction.data;

import com.example.springinaction.Ingredient;

import java.util.List;

/**
 * @author cyberxgg
 * @version 1.0
 * @date 2021/8/20 3:36 下午
 */
public interface IngredientJdbcService extends JdbcService{
    // 找所有行数据
    List<Ingredient> findAll();
    // 通过id查找一行数据
    Ingredient findOne(String id);
    // 存入一行数据
    Ingredient save(Ingredient ingredient);
}
