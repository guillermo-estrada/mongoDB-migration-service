package com.migration.hexa.migrationdb.config;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class test {

    public static void main(String[] args) throws IOException {
        Faker faker = new Faker();

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Employee VALUES ");

        for (int i = 0; i < 300000; i++) {
            Random rn = new Random();
            int answer = rn.nextInt(10) + 1;
            String firstName = faker.name().firstName();
            String lastname = faker.name().lastName();
            String phone = faker.phoneNumber().cellPhone();
            String aux = "(" + (i+1) + ", '" + firstName + "', '" + lastname + "', '" + firstName + lastname + "@hexaware.com', '" + phone + "', " + answer + "),\n";
            sb.append(aux);
        }
        sb.append(";");

        Files.write(Paths.get("./fileName.txt"), sb.toString().getBytes());
    }
}
