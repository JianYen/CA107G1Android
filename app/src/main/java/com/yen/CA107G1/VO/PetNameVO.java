package com.yen.CA107G1.VO;

import java.io.Serializable;

public class PetNameVO implements Serializable {
    private String pet_no;
    private String pet_name;

    public PetNameVO() {
        super();
    }

    public PetNameVO(String pet_no, String pet_name) {
        super();
        this.pet_no = pet_no;
        this.pet_name = pet_name;
    }

    public String getPet_no() {
        return pet_no;
    }

    public void setPet_no(String pet_no) {
        this.pet_no = pet_no;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

}
