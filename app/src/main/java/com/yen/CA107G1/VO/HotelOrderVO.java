package com.yen.CA107G1.VO;

import java.sql.Date;
import java.sql.Timestamp;

public class HotelOrderVO implements java.io.Serializable{
    private String h_ord_no;
    private Integer h_ord_status_no;
    private String h_room_no;
    private String mem_no;
    private Date h_ord_date_start;
    private Date h_ord_date_end;
    private Timestamp h_ord_time;
    private Integer h_ord_has_beauty;
    private String pet_no;
    private Integer h_ord_pickup;
    private String h_ord_address;
    private Integer h_ord_mem_points;
    private Integer h_ord_total;

    public HotelOrderVO() {

    }
    public String getH_ord_no() {
        return h_ord_no;
    }
    public void setH_ord_no(String h_ord_no) {
        this.h_ord_no = h_ord_no;
    }
    public Integer getH_ord_status_no() {
        return h_ord_status_no;
    }
    public void setH_ord_status_no(Integer h_ord_status_no) {
        this.h_ord_status_no = h_ord_status_no;
    }
    public String getH_room_no() {
        return h_room_no;
    }
    public void setH_room_no(String h_room_no) {
        this.h_room_no = h_room_no;
    }
    public String getMem_no() {
        return mem_no;
    }
    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }
    public Date getH_ord_date_start() {
        return h_ord_date_start;
    }
    public void setH_ord_date_start(Date h_ord_date_start) {
        this.h_ord_date_start = h_ord_date_start;
    }
    public Date getH_ord_date_end() {
        return h_ord_date_end;
    }
    public void setH_ord_date_end(Date h_ord_date_end) {
        this.h_ord_date_end = h_ord_date_end;
    }
    public Timestamp getH_ord_time() {
        return h_ord_time;
    }
    public void setH_ord_time(Timestamp h_ord_time) {
        this.h_ord_time = h_ord_time;
    }
    public Integer getH_ord_has_beauty() {
        return h_ord_has_beauty;
    }
    public void setH_ord_has_beauty(Integer h_ord_has_beauty) {
        this.h_ord_has_beauty = h_ord_has_beauty;
    }
    public String getPet_no() {
        return pet_no;
    }
    public void setPet_no(String pet_no) {
        this.pet_no = pet_no;
    }
    public Integer getH_ord_pickup() {
        return h_ord_pickup;
    }
    public void setH_ord_pickup(Integer h_ord_pickup) {
        this.h_ord_pickup = h_ord_pickup;
    }
    public String getH_ord_address() {
        return h_ord_address;
    }
    public void setH_ord_address(String h_ord_address) {
        this.h_ord_address = h_ord_address;
    }
    public Integer getH_ord_mem_points() {
        return h_ord_mem_points;
    }
    public void setH_ord_mem_points(Integer h_ord_mem_points) {
        this.h_ord_mem_points = h_ord_mem_points;
    }
    public Integer getH_ord_total() {
        return h_ord_total;
    }
    public void setH_ord_total(Integer h_ord_total) {
        this.h_ord_total = h_ord_total;
    }
}