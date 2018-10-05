package com.ming.wowomall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author m969130721@163.com
 * @date 18-8-30 下午8:42
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @RequestMapping("/list.do")
    public ServerResponse<PageInfo> getProductList(Integer categoryId, String keyword,
                                         @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10")Integer pageSize, String orderBy){
        return productService.getProductList(categoryId,keyword,pageNum,pageSize,orderBy);
    }

    @RequestMapping("/detail.do")
    public ServerResponse getProductDetail(Integer productId){
        return productService.getProductDetail(productId);
    }


}
