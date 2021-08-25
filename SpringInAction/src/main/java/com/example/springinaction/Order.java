package com.example.springinaction;

import lombok.Data;

/**
 * @author cyberxgg
 * @version 1.0
 * @date 2021/8/20 1:34 下午
 */

@Data
public class Order {
    /**
     * Taco外送订单信息：直接写一个地址大String也行，
     * 但是这里满足第一范式，每个字段都是不可再拆分的
     */
    private String deliveryName;
    private String street;
    private String city;
    private String state;
    private String zip;

    // @CreditCardNumber(message="not a valid credit card number")
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;
}
