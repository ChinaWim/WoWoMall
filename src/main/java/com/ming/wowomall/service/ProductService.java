package com.ming.wowomall.service;

import com.github.pagehelper.PageInfo;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.vo.ProductDetailVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-8-29 下午9:58
 */
public interface ProductService {

    ServerResponse<PageInfo> getManageProductList(Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo> getManageProductByNameOrProductId(String productName,Integer productId,Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVO> getManageProductDetail(Integer productId);

    ServerResponse updateProductStatus(Integer productId,Integer status);

    ServerResponse insertOrUpdate(Product product);

    ServerResponse<ProductDetailVO> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(Integer categoryId,String keyword,Integer pageNum, Integer pageSize,String orderBy);

}
