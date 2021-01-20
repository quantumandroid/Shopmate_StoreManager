package com.myshopmate.store.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopmate.store.Model.Select_Product_Model;
import com.myshopmate.store.R;
import com.myshopmate.store.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Select_Product_Adapter extends RecyclerView.Adapter<Select_Product_Adapter.ViewHolder> {

    private final List<Select_Product_Model> select_product_models;
    private final SelectProductListener selectProductListener;
    private final Context context;
    private int currentPosition;

    public Select_Product_Adapter(Context context, List<Select_Product_Model> selectProductModels) {
        this.select_product_models = selectProductModels;
        this.selectProductListener = (SelectProductListener) context;
        this.context = context;
        currentPosition = -1;
    }

    @Override
    public Select_Product_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_select_product, parent, false);
        return new Select_Product_Adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Select_Product_Adapter.ViewHolder holder, int position) {
        Select_Product_Model movie = select_product_models.get(position);

//        String sign = MainActivity.currency_sign;
        holder.product_name.setText(movie.getProduct_name());

        String price = movie.getPrice();
        if (price == null || price.isEmpty()) {
            holder.price.setText("");
        } else {
            holder.price.setText(movie.getPrice());
        }

        String variant = movie.getQuantity() + " " + movie.getUnit();
        holder.variant.setText(variant);

        holder.chkSelect.setOnCheckedChangeListener(null);

        holder.price.setEnabled(movie.isAdded());
        holder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.price.setEnabled(true);
//                    holder.price.requestFocus();
                    movie.setAdded(true);
                } else {
                    movie.setAdded(false);
//                    holder.price.setText("");
                    holder.price.setEnabled(false);
                    Utils.hideKeyboard((Activity)context,true);
                }
                movie.setChanged(isChecked != movie.isInitialState() || movie.isAdded() && movie.isPriceChanged());
                if (movie.isChanged()) {
                    select_product_models.set(position,movie);
                    selectProductListener.onSelectionChanged(isAllSelected(),position);
                } else if (getSelectionChangedProducts().size() == 0){
                    selectProductListener.onNothingChanged();
                }
            }
        });
        holder.chkSelect.setChecked(movie.isAdded());
        holder.price.setOnFocusChangeListener(null);
        holder.price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.price.setSelection(holder.price.getText().length());
                    currentPosition = position;
                } else {
                    currentPosition = -1;
                }
            }
        });

        holder.itemView.setOnLongClickListener(null);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
        holder.enableTextWatcher();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
        holder.disableTextWatcher();
    }

    @Override
    public int getItemCount() {
        return select_product_models.size();
    }

    public ArrayList<Select_Product_Model> getSelectedProducts() {
        ArrayList<Select_Product_Model> selectedProducts = new ArrayList<>();
        for (Select_Product_Model product : select_product_models) {
            if (product.isAdded()) selectedProducts.add(product);
        }
        return selectedProducts;
    }

    public ArrayList<Select_Product_Model> getSelectionChangedProducts() {
        ArrayList<Select_Product_Model> selectionChangedProducts = new ArrayList<>();
        for (Select_Product_Model product : select_product_models) {
            if (product.isChanged()) selectionChangedProducts.add(product);
        }
        return selectionChangedProducts;
    }

    public boolean isAllSelected() {
        return select_product_models.size() == getSelectedProducts().size();
    }

    public interface SelectProductListener {
        void onSelectionChanged(boolean allSelected, int position);
        void onNothingChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, variant;
        private final EditText price;
        private final CheckBox chkSelect;
        private final TextWatcher textWatcher;

        public ViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price_product);
            variant = itemView.findViewById(R.id.tv_variant);
            chkSelect = itemView.findViewById(R.id.chk_select);

            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString().trim();
                    if (!str.isEmpty() && currentPosition > -1) {
                        Select_Product_Model product = select_product_models.get(currentPosition);
                        product.setPrice(str);
                        product.setPriceChanged(true);
                        product.setChanged(true);
                        select_product_models.set(currentPosition, product);
                        selectProductListener.onSelectionChanged(isAllSelected(),currentPosition);
//                        notifyItemChanged(currentPosition);
                    }
                }
            };
        }
        void enableTextWatcher() {
            price.addTextChangedListener(textWatcher);
        }

        void disableTextWatcher() {
            price.removeTextChangedListener(textWatcher);
        }
    }

}
