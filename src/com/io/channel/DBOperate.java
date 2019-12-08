package com.io.channel;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lubaijiang
 */
public class DBOperate implements Closeable {


    public void update(){
        System.out.println("更新操作");
    }

    public static void main(String[] args) {
        try (DBOperate dbOperate = new DBOperate()) {
            dbOperate.update();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("关闭资源");
    }
}
