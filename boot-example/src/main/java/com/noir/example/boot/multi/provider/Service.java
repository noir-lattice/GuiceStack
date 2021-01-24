package com.noir.example.boot.multi.provider;

import com.google.inject.Inject;
import com.noir.example.boot.multi.instance.Base;
import org.noir.guice.boot.annotations.Injectable;

import java.util.Map;
import java.util.Set;

@Injectable
public class Service {

    @Inject
    private Set<Base> imps;

    @Inject
    private Map<String, Base> mapImps;

    public int impsCount() {
        return imps.size();
    }

    public int mapCount() {
        return mapImps.size();
    }

}
