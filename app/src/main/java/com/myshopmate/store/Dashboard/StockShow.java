package com.myshopmate.store.Dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.myshopmate.store.Adapter.StockShowAdapter;
import com.myshopmate.store.AppController;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Model.AlllProductModel;
import com.myshopmate.store.R;
import com.myshopmate.store.util.CustomVolleyJsonRequest;
import com.myshopmate.store.util.StockUpdaterInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockShow extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String store_id;
    private List<AlllProductModel> movieList;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Stock");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we fetching your stocks detail..");
        progressDialog.setCancelable(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StockShow.this, MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
        movieList = new ArrayList<>();

        recyclerView = findViewById(R.id.stock_recyler_view);
        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        store_id = sharedPreferences.getString("id", "");

        product();
    }


    private void product() {
        progressDialog.show();
        movieList.clear();
        //  category2_models.clear();
        // category3_models.clear();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
//        Log.d("dd", store_id);
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.storeproducts, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());
                try {

                    String status = response.getString("status");
//                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            AlllProductModel movie = new AlllProductModel();
                            movie.setCat_id(jsonObject.getString("cat_id"));
                            movie.setProduct_name(jsonObject.getString("product_name"));
                            movie.setMrp(jsonObject.getString("mrp"));
                            movie.setPrice(jsonObject.getString("price"));
                            movie.setQuantity(jsonObject.getString("quantity"));
                            movie.setUnit(jsonObject.getString("unit"));
                            movie.setP_id(jsonObject.getString("p_id"));
                            movie.setStock(jsonObject.getString("stock"));
                            movie.setVarient_image(jsonObject.getString("varient_image"));
                            movieList.add(movie);

                        }

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        StockShowAdapter adapter = new StockShowAdapter(getApplicationContext(), movieList, new StockUpdaterInterface() {
                            @Override
                            public void onStockUpdate(int position) {
                                Intent intent = new Intent(StockShow.this, StockUpdate.class);
//                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("po_id", movieList.get(position).getP_id());
                                intent.putExtra("product_name", movieList.get(position).getProduct_name());
                                intent.putExtra("stock", movieList.get(position).getStock());
                                startActivityForResult(intent,10);
                            }

                        });
                        recyclerView.setAdapter(adapter);
                        recyclerView.smoothScrollToPosition(0);
                        recyclerView.setAdapter(adapter);
                    } else {

                        // Toast.makeText(select_city.this, "data not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10){
            product();
        }
    }
}
