package com.noir.example.boot.inj;

import com.google.inject.Inject;
import org.noir.guice.boot.annotations.Injectable;

@Injectable
public class Foo {

    @Inject
    private Some some;

    public String textFoo() {
        return some.hello();
    }

}
