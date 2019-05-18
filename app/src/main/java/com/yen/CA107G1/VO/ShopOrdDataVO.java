package com.yen.CA107G1.VO;

import java.sql.Timestamp;

public class ShopOrdDataVO implements java.io.Serializable{
	private String s_ord_no;
	private String s_item_no;
	private Integer s_ord_count;
	private Integer s_ord_price;
	
	
	
	public ShopOrdDataVO() {
		super();
	}
	public ShopOrdDataVO(String s_ord_no, String s_item_no, Integer s_ord_count, Integer s_ord_price) {
		super();
		this.s_ord_no = s_ord_no;
		this.s_item_no = s_item_no;
		this.s_ord_count = s_ord_count;
		this.s_ord_price = s_ord_price;
	}
	public String getS_ord_no() {
		return s_ord_no;
	}
	public void setS_ord_no(String s_ord_no) {
		this.s_ord_no = s_ord_no;
	}
	public String getS_item_no() {
		return s_item_no;
	}
	public void setS_item_no(String s_item_no) {
		this.s_item_no = s_item_no;
	}
	public Integer getS_ord_count() {
		return s_ord_count;
	}
	public void setS_ord_count(Integer s_ord_count) {
		this.s_ord_count = s_ord_count;
	}
	public Integer getS_ord_price() {
		return s_ord_price;
	}
	public void setS_ord_price(Integer s_ord_price) {
		this.s_ord_price = s_ord_price;
	}

}
