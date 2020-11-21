package com.myshopmate.store.Dashboard;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.franmontiel.localechanger.LocaleChanger;
import com.myshopmate.store.Adapter.AllProductsAdapter;
import com.myshopmate.store.AddEditProductActivity;
import com.myshopmate.store.AppController;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Model.NewAllProductModel;
import com.myshopmate.store.R;
import com.myshopmate.store.util.AllProductClickListner;
import com.myshopmate.store.util.ConnectivityReceiver;
import com.myshopmate.store.util.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllProducts extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String store_id;
    private RecyclerView mList;
    private List<NewAllProductModel> movieList;
    private List<NewAllProductModel> searchList = new ArrayList<>();
    private AllProductsAdapter adapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;
    private SearchManager searchManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        store_id = sharedPreferences.getString("id", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.all_product));
        mList = findViewById(R.id.main_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AllProducts.this, MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
        movieList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mList.setLayoutManager(mLayoutManager);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        mList.setAdapter(adapter);

        if (ConnectivityReceiver.isConnected()) {
            progressDialog.show();
            product();
        } else {
            Toast.makeText(getApplicationContext(), "Network Issue", Toast.LENGTH_SHORT).show();
        }
    }


    private void product() {
        //  category2_models.clear();
        // category3_models.clear();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
        Log.d("dd", store_id);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.storeproducts, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());
                try {

                    String status = response.getString("status");
//                    String message = response.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        movieList.clear();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewAllProductModel>>() {
                        }.getType();
                        List<NewAllProductModel> orderModels = gson.fromJson(response.getString("data"), listType);
                        movieList.addAll(orderModels);
                    }

                    adapter = new AllProductsAdapter(AllProducts.this, movieList, searchList,new AllProductClickListner() {
                        @Override
                        public void onClick(int position) {
                            openLanguageDialog(position);
//                            delete(movieList.get(position).getP_id(),position);
                        }
                    });
                    mList.setAdapter(adapter);
                    mList.smoothScrollToPosition(0);

//                    if (status.contains("1")) {
//
//                        JSONArray jsonArray = response.getJSONArray("data");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            AlllProductModel movie = new AlllProductModel();
//                            movie.setCat_id(jsonObject.getString("cat_id"));
//                            movie.setProduct_name(jsonObject.getString("product_name"));
//                            movie.setMrp(jsonObject.getString("mrp"));
//                            movie.setPrice(jsonObject.getString("price"));
//                            movie.setQuantity(jsonObject.getString("quantity"));
//                            movie.setUnit(jsonObject.getString("unit"));
//                            movie.setP_id(jsonObject.getString("p_id"));
//
//                            movie.setVarient_image(jsonObject.getString("varient_image"));
//
//                            movieList.add(movie);
//
//                        }
//                    } else {
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
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

    private void delete(String p_id, final int position) {
        progressDialog.show();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("p_id", p_id);
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.store_delete_product, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {
                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {
//                        context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
                        Toast.makeText(AllProducts.this, "" + message, Toast.LENGTH_SHORT).show();
                        movieList.remove(position);
                        adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressDialog.dismiss();
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


    private void openLanguageDialog(final int position) {
//        View v = LayoutInflater.from(this).inflate(R.layout.dialog_language, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete the product!");
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delete(movieList.get(position).getP_id(), position);
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setVisibility(View.GONE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (adapter!=null){
//                    adapter.filterProductName(query);
//                }
////                adapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
////                adapter.getFilter().filter(query);
//                if (adapter!=null){
//                    adapter.filterProductName(query);
//                }
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        switch (item.getItemId()) {

            case R.id.action_add:
                //TODO add menu action here
                startActivity(new Intent(this, AddEditProductActivity.class));
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

       // return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
//        }

    }


    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

}
