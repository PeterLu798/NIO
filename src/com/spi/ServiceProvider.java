package com.spi;

import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author lubaijiang
 */
public class ServiceProvider {
    public static <S> Collection<S> load(Class<S> service){
        ServiceLoader<S> loader = ServiceLoader.load(service);
        Iterator<S> iterator = loader.iterator();
        while (iterator.hasNext()){
            S next = iterator.next();
            if(next instanceof Helloworld){
                ((Helloworld) next).hello();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ServiceProvider.load(Helloworld.class);
    }
}
