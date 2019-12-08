package com.spi.impl;


import com.spi.Helloworld;

public class ChickenHelloImpl implements Helloworld {
    @Override
    public void hello() {
        System.out.println("大爷来玩啊");
    }
}
