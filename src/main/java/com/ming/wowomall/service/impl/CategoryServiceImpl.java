package com.ming.wowomall.service.impl;

import com.google.common.collect.Sets;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.dao.CategoryMapper;
import com.ming.wowomall.dao.ProductMapper;
import com.ming.wowomall.pojo.Category;
import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.service.CategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author m969130721@163.com
 * @date 18-8-29 下午2:53
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);


    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategoryById(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMessage("传递参数错误");
        }
        List<Category> categoryList = categoryMapper.listChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到该品类的子品类");
            return ServerResponse.createByErrorMessage("未找到该品类的子品类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse insertCategory(Integer parentId,String categoryName) {
        if (parentId == null || !StringUtils.isNoneBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("传递参数错误");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        //分类可用
        category.setStatus(true);
        int effectRow = categoryMapper.insertSelective(category);
        return effectRow > 0 ? ServerResponse.createBySuccessMessage("添加品类成功"):
                ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || !StringUtils.isNoneBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("传递参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int effectRow = categoryMapper.updateByPrimaryKeySelective(category);
        return effectRow > 0 ? ServerResponse.createBySuccessMessage("更新品类名字成功"):
                ServerResponse.createByErrorMessage("更新品类名字失败");
    }


    @Override
    public ServerResponse<List<Integer>> getChildrenAndChildrenIdsById(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMessage("传递参数错误");
        }
        if (categoryMapper.checkCategoryId(categoryId) == 0) {
            return ServerResponse.createByErrorMessage("找不到该品类");
        }
        Set<Integer> categoryIdsSet = Sets.newHashSet(categoryId);
        this.setDeepCategoryIds(categoryIdsSet,categoryId);
        List<Integer> categoryIdsList = new ArrayList<>(categoryIdsSet);
        return  ServerResponse.createBySuccess(categoryIdsList);
    }

    private void setDeepCategoryIds(Set<Integer> resultSet, Integer categoryId){
        List<Integer> categoryIds = categoryMapper.listChildrenIdByParentId(categoryId);
        resultSet.addAll(categoryIds);
        for (Integer cid : categoryIds) {
            setDeepCategoryIds(resultSet,cid);
        }
    }

}
