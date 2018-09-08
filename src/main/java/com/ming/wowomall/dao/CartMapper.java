package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.Cart;
import com.ming.wowomall.vo.CartProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<CartProductVO> listCartProductVOByUserId(Integer userId);

    List<Cart> getCartByUserIdWithProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    int deleteByProductIdsWithUserId(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    int updateProductCheckedOrUnChecked(@Param("userId")Integer userId,@Param("productId") Integer productId,@Param("checked") int checked);

    int countCartProductByUserId(Integer userId);

    int updateAllProductCheckedOrUnChecked(@Param("userId")Integer userId,@Param("checked") int checked);

    int updateByUserIdWithProductId(@Param("userId")Integer userId, @Param("productId")Integer productId,@Param("count") Integer count);

    List<Cart> listCheckedCart(Integer userId);

    int deleteCheckedCartByUserId(Integer userId);
}