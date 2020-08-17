package com.gogrocersm.storemanager.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class TodayOrderModel implements Serializable {

    private String user_address;
    private String cart_id;
    private String user_name;
    private String user_phone;
    private String remaining_price;
    private String order_price;
    private String delivery_boy_name;
    private String delivery_boy_phone;
    private String delivery_date;
    private String time_slot;
    private String payment_mode;
    private String order_status;
    private String customer_phone;
    private ArrayList<My_order_detail_model> order_details;

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getRemaining_price() {
        return remaining_price;
    }

    public void setRemaining_price(String remaining_price) {
        this.remaining_price = remaining_price;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getDelivery_boy_name() {
        return delivery_boy_name;
    }

    public void setDelivery_boy_name(String delivery_boy_name) {
        this.delivery_boy_name = delivery_boy_name;
    }

    public String getDelivery_boy_phone() {
        return delivery_boy_phone;
    }

    public void setDelivery_boy_phone(String delivery_boy_phone) {
        this.delivery_boy_phone = delivery_boy_phone;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public ArrayList<My_order_detail_model> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(ArrayList<My_order_detail_model> order_details) {
        this.order_details = order_details;
    }

    //    public String cart_id;
//    public String user_name;
//    public String user_phone;
//    public String remaining_price;
//    public String order_price;
//    public String delivery_boy_name;
//    public String delivery_boy_phone;
//    public String delivery_date;
//    public String time_slot;
//    public String payment_mode;
//    public String order_status;
//    public String customer_phone;
//    public String product_name;
//    public String price;
//    public String mrp;
//    public String unit;
//    public String quantity;
//    public String varient_image;
//    public String description;
//    public String varient_id;
//    public String store_order_id;
//    public String qty;
//    public String user_address;
//    JSONArray jsonArray = new JSONArray();
//
//    public String getCart_id() {
//        return cart_id;
//    }
//
//    public void setCart_id(String cart_id) {
//        this.cart_id = cart_id;
//    }
//
//    public String getUser_name() {
//        return user_name;
//    }
//
//    public void setUser_name(String user_name) {
//        this.user_name = user_name;
//    }
//
//    public String getUser_phone() {
//        return user_phone;
//    }
//
//    public void setUser_phone(String user_phone) {
//        this.user_phone = user_phone;
//    }
//
//    public String getRemaining_price() {
//        return remaining_price;
//    }
//
//    public void setRemaining_price(String remaining_price) {
//        this.remaining_price = remaining_price;
//    }
//
//    public String getOrder_price() {
//        return order_price;
//    }
//
//    public void setOrder_price(String order_price) {
//        this.order_price = order_price;
//    }
//
//    public String getDelivery_boy_name() {
//        return delivery_boy_name;
//    }
//
//    public void setDelivery_boy_name(String delivery_boy_name) {
//        this.delivery_boy_name = delivery_boy_name;
//    }
//
//    public String getDelivery_boy_phone() {
//        return delivery_boy_phone;
//    }
//
//    public void setDelivery_boy_phone(String delivery_boy_phone) {
//        this.delivery_boy_phone = delivery_boy_phone;
//    }
//
//    public String getDelivery_date() {
//        return delivery_date;
//    }
//
//    public void setDelivery_date(String delivery_date) {
//        this.delivery_date = delivery_date;
//    }
//
//    public String getTime_slot() {
//        return time_slot;
//    }
//
//    public void setTime_slot(String time_slot) {
//        this.time_slot = time_slot;
//    }
//
//    public String getPayment_mode() {
//        return payment_mode;
//    }
//
//    public void setPayment_mode(String payment_mode) {
//        this.payment_mode = payment_mode;
//    }
//
//    public String getOrder_status() {
//        return order_status;
//    }
//
//    public void setOrder_status(String order_status) {
//        this.order_status = order_status;
//    }
//
//    public String getCustomer_phone() {
//        return customer_phone;
//    }
//
//    public void setCustomer_phone(String customer_phone) {
//        this.customer_phone = customer_phone;
//    }
//
//    public String getProduct_name() {
//        return product_name;
//    }
//
//    public void setProduct_name(String product_name) {
//        this.product_name = product_name;
//    }
//
//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public String getMrp() {
//        return mrp;
//    }
//
//    public void setMrp(String mrp) {
//        this.mrp = mrp;
//    }
//
//    public String getUnit() {
//        return unit;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getVarient_image() {
//        return varient_image;
//    }
//
//    public void setVarient_image(String varient_image) {
//        this.varient_image = varient_image;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getVarient_id() {
//        return varient_id;
//    }
//
//    public void setVarient_id(String varient_id) {
//        this.varient_id = varient_id;
//    }
//
//    public String getStore_order_id() {
//        return store_order_id;
//    }
//
//    public void setStore_order_id(String store_order_id) {
//        this.store_order_id = store_order_id;
//    }
//
//    public String getQty() {
//        return qty;
//    }
//
//    public void setQty(String qty) {
//        this.qty = qty;
//    }
//
//    public JSONArray getJsonArray() {
//        return jsonArray;
//    }
//
//    public void setJsonArray(JSONArray jsonArray) {
//        this.jsonArray = jsonArray;
//    }
//
//    public String getUser_address() {
//        return user_address;
//    }
//
//    public void setUser_address(String user_address) {
//        this.user_address = user_address;
//    }
}
