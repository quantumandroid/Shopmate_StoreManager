package com.myshopmate.store.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.store.Model.TransactionModel;
import com.myshopmate.store.R;
import com.myshopmate.store.util.Utils;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    Context context;
    ArrayList<TransactionModel> transactions;

    public TransactionsAdapter(Context context, ArrayList<TransactionModel> transactions) {
        this.context = context;
        this.transactions = transactions;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionModel transactionModel = transactions.get(position);
        String dateStr = Utils.formatDateTimeString(transactionModel.getTransactionDate(),"yyyy-MM-dd HH:mm:ss","dd MMM, yyyy");
        holder.tvDate.setText(dateStr);
        holder.tvTransactionID.setText(transactionModel.getTransactionID());
        holder.tvMode.setText(transactionModel.getPaymentMode());
        holder.tvReceivedAmount.setText(transactionModel.getPaidAmount());
        holder.tvPendingAmount.setText(transactionModel.getPendingAmount());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate, tvTransactionID, tvMode, tvReceivedAmount, tvPendingAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTransactionID = itemView.findViewById(R.id.tv_transaction_id);
            tvMode = itemView.findViewById(R.id.tv_payment_mode);
            tvReceivedAmount = itemView.findViewById(R.id.tv_amount_received);
            tvPendingAmount = itemView.findViewById(R.id.tv_amount_pending);
        }
    }
}
