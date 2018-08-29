package com.ming.wowomall.service;

import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * @author m969130721@163.com
 * @date 18-8-28 下午5:17
 */
public interface UserService {
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 用户注册
     * @param user
     * @return
     */
    ServerResponse register(User user);

    /**
     *　检查是否存在
     * @param str
     * @param type
     * @return
     */
    ServerResponse checkValid(String str, String type);

    /**
     *　忘记密码获取问题
     * @param username
     * @return
     */
    ServerResponse<String> forgetGetQuestion(String username);

    /**
     *　忘记密码检查答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);

    /**
     *　忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken);

    /**
     *　登录状态重置密码
     * @param passwordOld
     * @param passwordNew
     * @param userId
     * @return
     */
    ServerResponse resetPassword(String passwordOld, String passwordNew,Integer userId);

    /**
     *　更新用户信息
     * @param user
     * @return
     */
    ServerResponse<User> updateInformation(User user);

    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    ServerResponse<User> getInformation(Integer userId);

    /**
     * 检查是否管理员
     * @param user
     * @return
     */
    ServerResponse checkRoleAdmin(User user);
}
