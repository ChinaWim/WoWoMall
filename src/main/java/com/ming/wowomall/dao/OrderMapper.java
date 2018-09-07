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

    List<OrderVO> listOrderVOByUserId(Integer userId);

    OrderVO getOrderVOByOrderNo(Long orderNo);

    int updateOrderStatus(@Param("userId") Integer userId,@Param("orderNo")Long orderNo,@Param("status")Integer status);

}