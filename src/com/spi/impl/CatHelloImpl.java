package com.spi.impl;


import com.spi.Helloworld;

public class CatHelloImpl implements Helloworld {
    @Override
    public void hello() {
        System.out.println("喵喵喵");
    }
}
