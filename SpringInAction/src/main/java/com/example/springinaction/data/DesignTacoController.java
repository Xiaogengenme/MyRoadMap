package com.example.springinaction.data;

import com.example.springinaction.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cyberxgg
 * @version 1.0
 * @date 2021/8/20 2:33 下午
 */
public class DesignTacoController {
    @Autowired
    JdbcServiceImpl jdbc;

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> resultAll = jdbc.findAll();

        Ingredient.Type[] types = Ingredient.Type.values();
        for (Ingredient.Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
            filterByType(resultAll, type));
        }
        return "design";
    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
