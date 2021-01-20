package com.myshopmate.store.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.ApiExceptionUtil;
import com.myshopmate.store.Adapter.TransactionsAdapter;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Model.TransactionModel;
import com.myshopmate.store.Model.TransactionsResponse;
import com.myshopmate.store.NetworkConnectivity.ApiInterface;
import com.myshopmate.store.R;
import com.myshopmate.store.util.Session_management;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class EarningsActivity extends AppCompatActivity {

    Context context;
    private ArrayList<TransactionModel> transactions;
    private TransactionsAdapter transactionsAdapter;
    private RecyclerView rvTransactions;
    private TextView tvTotalBalance;
    private Session_management session_management;
    private TextView tvNoData;
    private ProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        context = this;
        session_management = new Session_management(context);
        tvTotalBalance = findViewById(R.id.tv_total_balance);
        rvTransactions = findViewById(R.id.rv_transactions);
        tvNoData = findViewById(R.id.tv_no_data);
        progressCircular = findViewById(R.id.progress_circular);

        transactions = new ArrayList<>();
        transactionsAdapter = new TransactionsAdapter(context,transactions);
        rvTransactions.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        rvTransactions.setAdapter(transactionsAdapter);
        getTransactions();
    }

    private void getTransactions() {
        ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
        Call<TransactionsResponse> transactionModelCall = apiInterface.getTransactions(session_management.getUserDetails().get(BaseURL.KEY_ID));
        transactionModelCall.enqueue(new Callback<TransactionsResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<TransactionsResponse> call, Response<TransactionsResponse> response) {
                progressCircular.setVisibility(View.INVISIBLE);
                if (response.body() == null) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                    rvTransactions.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                    rvTransactions.setVisibility(View.VISIBLE);
                    tvTotalBalance.setText("Pending Balance: ₹"+response.body().getTotalBalance());
                    transactions.addAll(response.body().getTransactions());
                    transactionsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TransactionsResponse> call, Throwable t) {
                progressCircular.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}