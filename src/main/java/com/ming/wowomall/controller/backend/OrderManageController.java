package com.ming.wowomall.controller.backend;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.OrderService;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author m969130721@163.com
 * @date 18-9-7 上午10:52
 */
@RestController
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private OrderService orderService;


//    list.do

    @RequestMapping("/list.do")
    public ServerResponse list(HttpServletRequest request,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10")Integer pageSize){

        return orderService.listManageOrder(pageNum,pageSize);
    }

    @RequestMapping("/search.do")
    public ServerResponse search(HttpServletRequest request,Long orderNo,
                         @RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10")Integer pageSize){

        return orderService.getManageOrder(orderNo,pageNum,pageSize);
    }



    @RequestMapping("/detail.do")
    public ServerResponse detail(HttpServletRequest request,Long orderNo){

        return orderService.getManageOrderDetail(orderNo);
    }


    @RequestMapping("/send_goods.do")
    public ServerResponse sendGoods(HttpServletRequest request,Long orderNo){

        return orderService.sendGoods(orderNo);
    }

}
