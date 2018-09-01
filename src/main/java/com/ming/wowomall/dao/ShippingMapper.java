package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    List<Shipping> list();

    int deleteByUserIdShippingId(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    Shipping getByUserIdShippingId(@Param("userId")Integer userId,@Param("shippingId")Integer shippingId);
}