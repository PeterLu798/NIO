package com.operator;

/**
 * @author lubaijiang
 */
public class Operator {
    public static void main(String[] args) {
        int i = 0;
        int i1 = i++;
        System.out.println("i= " + i + " i1= " + i1);

        int j = 0;
        int j1 = ++j;
        System.out.println("j= " + j + " j1= " + j1);

        int k = 0;
        k = k++;
        System.out.println("k= " + k);

        int g = 0;
        ++g;
        System.out.println("g= " + g);
    }




}
