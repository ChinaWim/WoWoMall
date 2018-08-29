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
     *检查是否存在
     * @param str
     * @param type
     * @return
     */
    ServerResponse checkValid(String str, String type);

    ServerResponse forgetGetQuestion(String username);

    ServerResponse forgetCheckAnswer(String username, String question, String answer);

    ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse resetPassword(String passwordOld, String passwordNew,Integer userId);

    ServerResponse updateInformation(User user);

    ServerResponse getInformation(Integer userId);
}
