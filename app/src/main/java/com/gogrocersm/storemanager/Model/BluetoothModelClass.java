package com.gogrocersm.storemanager.Model;

public class BluetoothModelClass {
    private String name;
    private String macNumber;

    public BluetoothModelClass(String name, String macNumber) {
        this.name = name;
        this.macNumber = macNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacNumber() {
        return macNumber;
    }

    public void setMacNumber(String macNumber) {
        this.macNumber = macNumber;
    }
}
