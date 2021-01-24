package com.noir.example.boot.multi.instance.impl;

import com.noir.example.boot.multi.instance.Base;
import org.noir.guice.boot.annotations.Injectable;

@Injectable
public class Some implements Base {

    @Override
    public String print() {
        return "some print";
    }

}
