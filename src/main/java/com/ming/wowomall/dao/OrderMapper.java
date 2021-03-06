package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.Order;
import com.ming.wowomall.vo.OrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order getByUserIdOrderNo(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);

    Order getByOrderNo(Long orderNo);

    int updateOrderStatus(@Param("userId") Integer userId,@Param("orderNo")Long orderNo,@Param("status")Integer status);

    List<Order> listOrderByUserId(Integer userId);

    List<Order> listOrder();

    List<Order> listOrderByStatusCreateTime(@Param("status")Integer status,@Param("dateTime") String dateTime);

    int updateListOrderStatus(@Param("orderIds") List<Integer> orderIds,@Param("status") Integer status);
}