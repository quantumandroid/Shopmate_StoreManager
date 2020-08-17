package com.gogrocersm.storemanager.Model;


import java.io.Serializable;

public class My_order_detail_model implements Serializable {

    private String store_order_id;
    private String product_name;
    private String varient_image;
    private String quantity;
    private String varient_id;
    private String qty;
    private String unit;
    private String price;
    private String order_cart_id;
    private String total_mrp;
    private String order_date;
    private String store_approval;

    public String getStore_order_id() {
        return store_order_id;
    }

    public void setStore_order_id(String store_order_id) {
        this.store_order_id = store_order_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_cart_id() {
        return order_cart_id;
    }

    public void setOrder_cart_id(String order_cart_id) {
        this.order_cart_id = order_cart_id;
    }

    public String getTotal_mrp() {
        return total_mrp;
    }

    public void setTotal_mrp(String total_mrp) {
        this.total_mrp = total_mrp;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStore_approval() {
        return store_approval;
    }

    public void setStore_approval(String store_approval) {
        this.store_approval = store_approval;
    }
}
