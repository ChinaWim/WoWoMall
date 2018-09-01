package com.ming.wowomall.service;

import com.github.pagehelper.PageInfo;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.Shipping;

/**
 * @author m969130721@163.com
 * @date 18-9-1 上午10:15
 */
public interface ShippingService {

    ServerResponse<Integer> insertOrUpdate(Shipping shipping);

    ServerResponse<Integer> deleteById(Integer shippingId);

    ServerResponse<Shipping> getByShippingId(Integer shippingId);

    ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize);
}
