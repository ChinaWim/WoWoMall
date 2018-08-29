package com.ming.wowomall;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class WowomallApplicationTests {


    @Test
    public void contextLoads() throws InterruptedException {
        new Thread(() -> {
            System.out.println("------");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
