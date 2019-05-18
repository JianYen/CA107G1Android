package com.yen.CA107G1.VO;


public class CartVO extends ShopItemVO {
	private int quantity;

	public CartVO(String s_item_no, Integer s_item_status, String s_item_text, Integer s_item_type_no,
			String s_item_type_name, String s_item_describe, Integer s_item_price, Integer s_msg_count,
			Integer s_msg_total, Integer s_item_count, int quantity) {
		super(s_item_no, s_item_status, s_item_text, s_item_type_no, s_item_type_name, s_item_describe, s_item_price,
				s_msg_count, s_msg_total, s_item_count);
		this.quantity = quantity;

	}

	public CartVO(ShopItemVO shopItemVO, int quantity) {
		this(shopItemVO.getS_item_no(), shopItemVO.getS_item_status(), shopItemVO.getS_item_text(),
				shopItemVO.getS_item_type_no(), shopItemVO.getS_item_type_name(), shopItemVO.getS_item_describe(),
				shopItemVO.getS_item_price(), shopItemVO.getS_msg_count(), shopItemVO.getS_msg_total(),
				shopItemVO.getS_item_count(), quantity);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
