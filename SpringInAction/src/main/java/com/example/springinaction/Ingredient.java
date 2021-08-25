package com.example.springinaction;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author cyberxgg
 * @version 1.0
 * @date 2021/8/20 1:32 下午
 */

@Data
@RequiredArgsConstructor
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
