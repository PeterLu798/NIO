package com.inner;

/**
 * @author lubaijiang
 */
public class Main {
    public static void main(String[] args) {
        Circle circle = new Circle(2);
        Circle.Draw draw = circle.new Draw();
        draw.c();

        Circle.MM mm = new Circle.MM();
        mm.say();
        Circle.MM.cry();
    }
}
