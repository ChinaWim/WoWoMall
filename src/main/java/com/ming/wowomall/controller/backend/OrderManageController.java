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
    public Object list(HttpServletRequest request,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10")Integer pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.listManageOrder(pageNum,pageSize);
    }

    @RequestMapping("/search.do")
    public Object search(HttpServletRequest request,Long orderNo,
                         @RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10")Integer pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.getManageOrder(orderNo,pageNum,pageSize);
    }



    @RequestMapping("/detail.do")
    public Object detail(HttpServletRequest request,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.getManageOrderDetail(orderNo);
    }


    @RequestMapping("/send_goods.do")
    public Object sendGoods(HttpServletRequest request,Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        if (Const.Role.ROLE_ADMIN != currentUser.getRole()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return orderService.sendGoods(orderNo);
    }

}
