package com.tyson.inventory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {

    public static void main(String[] args) {

        System.out.println(
                new BCryptPasswordEncoder().encode("Tyson1619")
        );

    }
}