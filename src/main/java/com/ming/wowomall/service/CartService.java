package com.ming.wowomall.service;

import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.vo.CartVO;

import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-8-31 下午2:29
 */
public interface CartService {
    ServerResponse<CartVO> list(Integer userId);

    ServerResponse<CartVO>  insertCart(Integer userId,Integer productId, Integer count);

    ServerResponse updateCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVO>  deleteByProductIdsWithUserId(Integer userId, List<String> productIdList);

    ServerResponse<CartVO>  updateProductCheckedOrUnChecked(Integer userId,Integer productId,Integer checked);

    ServerResponse<Integer> countCartProductByUserId(Integer userId);

    ServerResponse<CartVO> updateAllProductCheckedOrUnChecked(Integer userId,Integer checked);
}
