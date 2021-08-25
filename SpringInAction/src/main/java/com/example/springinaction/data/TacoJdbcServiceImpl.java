package com.example.springinaction.data;

import com.example.springinaction.Ingredient;
import com.example.springinaction.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.Arrays;
import java.util.Date;

/**
 * @author cyberxgg
 * @version 1.0
 * @date 2021/8/20 3:43 下午
 */
public class TacoJdbcServiceImpl implements TacoJdbcService{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        // 对于taco中所有ingredient存进Taco_Ingredient表
        for (Ingredient ingredient : taco.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }
        return taco;
    }

    @Override
    public long saveTacoInfo(Taco taco) {
        taco.setCreateAt(new Date());
        PreparedStatementCreator preparedStatementCreator =
                new PreparedStatementCreatorFactory(
                        "insert into Taco (name, createAt) values (?,?)",
                        Types.VARCHAR, Types.TIMESTAMP
                ).newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                new Timestamp(taco.getCreateAt().getTime())
                        )
                );
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbcTemplate.update(
                "insert into Taco_Ingredients(taco, ingredient)" +
                        "values(?, ?)",
                tacoId, ingredient.getId()
        );
    }
}
