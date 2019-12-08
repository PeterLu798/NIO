package com.spi.impl;


import com.spi.Helloworld;

public class DogHelloImpl implements Helloworld {
    @Override
    public void hello() {
        System.out.println("汪汪汪");
    }
}
