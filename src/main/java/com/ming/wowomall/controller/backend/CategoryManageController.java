package com.ming.wowomall.controller.backend;

import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.CategoryService;
import com.ming.wowomall.service.UserService;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author m969130721@163.com
 * @date 18-8-29 下午2:44
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @PostMapping("/get_category.do")
    public ServerResponse getCategory(@RequestParam(defaultValue = "0") Integer categoryId, HttpServletRequest request){

        return categoryService.getChildrenParallelCategoryById(categoryId);
    }


    @PostMapping("/add_category.do")
    public ServerResponse addCategory(@RequestParam(defaultValue = "0") Integer parentId, String categoryName, HttpServletRequest request){

        return categoryService.insertCategory(parentId,categoryName);
    }


    @PostMapping("/set_category_name.do")
    public ServerResponse setCategoryName(Integer categoryId, String categoryName, HttpServletRequest request){

        return categoryService.updateCategoryName(categoryId,categoryName);
    }

    @PostMapping("/get_deep_category.do")
    public ServerResponse getDeepCategory(Integer categoryId,HttpServletRequest request){

        return categoryService.getChildrenAndChildrenIdsById(categoryId);
    }




}
