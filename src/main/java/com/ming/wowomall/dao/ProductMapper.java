package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.vo.ProductListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> listByCategoryId(Integer categoryId);

    List<ProductListVO> listProduct();

    List<ProductListVO> listProductByNameOrProductId(@Param("productName") String productName,@Param("productId") Integer productId);

    int updateStatus(@Param("productId") Integer productId,@Param("status")Integer status);

    List<ProductListVO> listProductByKeyWordCategoryId(@Param("categoryIdList") List categoryIdList, @Param("keyword") String keyword);
}