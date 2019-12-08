package com.inner;

/**
 * @author lubaijiang
 */
public class Circle {
    private double radius = 0;
    private static int count = 1;
    public Circle(double radius){
        this.radius = radius;
    }

    protected void eee(){
        System.out.println("eee");
        Draw draw = new Draw();
        draw.draw();
        draw.b();
        System.out.println(draw.radius);

    }

    void c (){}

    class Draw{
        private double radius = 1;
         int aa = 1;
        private void eee(){
            System.out.println("rrr");
        }
        public void draw(){
            System.out.println("ddddddddddddd");
            System.out.println(radius);

            System.out.println(Circle.this.radius);

            System.out.println(count);
            eee();
            Circle.this.eee();
        }

        public void a (){
            System.out.println("a");
        }

        protected void b (){
            System.out.println("b");
        }

        void c (){
            System.out.println("c");
        }

        private void d (){
            System.out.println("d");
        }

        public void cry(){
            System.out.println();
        }
    }

    static class MM{
        public void say(){
            System.out.println("ddddddd");
        }

        public static void cry(){
            System.out.println("cccccccc");
        }
    }
}
