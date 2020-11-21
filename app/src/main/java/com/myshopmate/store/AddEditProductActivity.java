package com.myshopmate.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Model.NewAllProductModel;


public class AddEditProductActivity extends AppCompatActivity {

    AppCompatImageView imgProfile;
    TextInputEditText etProductName, etQty, etPrice, etMRP, etUnit, etDescription;
    Spinner spCategory;
    FloatingActionButton selectImage;
    Button btnSubmit;
    NewAllProductModel product;
    boolean isAdd = true;
    Context context;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        context = this;

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvTitle = findViewById(R.id.tv_title_add_product);
        imgProfile = findViewById(R.id.imgProfile);
        etProductName = findViewById(R.id.et_product_name);
        etQty = findViewById(R.id.et_qty);
        etPrice = findViewById(R.id.et_price);
        etMRP = findViewById(R.id.et_mrp);
        etUnit = findViewById(R.id.et_unit);
        etDescription = findViewById(R.id.et_description);
        selectImage = findViewById(R.id.fab_add_photo);
        spCategory = findViewById(R.id.sp_category);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdd) {
                    addProduct();
                } else {
                    updateProduct();
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product = (NewAllProductModel) bundle.get("product");
            isAdd = false;
            setData();
            tvTitle.setText("Edit Product");
        }
    }

    private void updateProduct() {

    }

    private void addProduct() {

    }

    private void setData() {
        etProductName.setText(product.getProduct_name());
        etQty.setText(product.getQuantity());
        etUnit.setText(product.getUnit());
        etMRP.setText(product.getMrp());
        etDescription.setText(product.getDescription());
        etPrice.setText(product.getPrice());
        Glide.with(context)
                .load(BaseURL.BASE_URL + product.getVarient_image())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imgProfile);
    }

    public void enableViews() {
        etProductName.setEnabled(true);
        etQty.setEnabled(true);
        etUnit.setEnabled(true);
        etMRP.setEnabled(true);
        etDescription.setEnabled(true);
        etPrice.setEnabled(true);
        spCategory.setEnabled(true);
        selectImage.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    public void disableViews() {
        spCategory.setEnabled(false);
        etProductName.setEnabled(false);
        etQty.setEnabled(false);
        etUnit.setEnabled(false);
        etMRP.setEnabled(false);
        etDescription.setEnabled(false);
        etPrice.setEnabled(false);
        selectImage.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
    }

    public void pickProfileImage(View view) {
        ImagePicker.Companion.with(this)
                //.crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(512, 512)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = null;
            if (data != null) {
                fileUri = data.getData();
                imgProfile.setImageURI(fileUri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}