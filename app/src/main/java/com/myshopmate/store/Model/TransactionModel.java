package com.myshopmate.store.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionModel {
    @SerializedName("created_at")
    @Expose
    private String transactionDate;
    @SerializedName("transaction_id")
    @Expose
    private String transactionID;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("pending_due")
    @Expose
    private String pendingAmount;
    @SerializedName("transaction_mode")
    @Expose
    private String paymentMode;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
