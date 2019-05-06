package com.yen.shoppingcar.VO;

import java.sql.Date;

public class PetVO implements java.io.Serializable{
    private String pet_no;
    private Integer pet_status;
    private String mem_no;
    private String pet_type;
    private String pet_breed;
    private String pet_name;
    private Double pet_weight;
    private java.sql.Date pet_birth;
    private byte[] pet_img;

    public String getPet_no() {
        return pet_no;
    }
    public void setPet_no(String pet_no) {
        this.pet_no = pet_no;
    }
    public Integer getPet_status() {
        return pet_status;
    }
    public void setPet_status(Integer pet_status) {
        this.pet_status = pet_status;
    }
    public String getMem_no() {
        return mem_no;
    }
    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }
    public String getPet_type() {
        return pet_type;
    }
    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }
    public String getPet_breed() {
        return pet_breed;
    }
    public void setPet_breed(String pet_breed) {
        this.pet_breed = pet_breed;
    }
    public String getPet_name() {
        return pet_name;
    }
    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }
    public Double getPet_weight() {
        return pet_weight;
    }
    public void setPet_weight(Double pet_weight) {
        this.pet_weight = pet_weight;
    }
    public Date getPet_birth() {
        return pet_birth;
    }
    public void setPet_birth(Date pet_birth) {
        this.pet_birth = pet_birth;
    }
//    public byte[] getPet_img() {
//        return pet_img;
//    }
//    public void setPet_img(byte[] pet_img) {
//        this.pet_img = pet_img;
//    }
}
