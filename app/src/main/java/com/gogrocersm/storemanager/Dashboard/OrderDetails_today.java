package com.gogrocersm.storemanager.Dashboard;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogrocersm.storemanager.Adapter.DeliveryBoyAdapter;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Model.My_order_detail_model;
import com.gogrocersm.storemanager.Model.NewDeliveryBoyModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.ConnectivityReceiver;
import com.gogrocersm.storemanager.util.CustomVolleyJsonArrayRequest;
import com.gogrocersm.storemanager.util.DeliveryBoyListClick;
import com.gogrocersm.storemanager.util.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetails_today extends AppCompatActivity {

    TextView status, order_id, customer_name, order_socity, customer_phone, customer_address, order_date, order_time, ammount,call_name;
    ImageView Phone;
    String phone_number = "";
    ImageView back_button;
    private RecyclerView rv_detail_order;
    Button reject_btn, confirm_order;
    private String store_order_id;
    private String sale_id, store_id, cartid;
    private ProgressDialog dialog;
    private RelativeLayout con_layout;
    private List<NewDeliveryBoyModel> boyModels = new ArrayList<>();

    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    //    private SharedPreferences.Editor editor;
    private Session_management session_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_today);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.order_detail));
        }

        session_management = new Session_management(this);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        rv_detail_order = findViewById(R.id.product_recycler);
        call_name = findViewById(R.id.call_name);
        con_layout = findViewById(R.id.con_layout);
        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
        store_id = sharedPreferences.getString("id", "");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        rv_detail_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_detail_order.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));

        status = findViewById(R.id.status);
        order_id = findViewById(R.id.order_id);
        customer_name = findViewById(R.id.customer_name);
        customer_address = findViewById(R.id.customer_address);
        reject_btn = findViewById(R.id.reject_order);
        confirm_order = findViewById(R.id.confirm_order);
//        order_socity = findViewById(R.id.order_socity);
        customer_phone = findViewById(R.id.customer_phone);
        order_date = findViewById(R.id.order_date);
//        order_time = findViewById(R.id.order_time);
        ammount = findViewById(R.id.ammount);

        sale_id = getIntent().getStringExtra("sale_id");
//        store_order_id = getIntent().getStringExtra("store_order_id");

        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
        } else {
            Toast.makeText(getApplicationContext(), "Network Issue", Toast.LENGTH_SHORT).show();
        }
        String user_fullname = getIntent().getStringExtra("user_fullname");
//        String socity = getIntent().getStringExtra("socity");
        String phone = getIntent().getStringExtra("customer_phone");
        String address = getIntent().getStringExtra("address");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String amount = getIntent().getStringExtra("ammount");
        String stats = getIntent().getStringExtra("status");
        cartid = getIntent().getStringExtra("cart_id");
        my_order_detail_modelList.clear();
        List<My_order_detail_model> ordd = (List<My_order_detail_model>) getIntent().getSerializableExtra("data");
        if (ordd != null) {
            my_order_detail_modelList.addAll(ordd);
        }
//        my_order_detail_modelList = (List<My_order_detail_model>) getIntent().getSerializableExtra("data");
//        if (stats.equals("0")) {
//            status.setText(getResources().getString(R.string.pending));
//        } else if (stats.equals("1")) {
//            status.setText(getResources().getString(R.string.confirm));
//        } else if (stats.equals("2")) {
//            status.setText(getResources().getString(R.string.outfordeliverd));
//        } else if (stats.equals("4")) {
//            status.setText(getResources().getString(R.string.delivered));
//        }

        order_id.setText("#" + sale_id);
        customer_name.setText(user_fullname);
        call_name.setText("Call To Customer");
//        order_socity.setText(socity);
        customer_phone.setText(phone);
        customer_address.setText(address);
//        String orderDate = mList.getDelivery_date();
        List<String> dateList = new ArrayList<String>(Arrays.asList((date.split("-"))));

        order_date.setText(dateList.get(2)+"-"+dateList.get(1)+"-"+dateList.get(0) + " & " + time);
//        order_date.setText(date + " & " + time);
//        order_time.setText(time);
        ammount.setText(session_management.getCurrency() + amount);
