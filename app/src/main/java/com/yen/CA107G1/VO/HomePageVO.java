package com.yen.CA107G1.VO;

public class HomePageVO {

    private int logo;
    private String name;

    public HomePageVO() {
    }

    public HomePageVO(int logo, String name) {
        this.logo = logo;
        this.name = name;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
