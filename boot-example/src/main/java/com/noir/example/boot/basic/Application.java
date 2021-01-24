package com.noir.example.boot.basic;

import org.noir.guice.boot.GuiceBootApplication;
import org.noir.guice.boot.annotations.Bootstrap;

@Bootstrap
public class Application {

    public static void main(String[] args) {
        GuiceBootApplication.run(Application.class);
    }
}
