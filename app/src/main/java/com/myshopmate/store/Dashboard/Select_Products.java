package com.myshopmate.store.Dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.myshopmate.store.Adapter.Select_Product_Adapter;
import com.myshopmate.store.AppController;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Model.Select_Product_Model;
import com.myshopmate.store.NetworkConnectivity.ApiInterface;
import com.myshopmate.store.R;
import com.myshopmate.store.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Select_Products extends AppCompatActivity implements Select_Product_Adapter.SelectProductListener {

    List<Select_Product_Model> selectProductModels = new ArrayList<>();
    RecyclerView recyclerView_selectproducts;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String store_id;
    private ProgressBar progress_circular;
    private Select_Product_Adapter adapter;
    private CheckBox chkSelectAll;
    private Context context;
    private boolean isSelectionChanged;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (isSelectionChanged) {
            showSelectionChangedDialog();
        } else {
            closeScreen();
        }
    }

    private void closeScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    private void showSelectionChangedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Save selected products");
        builder.setMessage("You have changed selection for one or more products.\nWould you like to save selection before leaving ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectProducts();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeScreen();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__products);
        context = this;
        isSelectionChanged = false;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.SelectProduct));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Select_Products.this, MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        store_id = sharedPreferences.getString("id", "");

        findViewById(R.id.btn_save_product_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProducts();
            }
        });
        chkSelectAll = findViewById(R.id.chk_select_all);
        /*chkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectAll();
                } else {
                    resetAll();
                }
            }
        });*/
        chkSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkSelectAll.isChecked()) {
                    selectAll();
                } else {
                    resetAll();
                }
            }
        });
        progress_circular = findViewById(R.id.progress_circular);

        recyclerView_selectproducts = findViewById(R.id.recyle_selectproducts);
        recyclerView_selectproducts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.VERTICAL, false));
        adapter = new Select_Product_Adapter(context, selectProductModels);
        recyclerView_selectproducts.refreshDrawableState();
        recyclerView_selectproducts.smoothScrollToPosition(0);
        recyclerView_selectproducts.setAdapter(adapter);
    }

    private void selectAll() {
        if (selectProductModels != null && adapter != null) {
            for (Select_Product_Model product : selectProductModels) {
                product.setAdded(true);
                product.setChanged(product.isAdded() != product.isInitialState());
            }
            adapter.notifyDataSetChanged();
//            recyclerView_selectproducts.smoothScrollToPosition(0);
        }
    }

    private void resetAll() {
        if (selectProductModels != null && adapter != null) {
            for (Select_Product_Model product : selectProductModels) {
                product.setAdded(false);
                product.setChanged(product.isAdded() != product.isInitialState());
            }
            adapter.notifyDataSetChanged();
//            recyclerView_selectproducts.smoothScrollToPosition(0);
        }
    }

    private void showSuccessAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                get_selected_produts();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void selectProducts() {
        if (adapter != null) {
            ArrayList<Select_Product_Model> products = adapter.getSelectionChangedProducts();
            if (products.size() > 0 && !store_id.isEmpty()) {
                if (progress_circular.getVisibility() == View.INVISIBLE || progress_circular.getVisibility() == View.GONE) {
                    progress_circular.setVisibility(View.VISIBLE);
                }
                String productsstr = new Gson().toJson(products);
                ApiInterface apiInterface = ApiInterface.retrofit.create(ApiInterface.class);
                Call<JSONObject> jsonObjectCall = apiInterface.changeSelectedProducts(store_id, productsstr);
                jsonObjectCall.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                        progress_circular.setVisibility(View.GONE);
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                        isSelectionChanged = false;
                        showSuccessAlert("Success");
                        /*if (response.body() != null) {
                            try {
                                String message = response.body().getString("message");
                                String status = response.body().getString("status");
                                if (status.trim().equals("1")) {
                                    isSelectionChanged = false;
                                    get_selected_produts();
                                }
                                Toast.makeText(Select_Products.this, message, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                progress_circular.setVisibility(View.GONE);
                                Toast.makeText(Select_Products.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progress_circular.setVisibility(View.GONE);
                            Toast.makeText(Select_Products.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }*/
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        progress_circular.setVisibility(View.GONE);
                        Toast.makeText(Select_Products.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void get_selected_produts() {
        if (progress_circular.getVisibility() == View.INVISIBLE || progress_circular.getVisibility() == View.GONE) {
            progress_circular.setVisibility(View.VISIBLE);
        }
        selectProductModels.clear();
        //  category2_models.clear();
        // category3_models.clear();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
//        Log.d("dd", store_id);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.productselect, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress_circular.setVisibility(View.GONE);
                Log.d("Tag", response.toString());
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String quantity = jsonObject.getString("quantity");
                            String unit = jsonObject.getString("unit");
                            String mrp = jsonObject.getString("mrp");
                            String price = jsonObject.getString("price");
                            String varient_image = jsonObject.getString("varient_image");
                            String product_name = jsonObject.getString("product_name");
                            String inStock = jsonObject.getString("in_stock");
                            String description = jsonObject.getString("description");
                            boolean isAdded = Boolean.parseBoolean(jsonObject.getString("is_added"));
                            String p_id = jsonObject.getString("p_id");
                            String stock = jsonObject.getString("stock");
                            String variantID = jsonObject.getString("varient_id");

                            Select_Product_Model select_organistionmodel = new Select_Product_Model();

                            select_organistionmodel.setMrp(mrp);
                            select_organistionmodel.setQuantity(quantity);
                            select_organistionmodel.setUnit(unit);
                            select_organistionmodel.setPrice(price);
                            select_organistionmodel.setVarient_image(varient_image);
                            select_organistionmodel.setProduct_name(product_name);
                            select_organistionmodel.setAdded(isAdded);
                            select_organistionmodel.setChanged(false);
                            select_organistionmodel.setPriceChanged(false);
                            select_organistionmodel.setInitialState(isAdded);
                            select_organistionmodel.setP_id(p_id);
                            select_organistionmodel.setIn_stock(inStock);
                            select_organistionmodel.setStock(stock);
                            select_organistionmodel.setDescription(description);
                            select_organistionmodel.setVarient_id(variantID);
                            selectProductModels.add(select_organistionmodel);
                        }
                        adapter.notifyDataSetChanged();
//                        chkSelectAll.setChecked(adapter.isAllSelected());
                    } else {
                        Toast.makeText(Select_Products.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_circular.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_selected_produts();
    }

    @Override
    public void onSelectionChanged(boolean allSelected, int position) {
        chkSelectAll.setChecked(allSelected);
        isSelectionChanged = true;
    }

    @Override
    public void onNothingChanged() {
        isSelectionChanged = false;
    }
}
