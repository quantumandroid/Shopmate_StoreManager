package com.myshopmate.store.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.myshopmate.store.Model.NextdayOrderModel;
import com.myshopmate.store.R;
import com.myshopmate.store.util.Session_management;
import com.myshopmate.store.util.TodayOrderClickListner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class My_Nextday_Order_Adapter extends RecyclerView.Adapter<My_Nextday_Order_Adapter.MyViewHolder> {

    String select = "false";
    SharedPreferences preferences;
    private List<NextdayOrderModel> modelList;
    private LayoutInflater inflater;
    private TodayOrderClickListner todayOrderClickListner;
    private Session_management session_management;
    //    private Fragment currentFragment;
    private Context context;

//    public My_Nextday_Order_Adapter(Context context, List<NextdayOrderModel> modemodelList, final Fragment currentFragment) {
//
//        this.context = context;
//        this.modelList = modelList;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.currentFragment = currentFragment;
//    }


    public My_Nextday_Order_Adapter(List<NextdayOrderModel> modelList, TodayOrderClickListner todayOrderClickListner) {
        this.modelList = modelList;
        this.todayOrderClickListner = todayOrderClickListner;
    }

    @Override
    public My_Nextday_Order_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_next_day_order_rv
                , parent, false);
        context = parent.getContext();
        session_management = new Session_management(parent.getContext());
        return new My_Nextday_Order_Adapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final My_Nextday_Order_Adapter.MyViewHolder holder, final int position) {
        NextdayOrderModel mList = modelList.get(position);
        if (mList.getPayment_mode().equals("Store Pick Up")) {
//            holder.assign_layout.setVisibility(View.GONE);
            holder.payment_mode.setText(mList.getPayment_mode());
        } else if (mList.getPayment_mode() != "Store Pick Up") {
            holder.payment_mode.setText(mList.getPayment_mode());
//            holder.assign_layout.setVisibility(View.VISIBLE);
        }


//        if (mList.getDelivery_boy_name() != null && !mList.getDelivery_boy_name().equalsIgnoreCase("") && !mList.getDelivery_boy_name().equalsIgnoreCase("null")) {
//            holder.tv_assign_to.setVisibility(View.VISIBLE);
//            holder.tv_assign_to.setText("Assigned to " + mList.getDelivery_boy_name());
//        } else {
//            holder.tv_assign_to.setVisibility(View.GONE);
//            holder.tv_assign_to.setText("");
//        }

        if (mList.getOrder_status().equalsIgnoreCase("Cancelled")) {
//            holder.assign_layout.setVisibility(View.GONE);
            holder.Assign_Boy_name.setVisibility(View.VISIBLE);
            holder.Assign_Order_button.setVisibility(View.GONE);
            holder.tv_assign_to.setVisibility(View.GONE);
        } else {
//            holder.assign_layout.setVisibility(View.VISIBLE);
            if (mList.getDelivery_boy_name() != null && !mList.getDelivery_boy_name().equalsIgnoreCase("") && !mList.getDelivery_boy_name().equalsIgnoreCase("null")) {
                holder.tv_assign_to.setVisibility(View.VISIBLE);
                holder.Assign_Boy_name.setVisibility(View.GONE);
                holder.Assign_Order_button.setVisibility(View.VISIBLE);
                holder.tv_assign_to.setText("Assigned to " + mList.getDelivery_boy_name());
            } else {
                holder.Assign_Boy_name.setVisibility(View.VISIBLE);
                holder.tv_assign_to.setVisibility(View.GONE);
                holder.Assign_Order_button.setVisibility(View.GONE);
                holder.tv_assign_to.setText("");
            }
        }

//        if (mList.getOrder_status().equals("0")) {
//            holder.tv_status.setText(context.getResources().getString(R.string.pending));
//        } else if (mList.getStatus().equals("1")) {
//            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
//        } else if (mList.getStatus().equals("2")) {
//            holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
//        } else if (mList.getStatus().equals("3")) {
//            holder.tv_status.setText(context.getResources().getString(R.string.delivered));
//        }
//        if (mList.getAssign_to().equals("0")) {
//            holder.assign_layout.setVisibility(View.VISIBLE);
//        } else if (mList.getAssign_to() != "0") {
//            holder.tv_assign_to.setText(context.getResources().getString(R.string.assign_to) + mList.getAssign_to());
//            holder.assign_layout.setVisibility(View.GONE);
//        }

//        if (mList.getOrder_status().equalsIgnoreCase("Cancelled")) {
//            holder.assign_layout.setVisibility(View.GONE);
//        } else {
//            holder.assign_layout.setVisibility(View.VISIBLE);
//        }

        if (mList.getOrder_status().equalsIgnoreCase("Cancelled")) {
            holder.tv_status.setText(mList.getOrder_status());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0023")));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else if (mList.getOrder_status().equalsIgnoreCase("Out_For_Delivery")) {
            holder.tv_status.setText("Out For Delivery");
//            holder.Assign_Boy_name.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0023")));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else if (mList.getOrder_status().equalsIgnoreCase("Confirmed")) {
            holder.tv_status.setText(mList.getOrder_status());
//            holder.Assign_Boy_name.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#88EA17")));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else {
            holder.tv_status.setText(mList.getOrder_status());
//            holder.Assign_Boy_name.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1b4ddc")));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.white));
            }
        }

        holder.tv_order_id.setText(mList.getCart_id());
        holder.tv_customer_name.setText(mList.getUser_name());
        holder.customer_address.setText(mList.getUser_address());
        holder.tv_customer_phone.setText(mList.getUser_phone());
