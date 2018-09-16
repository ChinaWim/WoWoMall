package com.ming.wowomall.controller.portal;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.UserService;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * @author m969130721@163.com
 * @date 18-8-28 下午5:16
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login.do")
    public ServerResponse login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse response = userService.login(username, password);
        if (response.isSuccess()) {
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            RedisShardedPoolUtil.setEx(session.getId(),JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout.do")
    public ServerResponse logout(HttpServletRequest request,HttpServletResponse response){
        String loginToken = CookieUtil.readLoginToken(request);
        if (!StringUtils.isBlank(loginToken)){
            RedisShardedPoolUtil.del(loginToken);
        }
        CookieUtil.delLoginToken(request,response);
        return ServerResponse.createBySuccessMessage("退出成功");
    }


    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register.do")
    public ServerResponse register(User user){
        return userService.register(user);
    }


    /**
     * 校验用户
     * @param str
     * @param type
     * @return
     */
    @PostMapping("/check_valid.do")
    public ServerResponse checkValid(String str, String type){
        return userService.checkValid(str,type);
    }

    /**
     * 获取登录用户信息
     * @return
     */
    @PostMapping("/get_user_info.do")
    public ServerResponse getUserInfo(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        return ServerResponse.createBySuccess(currentUser);
    }

    /**
     * 忘记密码
     * @param username
     * @return
     */
    @PostMapping("/forget_get_question.do")
    public ServerResponse forgetGetQuestion(String username){
        return userService.forgetGetQuestion(username);
    }

    /**
     * 提交问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("/forget_check_answer.do")
    public ServerResponse forgetCheckAnswer(String username,String question,String answer){
        return userService.forgetCheckAnswer(username,question,answer);
    }


    /**
     *
     *忘记密码的重设密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("/forget_reset_password.do")
    public ServerResponse forgetResetPassword(String username,String passwordNew,String forgetToken){
        return userService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登录状态修改密码
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @PostMapping("/reset_password.do")
    public ServerResponse resetPassword(HttpServletRequest request,String passwordOld,String passwordNew){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }


        return userService.resetPassword(passwordOld,passwordNew,currentUser.getId());
    }

    /**
     *登录状态更新个人信息
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/update_information.do")
    public ServerResponse updateInformation(User user,HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        //防止横向越权
        user.setId(currentUser.getId());
        ServerResponse response = userService.updateInformation(user);
        if (response.isSuccess()){
            RedisShardedPoolUtil.setEx(loginToken,JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;

    }


    /**
     * 获取当前登录用户的详细信息，并强制登录
     * @return
     */
    @PostMapping("/get_information.do")
    public ServerResponse getInformation(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr,User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,获取当前用户信息失败");
        }

        return userService.getInformation(currentUser.getId());
    }


    @GetMapping("/test")
    public String test(HttpServletRequest request){
        Enumeration<String> names = request.getParameterNames();
        StringBuilder sb = new StringBuilder();
        while (names.hasMoreElements()){
            sb.append(request.getParameter(names.nextElement())+" ");
        }
        return sb.toString();
    }

    @GetMapping("/exception")
    public String exception(HttpServletRequest request){
        int i = 1/0;
        return "exception";
    }




}
