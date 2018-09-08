package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.OrderItem;
import com.ming.wowomall.vo.OrderItemVO;
import com.ming.wowomall.vo.OrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> listByUserIdOrderNo(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);

    List<OrderItem> listByOrderNo(Long orderNo);

    int insertBatch(@Param("orderItemList") List<OrderItem> orderItemList);




}