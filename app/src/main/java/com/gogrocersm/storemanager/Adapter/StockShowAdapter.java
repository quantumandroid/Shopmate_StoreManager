package com.gogrocersm.storemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Dashboard.StockUpdate;
import com.gogrocersm.storemanager.Model.AlllProductModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.Session_management;
import com.gogrocersm.storemanager.util.StockUpdaterInterface;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class StockShowAdapter extends RecyclerView.Adapter<StockShowAdapter.ViewHolder> {

    private Context context;
    private List<AlllProductModel> list;
    private StockUpdaterInterface stockUpdaterInterface;
    private Session_management session_management;

    public StockShowAdapter(Context context, List<AlllProductModel> list, StockUpdaterInterface stockUpdaterInterface) {
        this.context = context;
        this.list = list;
        this.stockUpdaterInterface = stockUpdaterInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_stock_show_list_item, parent, false);
        session_management = new Session_management(parent.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final AlllProductModel movie = list.get(position);
//        String sign = MainActivity.currency_sign;
//        holder.product_Id.setText(movie.getCat_id());
        holder.product_name.setText(movie.getProduct_name());
        if (movie.getDescription()!=null && !movie.getDescription().equalsIgnoreCase("")&& !movie.getDescription().equalsIgnoreCase("null")){
            holder.product_desp.setVisibility(View.VISIBLE);
            holder.product_desp.setText(movie.getDescription());
        }else {
            holder.product_desp.setVisibility(View.GONE);
        }

//        holder.catogary_id.setText(movie.getMrp());
//        holder.increment.setText(movie.getIncrement());
        holder.price.setText("Price - " + session_management.getCurrency() + "" + movie.getPrice());
        holder.mrp.setText("Mrp - " + session_management.getCurrency() + "" + movie.getMrp());
        holder.qty.setText(movie.getQuantity() + " " + movie.getUnit());
        holder.stock_value.setText(movie.getStock());
//        holder.unit_value.setText();
//        holder.reward.setText(movie.getReward());
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL+movie.getVarient_image())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.productimage);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockUpdaterInterface.onStockUpdate(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, price, unit_value, qty, mrp, delete,product_desp,stock_value;
        public ImageView productimage;

        public ViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.name_product);
            product_desp = itemView.findViewById(R.id.product_desp);
            stock_value = itemView.findViewById(R.id.stock_value);
            price = itemView.findViewById(R.id.price_product);
//            unit_value = itemView.findViewById(R.id.unit_product);
            qty = itemView.findViewById(R.id.qty_product);
            productimage = itemView.findViewById(R.id.imageview_product);
            mrp = itemView.findViewById(R.id.mrp_product);
            delete = itemView.findViewById(R.id.delete);


        }
    }

}

