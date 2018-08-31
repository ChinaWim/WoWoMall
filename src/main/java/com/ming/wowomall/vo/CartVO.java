package com.ming.wowomall.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author m969130721@163.com
 * @date 18-8-31 下午9:22
 */
public class CartVO implements Serializable {

    private List<CartProductVO> cartProductVoList;

    private BigDecimal cartTotalPrice;

    private Boolean allChecked;

    private String imageHost;

    public List<CartProductVO> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVO> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    @Override
    public String toString() {
        return "CartVO{" +
                "cartProductVoList=" + cartProductVoList +
                ", cartTotalPrice=" + cartTotalPrice +
                ", allChecked=" + allChecked +
                ", imageHost='" + imageHost + '\'' +
                '}';
    }
}
