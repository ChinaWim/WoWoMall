package com.ming.wowomall.service.impl;

import com.google.common.collect.Maps;
import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.dao.CartMapper;
import com.ming.wowomall.dao.ProductMapper;
import com.ming.wowomall.pojo.Cart;
import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.service.CartService;
import com.ming.wowomall.vo.CartProductListVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-8-31 下午2:30
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<Map> listCartVOByUserId(Integer userId) {
        List<CartProductListVO> cartProductListVOList = cartMapper.listCartVOByUserId(userId);
        boolean allChecked = true;
        BigDecimal cartTotalPrice = new BigDecimal(0);
        for (CartProductListVO cartVO : cartProductListVOList) {
            if(cartVO.getProductChecked() == Const.CartProductStatus.UN_CHECKED){
                allChecked = false;
            }
            if (cartVO.getQuantity() > cartVO.getProductStock()){
                cartVO.setLimitQuantity("LIMIT_NUM_FAIL");
            } else {
                cartVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
            }
            BigDecimal cartProductStock = new BigDecimal(cartVO.getQuantity());
            BigDecimal productTotalPrice = cartVO.getProductPrice().multiply(cartProductStock);
            productTotalPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
            cartVO.setProductTotalPrice(productTotalPrice);
            if (cartVO.getProductChecked() == Const.CartProductStatus.CHECKED) {
                cartTotalPrice = cartTotalPrice.add(productTotalPrice);
            }
        }
        cartTotalPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
        Map<String,Object> resultMap = Maps.newHashMap();
        resultMap.put("cartProductVoList",cartProductListVOList);
        resultMap.put("allChecked",allChecked);
        resultMap.put("cartTotalPrice",cartTotalPrice);
        return ServerResponse.createBySuccess(resultMap);

    }

    @Override
    public ServerResponse insertOrUpdateCart(Integer userId,Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product != null && !Const.ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())){
            return ServerResponse.createByErrorMessage("商品下架或已删除");
        }
        List<Cart> cartList = cartMapper.getCartByUserIdWithProductId(userId, productId);
        if (!CollectionUtils.isEmpty(cartList)) {
            Cart cart = cartList.get(0);
            cart.setQuantity(cart.getQuantity() + count);
            cartMapper.updateByPrimaryKeySelective(cart);

        }else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(count);
            cartMapper.insertSelective(cart);
        }
        return this.listCartVOByUserId(userId);
    }

    @Override
    public ServerResponse deleteByProductIdsWithUserId(Integer userId,List<String> productIdList) {
        //todo
        cartMapper.deleteByProductIdsWithUserId(userId,productIdList.size() == 0 ? null : productIdList);

        return this.listCartVOByUserId(userId);
    }

    @Override
    public ServerResponse<Map> updateProductCheckedStatus(Integer userId,Integer productId,Integer status) {
        cartMapper.updateProductStatusByUserIdWithProductId(userId, productId, status);
        return this.listCartVOByUserId(userId);
    }

    @Override
    public ServerResponse<Integer> countCartProductByUserId(Integer userId) {
        int count = cartMapper.countCartProductByUserId(userId);
        return ServerResponse.createBySuccess(count);
    }

    @Override
    public ServerResponse<Map> updateSelectAllProductByUserId(Integer userId,Integer status) {
        cartMapper.updateAllProductStatusByUserId(userId, status);
        return this.listCartVOByUserId(userId);
    }

}
