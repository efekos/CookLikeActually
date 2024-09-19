package dev.efekos.cla.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CachedFunction<T, R> {

    private final Function<T, R> func;
    private final Map<Integer, R> cache = new HashMap<>();

    public CachedFunction(Function<T, R> func) {
        this.func = func;
    }

    public R apply(T t) {
        int i = t.hashCode();
        if (cache.containsKey(i)) return cache.get(i);
        R r = func.apply(t);
        cache.put(i, r);
        return r;
    }

    public void clear() {
        cache.clear();
    }

}
