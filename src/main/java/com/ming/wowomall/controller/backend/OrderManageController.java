package com.ming.wowomall.controller.backend;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
    public Object list(HttpSession session,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10")Integer pageSize){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.listManageOrder(pageNum,pageSize);
    }

    @RequestMapping("/search.do")
    public Object search(HttpSession session,Long orderNo,
                         @RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10")Integer pageSize){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.getManageOrder(orderNo,pageNum,pageSize);
    }



    @RequestMapping("/detail.do")
    public Object detail(HttpSession session,Long orderNo){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.getManageOrderDetail(orderNo);
    }


    @RequestMapping("/send_goods.do")
    public Object sendGoods(HttpSession session,Long orderNo){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.sendGoods(orderNo);
    }

}
