package com.inner;

/**
 * @author lubaijiang
 */
public class People {

    private void v () {
        class Woman{
            public Woman(String name){

            }
            private final String a = "ffffffffff";
             String b = "gggggggggggggg";
            public String getName(){
                return "v";
            }
        }
        Woman woman = new Woman("dddd");
        System.out.println(woman.getName());
        System.out.println(woman.b);
        System.out.println(woman.a);
    }

    private void t () {

    }

    public static void main(String[] args) {
        People people = new People();
        people.v();
    }
}
