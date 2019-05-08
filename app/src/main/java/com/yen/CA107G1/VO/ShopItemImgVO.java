package com.yen.CA107G1.VO;

import java.io.Serializable;

public class ShopItemImgVO implements Serializable {
    private String s_item_image_no;
    private byte[] s_item_images;
    private String s_item_no;

    public String getS_item_image_no() {
        return s_item_image_no;
    }
    public void setS_item_image_no(String s_item_image_no) {
        this.s_item_image_no = s_item_image_no;
    }
    public byte[] getS_item_images() {
        return s_item_images;
    }
    public void setS_item_images(byte[] s_item_images) {
        this.s_item_images = s_item_images;
    }
    public String getS_item_no() {
        return s_item_no;
    }
    public void setS_item_no(String s_item_no) {
        this.s_item_no = s_item_no;
    }

}