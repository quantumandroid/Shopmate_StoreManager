package com.gogrocersm.storemanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gogrocersm.storemanager.Model.BluetoothModelClass;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.BluetoothClick;

import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.ViewHolder> {

    List<BluetoothModelClass> modelClasses;
    private BluetoothClick bluetoothClick;

    public BluetoothListAdapter(List<BluetoothModelClass> modelClasses, BluetoothClick bluetoothClick) {
        this.modelClasses = modelClasses;
        this.bluetoothClick = bluetoothClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothModelClass bluetoothModelClass = modelClasses.get(position);
        holder.name.setText(bluetoothModelClass.getName());
        holder.macNumber.setText(bluetoothModelClass.getMacNumber());
        holder.itemView.setOnClickListener(v -> bluetoothClick.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView macNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.device_name);
            macNumber = itemView.findViewById(R.id.macAddress);
        }
    }
}
