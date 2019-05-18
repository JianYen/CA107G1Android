package com.yen.CA107G1.VO;

import java.sql.Timestamp;
import java.util.List;


import java.sql.Timestamp;
import java.util.List;


public class ShopOrdVO  implements java.io.Serializable{
	private String s_ord_no;
	private Integer s_status_no;
	private String mem_no;
	private Timestamp order_time;
	private Integer s_ord_total;
	private String s_ord_address;
	private Integer s_ord_mem_points;
	private List<CartVO> cartList;
	private int amount;


	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public ShopOrdVO() {
		super();
	}
	public ShopOrdVO(String s_ord_no, Integer s_status_no, String mem_no, Timestamp order_time, Integer s_ord_total,
					 String s_ord_address, Integer s_ord_mem_points, int amount, List<CartVO> cartList) {
		super();
		this.s_ord_no = s_ord_no;
		this.s_status_no = s_status_no;
		this.mem_no = mem_no;
		this.order_time = order_time;
		this.s_ord_total = s_ord_total;
		this.s_ord_address = s_ord_address;
		this.s_ord_mem_points = s_ord_mem_points;
		this.amount = amount;
		this.cartList = cartList;
	}
	public String getS_ord_no() {
		return s_ord_no;
	}
	public void setS_ord_no(String s_ord_no) {
		this.s_ord_no = s_ord_no;
	}
	public Integer getS_status_no() {
		return s_status_no;
	}
	public void setS_status_no(Integer s_status_no) {
		this.s_status_no = s_status_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public Timestamp getOrder_time() {
		return order_time;
	}
	public void setOrder_time(Timestamp order_time) {
		this.order_time = order_time;
	}
	public Integer getS_ord_total() {
		return s_ord_total;
	}
	public void setS_ord_total(Integer s_ord_total) {
		this.s_ord_total = s_ord_total;
	}
	public String getS_ord_address() {
		return s_ord_address;
	}
	public void setS_ord_address(String s_ord_address) {
		this.s_ord_address = s_ord_address;
	}
	public Integer getS_ord_mem_points() {
		return s_ord_mem_points;
	}
	public void setS_ord_mem_points(Integer s_ord_mem_points) {
		this.s_ord_mem_points = s_ord_mem_points;
	}
	public List<CartVO> getCartList() {
		return cartList;
	}
	public void setCartList(List<CartVO> cartList) {
		this.cartList = cartList;
	}



}
