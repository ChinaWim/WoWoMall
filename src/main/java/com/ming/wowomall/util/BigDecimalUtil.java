package com.ming.wowomall.util;

import java.math.BigDecimal;

/**
 * @author m969130721@163.com
 * @date 18-8-31 下午9:58
 */
public class BigDecimalUtil {
    private BigDecimalUtil(){
    }

    public static BigDecimal add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    public static BigDecimal subtract(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal multiply(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }
    public static BigDecimal divide(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //除不尽情况　保留两位小数，四舍五入
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[] args) {
        System.out.println(multiply(1, 2));
    }



}
