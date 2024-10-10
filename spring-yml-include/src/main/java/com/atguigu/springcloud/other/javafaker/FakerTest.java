package com.atguigu.springcloud.other.javafaker;

import com.github.javafaker.Faker;

import java.util.Locale;

public class FakerTest {

    public static void main(String[] args) {
        Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);

        System.out.println(faker.name().fullName());
        System.out.println(faker.internet().emailAddress("cctv"));
        System.out.println(faker.phoneNumber().phoneNumber());
        System.out.println(faker.date().birthday());
        System.out.println(faker.address().fullAddress());
        System.out.println(faker.random().nextInt(10, 1000));
    }

}
