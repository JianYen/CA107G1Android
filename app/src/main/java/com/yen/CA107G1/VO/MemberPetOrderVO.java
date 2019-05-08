package com.yen.CA107G1.VO;

import java.io.Serializable;

public class MemberPetOrderVO implements Serializable {

    private String h_ord_no;
    private String mem_name;
    private String pet_name;
    private String h_ord_address;
    private String pet_no;

    public MemberPetOrderVO() {
        super();
    }

    public MemberPetOrderVO(String h_ord_no, String mem_name, String pet_name, String h_ord_address, String pet_no) {
        super();
        this.h_ord_no = h_ord_no;
        this.mem_name = mem_name;
        this.pet_name = pet_name;
        this.h_ord_address = h_ord_address;
        this.pet_no = pet_no;
    }

    public String getH_ord_no() {
        return h_ord_no;
    }

    public void setH_ord_no(String h_ord_no) {
        this.h_ord_no = h_ord_no;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getH_ord_address() {
        return h_ord_address;
    }

    public void setH_ord_address(String h_ord_address) {
        this.h_ord_address = h_ord_address;
    }

    public String getPet_no() {
        return pet_no;
    }

    public void setPet_no(String pet_no) {
        this.pet_no = pet_no;
    }
}
