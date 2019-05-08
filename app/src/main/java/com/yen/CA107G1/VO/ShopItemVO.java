package com.yen.CA107G1.VO;

import java.io.Serializable;

public class ShopItemVO implements Serializable {
    private String s_item_no;
    private Integer s_item_status;
    private String s_item_text;
    private Integer s_item_type_no;
    private String s_item_type_name;
    private String s_item_describe;
    private Integer s_item_price;
    private Integer s_msg_count;
    private Integer s_msg_total;
    private Integer s_item_count;
    private Integer cart_count;

    public Integer getCart_count() {
        return cart_count;
    }

    public void setCart_count(Integer cart_count) {
        this.cart_count = cart_count;
    }

    public ShopItemVO() {
        super();
    }

    public String getS_item_no() {
        return s_item_no;
    }

    public void setS_item_no(String s_item_no) {
        this.s_item_no = s_item_no;
    }

    public Integer getS_item_status() {
        return s_item_status;
    }

    public void setS_item_status(Integer s_item_status) {
        this.s_item_status = s_item_status;
    }

    public String getS_item_text() {
        return s_item_text;
    }

    public void setS_item_text(String s_item_text) {
        this.s_item_text = s_item_text;
    }

    public Integer getS_item_type_no() {
        return s_item_type_no;
    }

    public void setS_item_type_no(Integer s_item_type_no) {
        this.s_item_type_no = s_item_type_no;
    }

    public String getS_item_type_name() {
        return s_item_type_name;
    }

    public void setS_item_type_name(String s_item_type_name) {
        this.s_item_type_name = s_item_type_name;
    }

    public String getS_item_describe() {
        return s_item_describe;
    }

    public void setS_item_describe(String s_item_describe) {
        this.s_item_describe = s_item_describe;
    }

    public Integer getS_item_price() {
        return s_item_price;
    }

    public void setS_item_price(Integer s_item_price) {
        this.s_item_price = s_item_price;
    }

    public Integer getS_msg_count() {
        return s_msg_count;
    }

    public void setS_msg_count(Integer s_msg_count) {
        this.s_msg_count = s_msg_count;
    }

    public Integer getS_msg_total() {
        return s_msg_total;
    }

    public void setS_msg_total(Integer s_msg_total) {
        this.s_msg_total = s_msg_total;
    }

    public Integer getS_item_count() {
        return s_item_count;
    }

    public void setS_item_count(Integer s_item_count) {
        this.s_item_count = s_item_count;
    }
}