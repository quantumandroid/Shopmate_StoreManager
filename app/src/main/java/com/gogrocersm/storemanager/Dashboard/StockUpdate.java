package com.gogrocersm.storemanager.Dashboard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.franmontiel.localechanger.LocaleChanger;
import com.gogrocersm.storemanager.Adapter.SelectStockListViewAdapter;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Config.SharedPref;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.CustomVolleyJsonRequest;
import com.gogrocersm.storemanager.util.Session_management;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockUpdate extends AppCompatActivity {
    final Context context = this;
    EditText editText_Qty, editText_Unit;
    TextView editText_Product;
    int Selected_Stock_product = 0;
    String SelectProduct = "";
    ArrayList<String> Product_List = new ArrayList<>();
    String Product_Id;
    ArrayList<String> Product_LIST_ID = new ArrayList<>();
    String pdi, prductname, stock;
    private JsonObject Json;
    private Session_management sessionManagement;
    private ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_update);
        sessionManagement = new Session_management(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pur_pr));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we updating your stock..");
        progressDialog.setCancelable(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent intent = new Intent(StockUpdate.this, MainActivity.class);
//                startActivity(intent);
            }
        });

        pdi = getIntent().getStringExtra("po_id");
        prductname = getIntent().getStringExtra("product_name");


        editText_Product = (TextView) findViewById(R.id.et_product);
        editText_Qty = (EditText) findViewById(R.id.et_quantity);
        editText_Unit = (EditText) findViewById(R.id.et_unit);

        editText_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Get_Products();


            }
        });

        editText_Product.setText(prductname);

        RelativeLayout Next = (RelativeLayout) findViewById(R.id.btn_add_product);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String value = editText_Product.getText().toString();
//                if (value.equals(SelectProduct)) {
                stockiupdate();

//                } else {
//                    Toast.makeText(context, "Please Select Product", Toast.LENGTH_SHORT).show();
//                }

            }
        });
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


    private void SelectProductsDialog() {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_stock_product_dialog);
        final ListView listView = (ListView) dialog.findViewById(R.id.list_product);
        SelectStockListViewAdapter sec = new SelectStockListViewAdapter(context, Product_List);
        listView.setAdapter(sec);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SelectProduct = (String) adapterView.getItemAtPosition(position);
                editText_Product.setText(StringUtils.capitalize(Product_List.get(position).toLowerCase().trim()));
                SelectProduct = editText_Product.getText().toString();
                Selected_Stock_product = position + 1;
                Product_Id = ("" + Product_LIST_ID.get(position));
                SharedPref.putString(context, BaseURL.KEY_STOCK_PRODUCTS_ID, Product_Id);
                dialog.dismiss();
            }
        });
        dialog.getWindow().getDecorView().setTop(100);
        dialog.getWindow().getDecorView().setLeft(100);
        dialog.show();

    }

    //    private void Get_Products() {
//        if (NetworkConnection.connectionChecking(context)) {
//            Json = new JsonObject();
//            Ion.with(context).load(BaseURL.GET_STOCK)
//                    .setTimeout(15000).setJsonObjectBody(Json).asString().setCallback(new FutureCallback<String>() {
//                @Override
//                public void onCompleted(Exception e, String result) {
//                    if (e == null) {
//                        Log.e("result", result);
//                        try {
//                            JSONObject js = new JSONObject(result);
//                            {
//                                JSONArray obj = js.getJSONArray("products");
//                                Product_List.clear();
//                                Product_LIST_ID.clear();
//                                for (int i = 0; i < obj.length(); i++) {
//                                    Product_List.add("" + obj.getJSONObject(i).optString("product_name"));
//                                    Product_LIST_ID.add("" + obj.getJSONObject(i).optString("product_id"));
//                                }
//                                Log.e("Size", "" + Product_List.size());
//                                Log.e("result", js.toString() + "\n" + js.getJSONArray("products") + "\n"
//                                        + obj.getJSONObject(0).optString("product_name"));
//                            }
//                            SelectProductsDialog();
//                        } catch (JSONException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//            });
//
//        } else {
//            Toast.makeText(context, "No Internet Connnection", Toast.LENGTH_SHORT).show();
//        }
//    }
    private void stockiupdate() {
        progressDialog.show();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("p_id", pdi);
        params.put("stock", editText_Qty.getText().toString());
        Log.d("xx", pdi);


        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.store_stock_update, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {
                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {
                        Intent intent = new Intent();
                        setResult(10,intent);
                        onBackPressed();
                        Toast.makeText(context.getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

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
//    private void update() {
//        final String getid = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//        final String pro_id = SharedPref.getString(this, BaseURL.KEY_STOCK_PRODUCTS_ID);
//        final String Qty = editText_Qty.getText().toString();
//        final String unit = editText_Unit.getText().toString();
//        if (NetworkConnection.connectionChecking(this)) {
//            RequestQueue rq = Volley.newRequestQueue(this);
//            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.store_stock_update,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.i("eclipse", "Response=" + response);
//                            try {
//                                JSONObject object = new JSONObject(response);
//                                JSONArray Jarray = object.getJSONArray("product");
//                                for (int i = 0; i < Jarray.length(); i++) {
//                                    JSONObject json_data = Jarray.getJSONObject(i);
//                                    String msg = json_data.getString("msg");
//                                    Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    System.out.println("Error [" + error + "]");
//                }
//            }) {
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("product_id", pro_id);
//                    params.put("qty", Qty);
//                    params.put("unit", unit);
//                    params.put("store_id_login", getid);
//
//                    return params;
//                }
//            };
//            rq.add(postReq);
//        } else {
//            Intent intent = new Intent(this, NetworkError.class);
//            startActivity(intent);
//        }
//    }
}
