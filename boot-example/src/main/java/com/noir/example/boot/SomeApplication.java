package com.noir.example.boot;

import org.noir.guice.boot.GuiceBootApplication;
import org.noir.guice.boot.annotations.Bootstrap;

@Bootstrap
public class SomeApplication {

    public static void main(String[] args) {
        GuiceBootApplication.run(SomeApplication.class);
    }
}
