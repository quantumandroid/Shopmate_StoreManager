package com.myshopmate.store.Model;

import java.util.ArrayList;

public class InvoiceModel {

    private String status;
    private String message;
    private String Name;
    private String number;
    private String address;
    private String city;
    private String pincode;
    private String paid_by_wallet;
    private String coupon_discount;
    private String price_to_pay;
    private String total_price;
    private String price_without_delivery;
    private String delivery_charge;
    private String invoice_no;
    private ArrayList<InvoiceOrderDetails> order_details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPaid_by_wallet() {
        return paid_by_wallet;
    }

    public void setPaid_by_wallet(String paid_by_wallet) {
        this.paid_by_wallet = paid_by_wallet;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getPrice_to_pay() {
        return price_to_pay;
    }

    public void setPrice_to_pay(String price_to_pay) {
        this.price_to_pay = price_to_pay;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getPrice_without_delivery() {
        return price_without_delivery;
    }

    public void setPrice_without_delivery(String price_without_delivery) {
        this.price_without_delivery = price_without_delivery;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public ArrayList<InvoiceOrderDetails> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(ArrayList<InvoiceOrderDetails> order_details) {
        this.order_details = order_details;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }
}
