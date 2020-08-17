package com.gogrocersm.storemanager.Model;

import java.util.List;

public class ListAssignAndUnassigned {
    private String viewType;
    private List<TodayOrderModel> todayOrderModels;
    private List<NextdayOrderModel> nextDayOrders;

    public ListAssignAndUnassigned(String viewType, List<TodayOrderModel> todayOrderModels, List<NextdayOrderModel> nextDayOrders) {
        this.viewType = viewType;
        this.todayOrderModels = todayOrderModels;
        this.nextDayOrders = nextDayOrders;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public List<TodayOrderModel> getTodayOrderModels() {
        return todayOrderModels;
    }

    public void setTodayOrderModels(List<TodayOrderModel> todayOrderModels) {
        this.todayOrderModels = todayOrderModels;
    }

    public List<NextdayOrderModel> getNextDayOrders() {
        return nextDayOrders;
    }

    public void setNextDayOrders(List<NextdayOrderModel> nextDayOrders) {
        this.nextDayOrders = nextDayOrders;
    }
}
