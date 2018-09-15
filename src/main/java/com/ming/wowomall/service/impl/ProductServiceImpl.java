package com.ming.wowomall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.dao.CategoryMapper;
import com.ming.wowomall.dao.ProductMapper;
import com.ming.wowomall.pojo.Category;
import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.service.CategoryService;
import com.ming.wowomall.service.ProductService;
import com.ming.wowomall.util.DateTimeUtil;
import com.ming.wowomall.util.PropertiesUtil;
import com.ming.wowomall.util.UUIDUtil;
import com.ming.wowomall.vo.ProductDetailVO;
import com.ming.wowomall.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-8-29 下午9:58
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public ServerResponse<PageInfo> getManageProductList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<ProductListVO> productVOList = productMapper.listProductListVO();
        productVOList.stream().forEach(x->x.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.wowomall.com/")));
        PageInfo<ProductListVO> pageInfo = new PageInfo<>(productVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> getProductByNameOrProductId(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<ProductListVO> productVOList = productMapper.listProductListVOByNameOrProductId(productName, productId);
        productVOList.stream().forEach(x->x.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.wowomall.com/")));
        PageInfo<Product> pageInfo = new PageInfo(productVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<ProductDetailVO> getManageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMessage("参数传递错误");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品下架或已删除");
        }
        ProductDetailVO productDetailVO = assembledProductDetail(product);
        return ServerResponse.createBySuccess(productDetailVO);

    }

    @Override
    public ServerResponse updateProductStatus(Integer productId,Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorMessage("参数传递错误");
        }
        int effectRow = productMapper.updateStatus(productId,status);
        return effectRow > 0 ?  ServerResponse.createBySuccessMessage("更新商品状态成功") :
                ServerResponse.createByErrorMessage("更新商品状态失败");
    }

    @Override
    public ServerResponse insertOrUpdate(Product product) {
        if (product.getId() == null) {
            String[] subImgArray = product.getSubImages().split(",");
            if (subImgArray.length > 0) {
                product.setMainImage(subImgArray[0]);
            }
            int effectRow = productMapper.insertSelective(product);
            return effectRow > 0 ?  ServerResponse.createBySuccessMessage("新增产品成功") :
                    ServerResponse.createByErrorMessage("新增产品失败");
        } else {
            int effectRow = productMapper.updateByPrimaryKeySelective(product);
            return effectRow > 0 ?  ServerResponse.createBySuccessMessage("更新产品成功") :
                    ServerResponse.createByErrorMessage("更新产品失败");
        }
    }

    @Override
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorMessage("参数传递错误");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品下架或已删除");
        }
        if (Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()){
            return ServerResponse.createByErrorMessage("商品下架或已删除");
        }
        ProductDetailVO productDetailVO = this.assembledProductDetail(product);
        return  ServerResponse.createBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<PageInfo> getProductList(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if (categoryId == null && StringUtils.isBlank(keyword)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = Lists.newArrayList();
        ServerResponse<List<Integer>> ids = categoryService.getChildrenAndChildrenIdsById(categoryId);
        if (ids.isSuccess()) {
            categoryIdList.addAll(ids.getData());
        }
        PageHelper.startPage(pageNum,pageSize,true);
        //排序处理
        if (StringUtils.isNoneBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderArray = orderBy.split("_");
                PageHelper.orderBy(orderArray[0]+" "+orderArray[1]);
            }
        }
        List<ProductListVO> productVOList = productMapper.listProductListVOByKeyWordCategoryId(categoryIdList.size() == 0 ? null : categoryIdList, keyword);
        productVOList.stream().forEach(x->x.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.wowomall.com/")));
        PageInfo<ProductListVO> pageInfo = new PageInfo<>(productVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }


    private ProductDetailVO assembledProductDetail(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setCategoryId(product.getCategoryId());
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        productDetailVO.setName(product.getName());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://image.wowomall.com/"));

        productDetailVO.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVO;

    }


}
