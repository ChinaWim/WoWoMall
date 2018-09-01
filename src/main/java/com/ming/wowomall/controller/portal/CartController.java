package com.ming.wowomall.controller.portal;

import com.google.common.base.Splitter;
import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author m969130721@163.com
 * @date 18-8-31 下午2:31
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @GetMapping("/list.do")
    public Object list(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.list(currentUser.getId());
    }

    @GetMapping("/add.do")
    public Object addProduct(HttpSession session,Integer productId,Integer count){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
       return cartService.insertCart(currentUser.getId(),productId,count);
    }


    @RequestMapping("/update.do")
    public Object updateProduct(HttpSession session,Integer productId,Integer count){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.updateCart(currentUser.getId(),productId,count);
    }

    @RequestMapping("/delete_product.do")
    public Object deleteProduct(HttpSession session,String productIds){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (StringUtils.isBlank(productIds)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        /*String[] productIdArray = productIds.split(",");
        List<String> productIdList = Arrays.stream(productIdArray).collect(Collectors.toList());*/
        //guava
        List<String> productIdList = Splitter.on(",").splitToList(productIds);

        return cartService.deleteByProductIdsWithUserId(currentUser.getId(),productIdList);
    }

    @RequestMapping("/select.do")
    public Object selectProduct(HttpSession session,Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.updateProductCheckedOrUnChecked(currentUser.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("/un_select.do")
    public Object unSelectProduct (HttpSession session,Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.updateProductCheckedOrUnChecked(currentUser.getId(),productId,Const.Cart.UN_CHECKED);
    }

    @RequestMapping("/get_cart_product_count.do")
    public Object getCartProductCount (HttpSession session,Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createBySuccess(0);
        }
        return cartService.countCartProductByUserId(currentUser.getId());
    }


    @RequestMapping("/select_all.do")
    public Object selectAllProduct (HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.updateAllProductCheckedOrUnChecked(currentUser.getId(),Const.Cart.CHECKED);
    }

    @RequestMapping("/un_select_all.do")
    public Object unSelectAllProduct (HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.updateAllProductCheckedOrUnChecked(currentUser.getId(),Const.Cart.UN_CHECKED);
    }


}