//        holder.tv_order_date.setText(mList.getDelivery_date());
        String orderDate = mList.getDelivery_date();
        List<String> dateList = new ArrayList<String>(Arrays.asList((orderDate.split("-"))));

        holder.tv_order_date.setText(dateList.get(2) + "-" + dateList.get(1) + "-" + dateList.get(0) + " & " + mList.getTime_slot());
        holder.tv_ammount.setText(session_management.getCurrency() + "" + mList.getOrder_price());
//        holder.tv_customer_socity.setText(mList.getSocity_name());
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language = preferences.getString("language", "");

        holder.Assign_Boy_name.setText("View Order");
        holder.Assign_Boy_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                todayOrderClickListner.ClickGetBoys(position, "unassign");
                todayOrderClickListner.forOrderTodayClick(position, "tomorrow");
            }
        });

        holder.Assign_Order_button.setText("Print Invoice");
        holder.Assign_Order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayOrderClickListner.assignClickBoys(holder.getAdapterPosition(), "tomorrow");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.Assign_Order_button.getVisibility() == View.VISIBLE) {
                    todayOrderClickListner.forOrderTodayClick(holder.getAdapterPosition(), "tomorrow");
                }
            }
        });

//        if (language.contains("spanish")) {
//            String timefrom=mList.getDelivery_time_from();
//            String timeto=mList.getDelivery_time_to();
//
//            timefrom=timefrom.replace("pm","م");
//            timefrom=timefrom.replace("am","ص");
//
//            timeto=timeto.replace("pm","م");
//            timeto=timeto.replace("am","ص");
//
//
//
//            holder.tv_order_time.setText(timefrom + "-" + timeto);
//        }else {
//
//            String timefrom=mList.getDelivery_time_from();
//            String timeto=mList.getDelivery_time_to();
//
//
//            holder.tv_order_time.setText(timefrom + "-" + timeto);
//
//        }


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_ammount, tv_assign_to, tv_status, tv_order_id, tv_customer_name, customer_address, Assign_Order_button,
                tv_customer_socity, tv_customer_phone, tv_order_date, tv_order_time, Assign_Boy_name, payment_mode;
        public RelativeLayout Assign_Order_to_layout;
        public CardView card_view;
        int Selected_Boy = 0;
        String SelectBoy = "";
        ArrayList<String> Boy_List = new ArrayList<>();
        String Boy_Id;
        ArrayList<String> BOY_LIST_ID = new ArrayList<>();
        private JsonObject Json;
        private LinearLayout assign_layout;

        public MyViewHolder(View view) {
            super(view);
            tv_ammount = view.findViewById(R.id.ammount);
            customer_address = view.findViewById(R.id.customer_address);
            payment_mode = view.findViewById(R.id.payment_mode);
            tv_assign_to = view.findViewById(R.id.assign_to);
            tv_status = view.findViewById(R.id.status);
            tv_order_id = view.findViewById(R.id.order_id);
            tv_customer_name = view.findViewById(R.id.customer_name);
            tv_customer_socity = view.findViewById(R.id.order_socity);
            tv_customer_phone = view.findViewById(R.id.customer_phone);
            tv_order_date = view.findViewById(R.id.order_date);
            tv_order_time = view.findViewById(R.id.order_time);
            assign_layout = view.findViewById(R.id.assign_layout);
//            Assign_Order_to_layout = view.findViewById(R.id.assign_order_to);
            Assign_Boy_name = view.findViewById(R.id.order_assign_list);
            Assign_Order_button = view.findViewById(R.id.assign_order);
//            assign_layout = (LinearLayout) view.findViewById(R.id.assign_layout);
//            Assign_Order_to_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Get_Boys();
//
//                }
//            });
//            Assign_Order_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (select.contains("true")) {
//
//                        int pos = getAdapterPosition();
//                        final String id = modelList.get(pos).getCart_id();
//                        final String getname = SharedPref.getString(context, BaseURL.KEY_DELIVERY_BOY_NAME);
//                        if (NetworkConnection.connectionChecking(context)) {
//                            RequestQueue rq = Volley.newRequestQueue(context);
//                            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.ASSIGN_ORDER,
//                                    new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            Log.i("eclipse", "Response=" + response);
//                                            try {
//                                                JSONObject object = new JSONObject(response);
//                                                JSONArray Jarray = object.getJSONArray("assign");
//                                                for (int i = 0; i < Jarray.length(); i++) {
//                                                    JSONObject json_data = Jarray.getJSONObject(i);
//                                                    String msg = json_data.getString("msg");
//                                                    Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
//                                                    Intent intent = new Intent(context, AssignSuccess.class);
//                                                    context.startActivity(intent);
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }, new Response.ErrorListener() {
//
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    System.out.println("Error [" + error + "]");
//                                }
//                            }) {
//
//                                @Override
//                                protected Map<String, String> getParams() throws AuthFailureError {
//                                    Map<String, String> params = new HashMap<String, String>();
//                                    params.put("order_id", id);
//                                    params.put("boy_name", getname);
//                                    return params;
//                                }
//                            };
//                            rq.add(postReq);
//                        } else {
//                            Intent intent = new Intent(context, NetworkError.class);
//                            context.startActivity(intent);
//                        }
//                    }else {
//                        Toast.makeText(context, "Please Select Delivery Boy", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            });


        }

