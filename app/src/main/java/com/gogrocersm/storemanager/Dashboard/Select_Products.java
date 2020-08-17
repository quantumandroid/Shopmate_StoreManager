package com.gogrocersm.storemanager.Dashboard;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gogrocersm.storemanager.Adapter.Select_Product_Adapter;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Model.Select_Product_Model;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select_Products extends AppCompatActivity {

    List<Select_Product_Model> selectProductModels = new ArrayList<>();
    RecyclerView recyclerView_selectproducts;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String store_id;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__products);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.SelectProduct));
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

        recyclerView_selectproducts = findViewById(R.id.recyle_selectproducts);
        recyclerView_selectproducts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false));

        select_produts();
    }

    private void select_produts() {

        selectProductModels.clear();
        //  category2_models.clear();
        // category3_models.clear();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
        Log.d("dd", store_id);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.productselect, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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

                            Select_Product_Model select_organistionmodel = new Select_Product_Model();


                            select_organistionmodel.setMrp(mrp);
                            select_organistionmodel.setQuantity(quantity);
                            select_organistionmodel.setUnit(unit);
                            select_organistionmodel.setPrice(price);
                            select_organistionmodel.setVarient_image(varient_image);
                            select_organistionmodel.setProduct_name(product_name);


                            selectProductModels.add(select_organistionmodel);


                        }

                        Select_Product_Adapter adapter = new Select_Product_Adapter(selectProductModels);
                        recyclerView_selectproducts.setAdapter(adapter);
                        recyclerView_selectproducts.refreshDrawableState();
                        recyclerView_selectproducts.smoothScrollToPosition(0);

                        recyclerView_selectproducts.setAdapter(adapter);
                    } else {

                        // Toast.makeText(select_city.this, "data not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }
}
