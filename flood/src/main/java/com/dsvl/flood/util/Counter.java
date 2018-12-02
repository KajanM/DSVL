package com.dsvl.flood.util;

public class Counter {
    public static long id;

    public static long nextId() {
        id++;
        return id;
    }
}
