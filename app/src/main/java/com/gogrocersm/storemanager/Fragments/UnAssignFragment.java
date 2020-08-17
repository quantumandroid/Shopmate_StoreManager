package com.gogrocersm.storemanager.Fragments;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UnAssignFragment extends RecyclerView.Adapter<UnAssignFragment.MySubView> {

    @androidx.annotation.NonNull
    @Override
    public MySubView onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MySubView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MySubView extends RecyclerView.ViewHolder {
        public MySubView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