//        private void SelectBoyDialog() {
//            final Dialog dialog;
//            dialog = new Dialog(context);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.activity_assign_order_dialog);
//            final ListView listView = dialog.findViewById(R.id.list_order);
//            SelectDeliveryBoyListViewAdapter sec = new SelectDeliveryBoyListViewAdapter(context, Boy_List);
//            listView.setAdapter(sec);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                    select = "true";
//                    SelectBoy = (String) adapterView.getItemAtPosition(position);
//                    Assign_Boy_name.setText(StringUtils.capitalize(Boy_List.get(position).toLowerCase().trim()));
//                    SelectBoy = Assign_Boy_name.getText().toString();
//                    SharedPref.putString(context, BaseURL.KEY_DELIVERY_BOY_NAME, SelectBoy);
//                    Selected_Boy = position + 1;
//                    Boy_Id = ("" + BOY_LIST_ID.get(position));
//                    SharedPref.putString(context, BaseURL.KEY_DELIVERY_BOY_ID, Boy_Id);
//                    dialog.dismiss();
//                }
//            });
//            dialog.getWindow().getDecorView().setTop(100);
//            dialog.getWindow().getDecorView().setLeft(100);
//            dialog.show();
//
//        }
//
//        private void Get_Boys() {
//            if (NetworkConnection.connectionChecking(context)) {
//                Json = new JsonObject();
//                Ion.with(context).load(BaseURL.productcancelled)
//                        .setTimeout(15000).setJsonObjectBody(Json).asString().setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String result) {
//                        if (e == null) {
//                            Log.e("result", result);
//                            try {
//                                JSONObject js = new JSONObject(result);
//                                {
//                                    JSONArray obj = js.getJSONArray("delivery_boy");
//                                    Boy_List.clear();
//                                    BOY_LIST_ID.clear();
//                                    for (int i = 0; i < obj.length(); i++) {
//                                        Boy_List.add("" + obj.getJSONObject(i).optString("user_name"));
//                                        BOY_LIST_ID.add("" + obj.getJSONObject(i).optString("id"));
//
//                                    }
//                                    Log.e("Size", "" + Boy_List.size());
//                                    Log.e("result", js.toString() + "\n" + js.getJSONArray("delivery_boy") + "\n"
//                                            + obj.getJSONObject(0).optString("user_name"));
//                                }
//                                SelectBoyDialog();
//                            } catch (JSONException e1) {
//                                e1.printStackTrace();
//                            }
//                        }
//                    }
//                });
//
//            } else {
//                Toast.makeText(context, "No Internet Connnection", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

}
