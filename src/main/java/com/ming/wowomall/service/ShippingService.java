package com.ming.wowomall.service;

import com.github.pagehelper.PageInfo;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.Shipping;

import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-9-1 上午10:15
 */
public interface ShippingService {

    ServerResponse<Map> insertOrUpdate(Integer userId,Shipping shipping);

    ServerResponse<Integer> delete(Integer userId,Integer shippingId);

    ServerResponse<Shipping> getShipping(Integer userId,Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId,Integer pageNum, Integer pageSize);
}
