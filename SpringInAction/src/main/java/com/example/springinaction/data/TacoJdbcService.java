package com.example.springinaction.data;

import com.example.springinaction.Ingredient;
import com.example.springinaction.Taco;

/**
 * @author cyberxgg
 * @version 1.0
 * @date 2021/8/20 3:41 下午
 */
public interface TacoJdbcService {
    public Taco save(Taco taco);
    public long saveTacoInfo(Taco taco);
    public void saveIngredientToTaco(Ingredient ingredient, long tacoId);
}
