package com.inner.other;

import com.inner.NoNameInner;

public class NoNameOuter {
    public void test(NoNameInner noNameInner){
        System.out.println(noNameInner.getName());
    }

    public static void main(String[] args) {
        final int name1 = 1;
        NoNameOuter noNameOuter = new NoNameOuter();
        noNameOuter.test(new NoNameInner() {
            @Override
            public void setName(String name) {
                name = String.valueOf(name1);
            }
        });
    }
}
