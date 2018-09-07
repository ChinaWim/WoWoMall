package com.ming.wowomall.service;

import com.github.pagehelper.PageInfo;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.vo.OrderVO;

import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-9-1 下午4:07
 */
public interface OrderService {
    ServerResponse insertOrder(Integer userId, Integer shippingId);

    ServerResponse<Map> pay(Integer userId, Long orderNo, String path);

    /**
     * 获取订单状态
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<Boolean> getOrderPayStatus(Integer userId,Long orderNo);


    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse<Map> getOrderCartProduct(Integer userId);

    ServerResponse<PageInfo> listOrderByUserId(Integer userId,Integer pageNum,Integer pageSize);

    ServerResponse getOrderDetail(Integer userId, Long orderNo);

    ServerResponse cancelOrder(Integer userId, Long orderNo);

}
