package com.ming.wowomall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.dao.ShippingMapper;
import com.ming.wowomall.pojo.Shipping;
import com.ming.wowomall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-9-1 上午10:15
 */
@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;


    @Override
    public ServerResponse<Map> insertOrUpdate(Integer userId,Shipping shipping) {
        if(userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //防止横向越权
        shipping.setUserId(userId);

        if(shipping.getId() != null){
            int effectRow = shippingMapper.updateByPrimaryKeySelective(shipping);
            return effectRow > 0 ? ServerResponse.createBySuccessMessage("更新地址成功")
                    : ServerResponse.createByErrorMessage("更新地址失败");
        }

        int effectRow = shippingMapper.insertSelective(shipping);
        Map resultMap = Maps.newHashMap();
        resultMap.put("shippingId",shipping.getId());
        return effectRow > 0 ? ServerResponse.createBySuccessMessage("新建地址成功",resultMap)
                : ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<Integer> delete(Integer userId,Integer shippingId) {
       if (shippingId == null || userId == null) {
           return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
       }
       //防止横向越权
        int effect = shippingMapper.deleteByUserIdShippingId(userId,shippingId);
        return effect > 0 ? ServerResponse.createBySuccessMessage("删除地址成功")
                : ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse<Shipping> getShipping(Integer userId,Integer shippingId){
        if (shippingId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Shipping shipping = shippingMapper.getByUserIdShippingId(userId,shippingId);
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,true);
        List<Shipping> shippingList = shippingMapper.list();
        PageInfo<Shipping> pageInfo = new PageInfo<>(shippingList);
        return  ServerResponse.createBySuccess(pageInfo);
    }



}
