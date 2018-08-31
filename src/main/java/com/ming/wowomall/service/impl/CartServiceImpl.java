package com.ming.wowomall.service.impl;

import com.google.common.collect.Maps;
import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.dao.CartMapper;
import com.ming.wowomall.dao.ProductMapper;
import com.ming.wowomall.pojo.Cart;
import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.service.CartService;
import com.ming.wowomall.util.BigDecimalUtil;
import com.ming.wowomall.util.PropertiesUtil;
import com.ming.wowomall.vo.CartProductVO;
import com.ming.wowomall.vo.CartVO;
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
    public ServerResponse<CartVO> list(Integer userId){
        if (userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        CartVO cartVO = this.getCartVOByUserId(userId);
        return ServerResponse.createBySuccess(cartVO);
    }


    @Override
    public ServerResponse insertCart(Integer userId,Integer productId, Integer count) {
        if (userId == null || productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || !Const.ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())){
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
        CartVO cartVO = this.getCartVOByUserId(userId);
        return ServerResponse.createBySuccess(cartVO);
    }




    @Override
    public ServerResponse updateCart(Integer userId,Integer productId, Integer count) {
        if (userId == null || productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || !Const.ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())){
            return ServerResponse.createByErrorMessage("商品下架或已删除");
        }
        cartMapper.updateByUserIdWithProductId(userId,productId,count);

        CartVO cartVO = this.getCartVOByUserId(userId);
        return ServerResponse.createBySuccess(cartVO);
    }


    @Override
    public ServerResponse deleteByProductIdsWithUserId(Integer userId,List<String> productIdList) {
        if (userId == null || CollectionUtils.isEmpty(productIdList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByProductIdsWithUserId(userId,productIdList.size() == 0 ? null : productIdList);

        CartVO cartVO = this.getCartVOByUserId(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> updateProductCheckedOrUnChecked(Integer userId,Integer productId,Integer checked) {
        if (userId == null || productId == null || checked == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.updateProductCheckedOrUnChecked(userId, productId, checked);
        CartVO cartVO = this.getCartVOByUserId(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<Integer> countCartProductByUserId(Integer userId) {
        if (userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int count = cartMapper.countCartProductByUserId(userId);
        return ServerResponse.createBySuccess(count);
    }

    @Override
    public ServerResponse<CartVO> updateAllProductCheckedOrUnChecked(Integer userId,Integer checked) {
        if (userId == null || checked == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.updateAllProductCheckedOrUnChecked(userId, checked);
        CartVO cartVO = this.getCartVOByUserId(userId);
        return ServerResponse.createBySuccess(cartVO);
    }


    private CartVO getCartVOByUserId(Integer userId) {
        CartVO cartVO = new CartVO();

        List<CartProductVO> cartProductVOList = cartMapper.listCartVOByUserId(userId);
        boolean allChecked = true;
        BigDecimal cartTotalPrice = new BigDecimal("0");

        for (CartProductVO cartProductVO : cartProductVOList) {
            if(cartProductVO.getProductChecked() == Const.Cart.UN_CHECKED){
                allChecked = false;
            }
            //判断库存
            int buyLimitCount = 0;
            if (cartProductVO.getQuantity() > cartProductVO.getProductStock()){
                cartProductVO.setLimitQuantity(Const.Cart.LIMI_NUM_FAIL);
                //防止库存溢出
                buyLimitCount = cartProductVO.getProductStock();
                Cart cartForQuantity = new Cart();
                cartForQuantity.setId(cartProductVO.getId());
                cartForQuantity.setQuantity(buyLimitCount);
                cartMapper.updateByPrimaryKeySelective(cartForQuantity);
            } else {
                buyLimitCount = cartProductVO.getQuantity();
                cartProductVO.setLimitQuantity(Const.Cart.LIMI_NUM_SUCCESS);
            }
            cartProductVO.setQuantity(buyLimitCount);
            //计算商品总价
            BigDecimal productTotalPrice = BigDecimalUtil.multiply(cartProductVO.getProductPrice().doubleValue(),buyLimitCount);
            cartProductVO.setProductTotalPrice(productTotalPrice);

            if (Const.Cart.CHECKED == cartProductVO.getProductChecked()) {
                cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
            }
        }

        cartVO.setCartProductVoList(cartProductVOList);
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setAllChecked(allChecked);
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.wowomall.com/"));
        return cartVO;
    }

}
