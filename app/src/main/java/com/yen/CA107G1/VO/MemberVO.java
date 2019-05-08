package com.yen.CA107G1.VO;

import java.io.Serializable;

public class MemberVO implements Serializable {
    private String mem_no;
    private Integer mem_status;
    private Integer mem_points;
    private String mem_account;
    private String mem_password;
    private String mem_name;
    private String mem_email;
    private String mem_phone_no;
    private String mem_postnum;
    private String mem_address;
    private Integer mem_gender;
    private byte[] mem_img;

    public MemberVO() {

    }

    public String getMem_no() {
        return mem_no;
    }
    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }
    public Integer getMem_status() {
        return mem_status;
    }
    public void setMem_status(Integer mem_status) {
        this.mem_status = mem_status;
    }
    public Integer getMem_points() {
        return mem_points;
    }
    public void setMem_points(Integer mem_points) {
        this.mem_points = mem_points;
    }
    public String getMem_account() {
        return mem_account;
    }
    public void setMem_account(String mem_account) {
        this.mem_account = mem_account;
    }
    public String getMem_password() {
        return mem_password;
    }
    public void setMem_password(String mem_password) {
        this.mem_password = mem_password;
    }
    public String getMem_name() {
        return mem_name;
    }
    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }
    public String getMem_email() {
        return mem_email;
    }
    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }
    public String getMem_phone_no() {
        return mem_phone_no;
    }
    public void setMem_phone_no(String mem_phone_no) {
        this.mem_phone_no = mem_phone_no;
    }
    public String getMem_postnum() {
        return mem_postnum;
    }
    public void setMem_postnum(String mem_postnum) {
        this.mem_postnum = mem_postnum;
    }
    public String getMem_address() {
        return mem_address;
    }
    public void setMem_address(String mem_address) {
        this.mem_address = mem_address;
    }
    public Integer getMem_gender() {
        return mem_gender;
    }
    public void setMem_gender(Integer mem_gender) {
        this.mem_gender = mem_gender;
    }
    public byte[] getMem_img() {
        return mem_img;
    }
    public void setMem_img(byte[] mem_img) {
        this.mem_img = mem_img;
    }
}
