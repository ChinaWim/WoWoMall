package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    int checkValid(@Param("str") String str, @Param("type") String type);

    int checkAnswer(@Param("username")String username,@Param("question") String question,@Param("answer") String answer);

    User getByLogin(@Param("username") String username,@Param("password") String password);

    User getByUsername(String username);

    String getQuestionByUsername(String username);

    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);

}