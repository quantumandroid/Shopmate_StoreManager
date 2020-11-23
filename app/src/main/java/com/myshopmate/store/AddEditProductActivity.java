package com.myshopmate.store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.myshopmate.store.Adapter.CategoriesAdapter;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Model.Category;
import com.myshopmate.store.Model.NewAllProductModel;
import com.myshopmate.store.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddEditProductActivity extends AppCompatActivity {

    AppCompatImageView imgProfile;
    TextInputEditText etProductName, etQty, etPrice, etMRP, etUnit, etDescription;
    TextInputEditText etQtyVariant, etPriceVariant, etMRPVariant, etUnitVariant, etDescriptionVariant;
    Spinner spCategory;
    FloatingActionButton selectImage;
    Button btnSubmit;//,btnAddVariants;
    NewAllProductModel product;
    boolean isAdd = true, isProduct = false;
    Context context;
    TextView tvTitle;
    private ProgressDialog progressDialog;
    ArrayList<Category> categories;
    private  CategoriesAdapter categoriesAdapter;
    private String selectedCatID = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);
        context = this;

        categories = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
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
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCatID = categories.get(position).getCat_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdd) {
                    if (isProduct) {
                        addProduct();
                    } else {
                        addVariant();
                    }
                } else {
                    if (isProduct) {
                        updateProduct();
                    } else {
                        updateVariant();
                    }
                }
            }
        });
        /*btnAddVariants = findViewById(R.id.btn_add_variants);
        btnAddVariants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddVariantDialog();
            }
        });*/

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product = (NewAllProductModel) bundle.get("product");
            isAdd = false;
            setData();
            tvTitle.setText("Edit Product");
        }
        getCategories();
    }

    private void showAddVariantDialog() {
        if (!isAdd) {
            etQtyVariant.setText(product.getQuantity());
            etUnitVariant.setText(product.getUnit());
            etMRPVariant.setText(product.getMrp());
            etDescriptionVariant.setText(product.getDescription());
            etPriceVariant.setText(product.getPrice());
        }
    }

    private void addProduct() {

    }

    private void addVariant() {

    }

    private void updateVariant() {

    }

    private void updateProduct() {

    }

    @SuppressLint("SetTextI18n")
    private void setUpVariant() {
        isProduct = true;
        tvTitle.setText("Add Variant");
        disableProductDetails();
    }

    @SuppressLint("SetTextI18n")
    private void resetData() {
        enableProductDetails();
        isProduct = false;
        if (isAdd) {
            tvTitle.setText("Add Product");
        } else {
            tvTitle.setText("Edit Product");
        }
        etProductName.setText("");
        etQty.setText("");
        etUnit.setText("");
        etMRP.setText("");
        etDescription.setText("");
        etPrice.setText("");
        imgProfile.setImageBitmap(null);

    }

    private void getCategories() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.getCategories, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {
                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        ArrayList<String> categoryNames = new ArrayList<>();
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Gson gson = new Gson();
                            Category category = gson.fromJson(String.valueOf(jsonObject),Category.class);
                            categories.add(category);
                            categoryNames.add(category.getTitle());
                        }
                        setCategoryAdapter(categoryNames);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    if (progressDialog!=null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //    Toast.makeText(context.getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void setCategoryAdapter(ArrayList<String> categoryNames) {
      //  categoriesAdapter = new CategoriesAdapter(context, categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(categoryNames);
        spCategory.setAdapter(adapter);
      //  spCategory.setAdapter(categoriesAdapter);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Product added successfully");
        builder.setMessage("");
        builder.setPositiveButton("Add Variants", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setUpVariant();
            }
        });
        builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetData();
            }
        });
        AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
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

    private void disableProductDetails() {
        spCategory.setEnabled(false);
        etProductName.setEnabled(false);
    }

    private void enableProductDetails() {
        spCategory.setEnabled(true);
        etProductName.setEnabled(true);
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