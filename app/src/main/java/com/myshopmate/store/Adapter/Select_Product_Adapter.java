package com.myshopmate.store.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.MainActivity;
import com.myshopmate.store.Model.AlllProductModel;
import com.myshopmate.store.Model.Select_Product_Model;
import com.myshopmate.store.R;

import java.util.List;

public class Select_Product_Adapter extends RecyclerView.Adapter<Select_Product_Adapter.ViewHolder> {

    private Context context;
    private List<Select_Product_Model> select_product_models;


    public Select_Product_Adapter(List<Select_Product_Model> selectProductModels) {
        this.select_product_models = selectProductModels;

    }

    @Override
    public Select_Product_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_select_product, parent, false);
        context = v.getContext();
        return new Select_Product_Adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Select_Product_Adapter.ViewHolder holder, int position) {
        final Select_Product_Model movie = select_product_models.get(position);

        String sign = MainActivity.currency_sign;
        holder.product_name.setText(movie.getProduct_name());

        holder.price.setText(sign + movie.getPrice());
        holder.unit_value.setText(movie.getUnit());
        holder.qty.setText(movie.getQuantity());
        holder.mrp.setText(movie.getMrp());
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + movie.getVarient_image()
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.productimage);
    }

    @Override
    public int getItemCount() {
        return select_product_models.size();


    }

    public interface AllProductsAdapterListener {
        void onContactSelected(AlllProductModel contact);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, price, unit_value, qty, mrp;
        public ImageView productimage;

        public ViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price_product);
            unit_value = itemView.findViewById(R.id.unit_product);
            qty = itemView.findViewById(R.id.qty_product);
            productimage = itemView.findViewById(R.id.imageview_product);
            mrp = itemView.findViewById(R.id.mrp_product);


        }
    }

}
