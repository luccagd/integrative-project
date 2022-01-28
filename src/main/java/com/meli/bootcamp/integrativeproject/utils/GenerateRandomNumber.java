package com.meli.bootcamp.integrativeproject.utils;

public class GenerateRandomNumber {
    public static Integer generateRandomBatchNumber() {
        Integer min = 100000;
        Integer max = 999999;

        Integer randomBatchNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        return randomBatchNumber;
    }
}
