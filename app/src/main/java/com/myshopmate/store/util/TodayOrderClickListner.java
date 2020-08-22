package com.myshopmate.store.util;

public interface TodayOrderClickListner {
    void ClickGetBoys(int position,String viewType);

    void assignClickBoys(int position,String viewType);

    void forOrderTodayClick(int position, String viewType);
    void forOrderNextClick(int position);
}
