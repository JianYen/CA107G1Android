package com.yen.shoppingcar.VO;

import java.io.Serializable;

public class HotelRoomTypeVO implements Serializable {
    private String h_roomtype_no;
    private String h_roomtype_text;
    private byte[] h_roomtype_images;
    private Integer h_roomtype_price;
    private String h_roomtype_desc;
    private Integer h_roomtype_status;
    private String h_sale_no;

    public HotelRoomTypeVO() {

    }
    public String getH_roomtype_no() {
        return h_roomtype_no;
    }
    public void setH_roomtype_no(String h_roomtype_no) {
        this.h_roomtype_no = h_roomtype_no;
    }
    public String getH_roomtype_text() {
        return h_roomtype_text;
    }
    public void setH_roomtype_text(String h_roomtype_text) {
        this.h_roomtype_text = h_roomtype_text;
    }
    public byte[] getH_roomtype_images() {
        return h_roomtype_images;
    }
    public void setH_roomtype_images(byte[] h_roomtype_images) {
        this.h_roomtype_images = h_roomtype_images;
    }
    public Integer getH_roomtype_price() {
        return h_roomtype_price;
    }
    public void setH_roomtype_price(Integer h_roomtype_price) {
        this.h_roomtype_price = h_roomtype_price;
    }
    public String getH_roomtype_desc() {
        return h_roomtype_desc;
    }
    public void setH_roomtype_desc(String h_roomtype_desc) {
        this.h_roomtype_desc = h_roomtype_desc;
    }
    public Integer getH_roomtype_status() {
        return h_roomtype_status;
    }
    public void setH_roomtype_status(Integer h_roomtype_status) {
        this.h_roomtype_status = h_roomtype_status;
    }
    public String getH_sale_no() {
        return h_sale_no;
    }
    public void setH_sale_no(String h_sale_no) {
        this.h_sale_no = h_sale_no;
    }
}