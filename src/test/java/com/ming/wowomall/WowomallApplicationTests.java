package com.ming.wowomall;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ming.wowomall.dao.UserMapper;
import com.ming.wowomall.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WowomallApplicationTests {
    @Autowired
    private UserMapper mapper;

    @Test
    public void contextLoads() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("123");
        logger.debug("1---------------------");
        logger.error("error --------------");

        PageHelper.startPage(1, 10);
        List<User> list = mapper.listAll();
        PageInfo<User> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        System.out.println(total);
        System.out.println(list);
    }

}
