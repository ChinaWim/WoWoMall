package com.ming.wowomall.service.impl;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.cache.TokenCache;
import com.ming.wowomall.dao.UserMapper;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.UserService;
import com.ming.wowomall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author m969130721@163.com
 * @date 18-8-28 下午5:18
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        ServerResponse response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        password = MD5Util.MD5EncodeUtf8(password);
        User originUser = userMapper.getByLogin(username, password);
        if (originUser == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        originUser.setPassword(null);
        originUser.setQuestion(null);
        originUser.setAnswer(null);
        return ServerResponse.createBySuccessMessage("登录成功",originUser);
    }

    @Override
    public ServerResponse register(User user) {
        ServerResponse response = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户已存在");
        }
        response = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int effectRow = userMapper.insertSelective(user);
        return effectRow > 0 ?  ServerResponse.createBySuccessMessage("注册成功") :
                ServerResponse.createByErrorMessage("注册失败");
    }

    @Override
    public ServerResponse forgetGetQuestion(String username) {
        ServerResponse response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.getQuestionByUsername(username);
        if (question == null) {
            return ServerResponse.createByErrorMessage("该用户未设置找回密码问题");
        }
        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse forgetCheckAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount == 0){
            return ServerResponse.createBySuccessMessage("问题答案错误");
        }
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username,forgetToken);
        return ServerResponse.createBySuccess(forgetToken);
    }

    @Override
    public ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("forgetToken参数需要传递");
        }
        User originUser = userMapper.getByUsername(username);
        if (originUser == null) {
            ServerResponse.createByErrorMessage("用户不存在");
        }
        if (!StringUtils.equals(TokenCache.getKey(TokenCache.TOKEN_PREFIX+username),forgetToken)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        //更新
        passwordNew = MD5Util.MD5EncodeUtf8(passwordNew);
        originUser.setPassword(passwordNew);
        int effectRow = userMapper.updateByPrimaryKeySelective(originUser);
        return effectRow > 0 ?  ServerResponse.createBySuccessMessage("修改密码成功") :
                ServerResponse.createByErrorMessage("修改密码失败");

    }


    @Override
    public ServerResponse resetPassword(String passwordOld, String passwordNew,Integer userId) {
        User originalUser = userMapper.selectByPrimaryKey(userId);

        if(!MD5Util.MD5EncodeUtf8(passwordOld).equals(originalUser.getPassword())){
            return ServerResponse.createByErrorMessage("旧密码输入错误");
        }
        originalUser.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int effectRow = userMapper.updateByPrimaryKeySelective(originalUser);
        return effectRow > 0 ?  ServerResponse.createBySuccessMessage("修改密码成功") :
                ServerResponse.createByErrorMessage("修改密码失败");

    }

    @Override
    public ServerResponse updateInformation(User user) {
        if (StringUtils.isNoneBlank(user.getEmail())) {
            int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
            if(resultCount > 0){
                return ServerResponse.createByErrorMessage("邮箱已存在");
            }
        }
        int effectRow = userMapper.updateByPrimaryKeySelective(user);
        User originUser = userMapper.selectByPrimaryKey(user.getId());
        originUser.setPassword(null);
        originUser.setQuestion(null);
        originUser.setAnswer(null);
        return effectRow > 0 ?  ServerResponse.createBySuccessMessage("更新个人信息成功",originUser) :
                ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse getInformation(Integer userId) {
        User originUser = userMapper.selectByPrimaryKey(userId);
        originUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(originUser);
    }


    @Override
    public ServerResponse checkValid(String str, String type) {
        if (StringUtils.isBlank(type)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        int resultCount = 0;
        if (type.equals(Const.USERNAME)){
            resultCount = userMapper.checkUsername(str);
        }else if(type.equals(Const.EMAIL)){
            resultCount = userMapper.checkEmail(str);
        }
        return resultCount == 0 ?  ServerResponse.createBySuccessMessage("校验成功") :
                ServerResponse.createByErrorMessage(type+"已存在");
    }

}