//        status.setText(stats);

        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_reject();

            }
        });

        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                confirmorder();
                getBoysList();
            }
        });

        if (stats!=null){

            if (stats.equalsIgnoreCase("Cancelled")){
                status.setText(stats);
                con_layout.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0023")));
                    status.setTextColor(getResources().getColor(R.color.white));
                }
            }else if (stats.equalsIgnoreCase("Out_For_Delivery")){
                status.setText("Out For Delivery");
                con_layout.setVisibility(View.GONE);
                confirm_order.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0023")));
                    status.setTextColor(getResources().getColor(R.color.white));
                }
            }else if (stats.equalsIgnoreCase("Confirmed")){
                status.setText(stats);
                con_layout.setVisibility(View.VISIBLE);
                confirm_order.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#88EA17")));
                    status.setTextColor(getResources().getColor(R.color.white));
                }
            }else {
                status.setText(stats);
                con_layout.setVisibility(View.VISIBLE);
                confirm_order.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1b4ddc")));
                    status.setTextColor(getResources().getColor(R.color.white));
                }
            }
        }



        Phone = findViewById(R.id.make_call);
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action();
                }

            }

        });


    }

    private void makeGetOrderDetailRequest(String sale_id) {

        // Tag used to cancel the request
//        String tag_json_obj = "json_order_detail_req";
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("sale_id", sale_id);
//
//        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
//                BaseURL.OrderDetail, params, new Response.Listener<JSONArray>() {
//
//            @Override
//            public void onResponse(JSONArray response) {
//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<My_order_detail_model>>() {
//                }.getType();
//
//                my_order_detail_modelList = gson.fromJson(response.toString(), listType);


//        JSONArray jsonArray = new JSONArray();
//        jsonArray = BaseURL.order_detail;
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//
//            try {
//                JSONObject object = jsonArray.getJSONObject(i);
//
//                My_order_detail_model my_order_detail_model1 = new My_order_detail_model();
//                my_order_detail_model1.setProduct_name(object.getString("product_name"));
//                my_order_detail_model1.setPrice(object.getString("price"));
//                my_order_detail_model1.setProduct_image(object.getString("varient_image"));
//                my_order_detail_model1.setQty(object.getString("qty"));
//                my_order_detail_model1.setStore_order_id(object.getString("store_order_id"));
//
//
//                my_order_detail_modelList.add(my_order_detail_model1);
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }

        OrderDetails_today.My_order_detail_adapter adapter = new OrderDetails_today.My_order_detail_adapter(my_order_detail_modelList);
        rv_detail_order.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        if (my_order_detail_modelList.size() <= 0) {
//            Toast.makeText(OrderDetails_today.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
//        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void call_action() {
        phone_number = customer_phone.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
        callIntent.setData(Uri.parse("tel:" + phone_number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
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

    class My_order_detail_adapter extends RecyclerView.Adapter<OrderDetails_today.My_order_detail_adapter.MyViewHolder> {

        private List<My_order_detail_model> modelList;
        private List<My_order_detail_model> itemList;
        private Context context;

        public My_order_detail_adapter(List<My_order_detail_model> modelList) {
            this.modelList = modelList;
        }

        @Override
        public OrderDetails_today.My_order_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.orderdetails_todayc_listitem, parent, false);

            context = parent.getContext();
            return new OrderDetails_today.My_order_detail_adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetails_today.My_order_detail_adapter.MyViewHolder holder, int position) {
            My_order_detail_model mList = modelList.get(position);

            Glide.with(context)
                    .load(BaseURL.IMG_PRODUCT_URL + mList.getVarient_image())
                    .centerCrop()
                    .placeholder(R.drawable.icons)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.iv_img);

            holder.tv_title.setText(mList.getProduct_name()+" - "+(mList.getQty() + " x " + mList.getQuantity()+ "" +mList.getUnit()));
            holder.tv_price.setText(session_management.getCurrency() + " " + mList.getPrice());
//            holder.tv_qty.setText("Qty - " + mList.getQty() + " x " + mList.getQuantity()+ "" +mList.getUnit());


        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_title, tv_price, tv_qty;
            public ImageView iv_img, delete;

            public MyViewHolder(View view) {
                super(view);
                tv_title = view.findViewById(R.id.tv_order_Detail_title);
                tv_price = view.findViewById(R.id.tv_order_Detail_price);
                tv_qty = view.findViewById(R.id.tv_order_Detail_qty);
                iv_img = view.findViewById(R.id.iv_order_detail_img);
//                delete = view.findViewById(R.id.delete);


            }
        }

    }

    private void order_reject() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
        params.put("cart_id", cartid);

        CustomVolleyJsonArrayRequest jsonObjectRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseURL.order_rejected, params
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Tag", response.toString());

                try {

                    JSONObject object = response.getJSONObject(0);
                    String result = object.getString("result");

//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    Intent intent = new Intent();
                    intent.putExtra("runapi","true");
                    setResult(7,intent);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    }else {
                        finish();
                    }

                    Toast.makeText(OrderDetails_today.this, "" + result, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //    Toast.makeText(context.getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void getBoysList() {
        dialog.show();
        boyModels.clear();
        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBoyList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG", response);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewDeliveryBoyModel>>() {
                        }.getType();
                        List<NewDeliveryBoyModel> nextdayOrderModels1 = gson.fromJson(jsonObject.getString("data"), listType);
                        boyModels.addAll(nextdayOrderModels1);
                        selectBoyDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                System.out.println("Error [" + error + "]");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("store_id", store_id);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }


    private void selectBoyDialog() {
        final Dialog dialogs;
        dialogs = new Dialog(OrderDetails_today.this);
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogs.setContentView(R.layout.delivery_boy_list);
//        dialog.getWindow().getDecorView().setTop(100);
//        dialog.getWindow().getDecorView().setLeft(100);

        RecyclerView listRecy = dialogs.findViewById(R.id.boy_list);
        DeliveryBoyAdapter deliveryBoyAdapter = new DeliveryBoyAdapter(OrderDetails_today.this, boyModels, new DeliveryBoyListClick() {
            @Override
            public void onClick(int position) {
                dialogs.dismiss();
                if (dialog!=null && !dialog.isShowing()){
                    dialog.show();
                }
                assignBoyToOrder(boyModels.get(position).getDboy_id());
            }
        });
        listRecy.setLayoutManager(new LinearLayoutManager(OrderDetails_today.this, LinearLayoutManager.VERTICAL, false));
        listRecy.setAdapter(deliveryBoyAdapter);
        dialogs.show();

    }

    private void assignBoyToOrder(final String dboy_id) {
//        dialog.show();
//        boyModels.clear();
        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.assignBoyToOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        Intent intent = new Intent();
                        intent.putExtra("runapi","true");
                        setResult(7,intent);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        }else {
                            finish();
                        }
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<NewDeliveryBoyModel>>() {
//                        }.getType();
//                        List<NewDeliveryBoyModel> nextdayOrderModels1 = gson.fromJson(jsonObject.getString("data"), listType);
//                        boyModels.addAll(nextdayOrderModels1);
                    }else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                System.out.println("Error [" + error + "]");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("store_id", store_id);
                params.put("cart_id", cartid);
                params.put("dboy_id", dboy_id);
//                params.put("store_id", store_id);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}