package com.noir.example.boot.inj;

import org.noir.guice.boot.annotations.Injectable;

@Injectable
public class Some {

    public String hello() {
        return "hello";
    }
}
