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
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/get_category.do",method = RequestMethod.POST)
    public ServerResponse getCategory(@RequestParam(defaultValue = "0") Integer categoryId, HttpServletRequest request){

        return categoryService.getChildrenParallelCategoryById(categoryId);
    }


    @RequestMapping(value = "/add_category.do",method = RequestMethod.POST)
    public ServerResponse addCategory(@RequestParam(defaultValue = "0") Integer parentId, String categoryName, HttpServletRequest request){

        return categoryService.insertCategory(parentId,categoryName);
    }


    @RequestMapping(value = "/set_category_name.do",method = RequestMethod.POST)
    public ServerResponse setCategoryName(Integer categoryId, String categoryName, HttpServletRequest request){

        return categoryService.updateCategoryName(categoryId,categoryName);
    }

    @RequestMapping(value = "/get_deep_category.do",method = RequestMethod.POST)
    public ServerResponse getDeepCategory(Integer categoryId,HttpServletRequest request){

        return categoryService.getChildrenAndChildrenIdsById(categoryId);
    }




}
