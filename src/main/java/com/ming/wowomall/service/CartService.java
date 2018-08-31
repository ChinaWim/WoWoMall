package com.ming.wowomall.service;

import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.vo.CartProductListVO;

import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-8-31 下午2:29
 */
public interface CartService {
    ServerResponse<Map> listCartVOByUserId(Integer userId);

    ServerResponse<Map>  insertOrUpdateCart(Integer userId,Integer productId, Integer count);

    ServerResponse<Map>  deleteByProductIdsWithUserId(Integer userId,List<String> productIdList);

    ServerResponse<Map>  updateProductCheckedStatus(Integer userId,Integer productId,Integer status);

    ServerResponse<Integer> countCartProductByUserId(Integer userId);

    ServerResponse<Map> updateSelectAllProductByUserId(Integer userId,Integer status);
}
