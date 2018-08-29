package com.ming.wowomall.service;

import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.Category;

import java.util.List;

/**
 * @author m969130721@163.com
 * @date 18-8-29 下午2:53
 */
public interface CategoryService {
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse addCategory(Integer parentId,String categoryName);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 递归查询本节点id和孩子节点id
     * @param categoryId
     * @return
     */
    ServerResponse<List<Integer>> getDeepCategory(Integer categoryId);


}
