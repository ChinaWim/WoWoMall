package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.Cart;
import com.ming.wowomall.vo.CartProductListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<CartProductListVO> listCartVOByUserId(Integer userId);

    List<Cart> getCartByUserIdWithProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    int deleteByProductIdsWithUserId(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    int updateProductStatusByUserIdWithProductId(@Param("userId")Integer userId,@Param("productId") Integer productId,@Param("status") int status);

    int countCartProductByUserId(Integer userId);

    int updateAllProductStatusByUserId(@Param("userId")Integer userId,@Param("status") int status);

}