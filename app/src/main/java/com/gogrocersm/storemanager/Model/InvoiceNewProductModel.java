package com.gogrocersm.storemanager.Model;

import java.util.List;

public class InvoiceNewProductModel {
    private List<String> productName;
    private String productDescp;

    public InvoiceNewProductModel(List<String> productName, String productDescp) {
        this.productName = productName;
        this.productDescp = productDescp;
    }

    public List<String> getProductName() {
        return productName;
    }

    public void setProductName(List<String> productName) {
        this.productName = productName;
    }

    public String getProductDescp() {
        return productDescp;
    }

    public void setProductDescp(String productDescp) {
        this.productDescp = productDescp;
    }

    @Override
    public String toString() {
        return ""+productDescp;
    }
}
