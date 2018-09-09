package com.ming.wowomall.vo;

import com.ming.wowomall.pojo.OrderItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author m969130721@163.com
 * @date 18-9-8 下午3:27
 */
public class OrderProductVO implements Serializable {

    private List<OrderItemVO> orderItemVoList;

    private String imageHost;

    private BigDecimal productTotalPrice;

    public List<OrderItemVO> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVO> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    @Override
    public String toString() {
        return "OrderProductVO{" +
                "orderItemVoList=" + orderItemVoList +
                ", imageHost='" + imageHost + '\'' +
                ", productTotalPrice=" + productTotalPrice +
                '}';
    }
}
