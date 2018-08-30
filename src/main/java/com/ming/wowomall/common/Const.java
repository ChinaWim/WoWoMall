package com.ming.wowomall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 常量类
 *
 * @author m969130721@163.com
 * @date 18-5-27 下午6:43
 */
public class Const {

    public static final String CURRENT_USER = "current_user";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }


    public enum ProductStatusEnum {
        ON_SALE(1,"在线");
        private Integer code;
        private String value;

        ProductStatusEnum(Integer code,String value){
            this.code = code;
            this.value = value;
        }
        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

}
