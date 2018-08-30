package com.ming.wowomall.controller.backend;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.CategoryService;
import com.ming.wowomall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
    public Object getCategory(@RequestParam(defaultValue = "0") Integer categoryId, HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()) {
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return categoryService.getChildrenParallelCategoryById(categoryId);
    }


    @PostMapping("/add_category.do")
    public Object addCategory(@RequestParam(defaultValue = "0") Integer parentId, String categoryName, HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()) {
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return categoryService.insertCategory(parentId,categoryName);
    }


    @PostMapping("/set_category_name.do")
    public Object setCategoryName(Integer categoryId, String categoryName, HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()) {
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return categoryService.updateCategoryName(categoryId,categoryName);
    }

    @PostMapping("/get_deep_category.do")
    public Object getDeepCategory(Integer categoryId,HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()) {
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return categoryService.getChildrenAndChildrenIdsById(categoryId);
    }




}
