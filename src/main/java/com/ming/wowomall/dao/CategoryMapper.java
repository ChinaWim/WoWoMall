package com.ming.wowomall.dao;

import com.ming.wowomall.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> listChildrenByParentId(Integer parentId);

    List<Integer> listChildrenIdByParentId(Integer parentId);

    int addBatch(@Param("categoryList") List<Category> categoryList);

}