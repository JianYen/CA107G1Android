package com.yen.CA107G1.VO;

public class NewsVO implements java.io.Serializable{
    private Integer news_no;
    private String news_text;

    public NewsVO() {

    }

    public Integer getNews_no() {
        return news_no;
    }

    public void setNews_no(Integer news_no) {
        this.news_no = news_no;
    }

    public String getNews_text() {
        return news_text;
    }

    public void setNews_text(String news_text) {
        this.news_text = news_text;
    }
}

