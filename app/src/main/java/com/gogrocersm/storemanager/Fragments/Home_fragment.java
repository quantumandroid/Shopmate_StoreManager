package com.gogrocersm.storemanager.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gogrocersm.storemanager.Adapter.BluetoothListAdapter;
import com.gogrocersm.storemanager.Adapter.DeliveryBoyAdapter;
import com.gogrocersm.storemanager.Adapter.My_Nextday_Order_Adapter;
import com.gogrocersm.storemanager.Adapter.My_Today_Order_Adapter;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Dashboard.MyOrderDeatil;
import com.gogrocersm.storemanager.Dashboard.OrderDetails_today;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.Model.BluetoothModelClass;
import com.gogrocersm.storemanager.Model.InvoiceModel;
import com.gogrocersm.storemanager.Model.InvoiceNewProductModel;
import com.gogrocersm.storemanager.Model.ListAssignAndUnassigned;
import com.gogrocersm.storemanager.Model.NewDeliveryBoyModel;
import com.gogrocersm.storemanager.Model.NextdayOrderModel;
import com.gogrocersm.storemanager.Model.TodayOrderModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.BluetoothClick;
import com.gogrocersm.storemanager.util.DeliveryBoyListClick;
import com.gogrocersm.storemanager.util.PrinterCommands;
import com.gogrocersm.storemanager.util.Session_management;
import com.gogrocersm.storemanager.util.TodayOrderClickListner;
import com.gogrocersm.storemanager.util.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class Home_fragment extends Fragment {

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String TAG = Home_fragment.class.getSimpleName();
    ProgressDialog pd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String store_id;
    List<NewDeliveryBoyModel> boyModels = new ArrayList<>();
    OutputStream outputStream = null;
    Set<BluetoothDevice> pairedDevices = null;
    BitSet dots = new BitSet();
    //    private RecyclerView rv_today_orders,rv_next_day_orders;
    private My_Today_Order_Adapter my_today_order_adapter;
    private My_Nextday_Order_Adapter my_nextday_order_adapter;
    private List<TodayOrderModel> movieList = new ArrayList<>();
    private List<NextdayOrderModel> nextdayOrderModels = new ArrayList<>();
    private List<ListAssignAndUnassigned> listAssignAndUnassigneds = new ArrayList<>();
    private LinearLayout linearLayout;
    private AssignFragment adapter;
    private ProgressDialog dialog;
    private AlertDialog.Builder bluetoothDialog;
    private Context context;
    private int todayPosition = -1;
    private int unassPosition = -1;
    private String boy_id = "";
    private List<BluetoothModelClass> mpairedDeviceList = new ArrayList<>();
    private ArrayAdapter<String> mArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;   //��������������
    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothSocket mBluetoothSocket = null;
    private ByteArrayOutputStream buffer = null;
    private String selectedAddress = "";
    private SwipeRefreshLayout swipe_to;
    private String newCart_id = "";
    //    private List<InvoiceModel> invoiceModelList = new ArrayList<>();
    private InvoiceModel invoiceModel;
    private List<String> headListInvoice = new ArrayList<>();
    private List<String> invoiceStringList = new ArrayList<>();
    private List<String> dateInvoiceList = new ArrayList<>();
    private List<InvoiceNewProductModel> newProductModels = new ArrayList<>();
    private List<String> tailPartInvoice = new ArrayList<>();
    private List<String> totalPoint = new ArrayList<>();
    private BluetoothListAdapter myBtAdapter;
    private View view;
    private String bill = "";
    private String newbill = "";
    private int mWidth;
    private int mHeight;
    private Session_management session_management;

    public Home_fragment() {

    }

    private void showPrinterDialog() {
        bluetoothDialog = new AlertDialog.Builder(context);
        bluetoothDialog.setTitle("Select Printer");
        bluetoothDialog.setMessage(getString(R.string.XPrinterhint));
        bluetoothDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        });

        bluetoothDialog.setNeutralButton("No", (dialog, which) -> dialog.dismiss());
//        bluetoothDialog.show();
    }

    private void showBluetoothDialog(View view, String cart_id) {
        if (mBluetoothSocket != null && mBluetoothSocket.isConnected()) {
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                prinbill();
            } else {
                mpairedDeviceList.clear();
                try {
                    mBluetoothSocket.close();
                    mBluetoothSocket = null;
                    showPrinterDialog();
                    bluetoothDialog.create().show();
                    Toast.makeText(context, "No Printer Attach to the Device, Attach device first!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            mpairedDeviceList.clear();
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                showAlertBluetooth(view, cart_id);
            } else {
                showPrinterDialog();
                bluetoothDialog.create().show();
                Toast.makeText(context, "No Printer Attach to the Device, Attach device first!", Toast.LENGTH_SHORT).show();
            }
        }
//        if (selectedAddress != null && !selectedAddress.equalsIgnoreCase("")) {
//            dialog.show();
//            if (mBluetoothAdapter.getProfileConnectionState(BluetoothAdapter.STATE_CONNECTED) == BluetoothAdapter.STATE_CONNECTED) {
//                prinbill();
//            } else {
//                createConnection(selectedAddress, null, cart_id);
//            }
//
//        } else {
//
//        }
    }

    private void showAlertBluetooth(View view, String cart_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = this.view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.blueetoothselectview, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        RecyclerView mSpinner = dialogView.findViewById(R.id.blue_recy);
        TextView connected_device = dialogView.findViewById(R.id.connected_device);
        myBtAdapter = new BluetoothListAdapter(mpairedDeviceList, new BluetoothClick() {
            @Override
            public void onClick(int position) {
//                    alertDialog.dismiss();
                createConnection(mpairedDeviceList.get(position).getName() + "#" + mpairedDeviceList.get(position).getMacNumber(), alertDialog, cart_id);
            }
        });
        mSpinner.setAdapter(myBtAdapter);

        try {
            if (mBluetoothAdapter == null) {

            } else if (mBluetoothAdapter.isEnabled()) {
                String getName;
                pairedDevices = mBluetoothAdapter.getBondedDevices();
                while (mpairedDeviceList.size() > 1) {
                    mpairedDeviceList.remove(1);
                }
                if (pairedDevices.size() == 0) {
                    alertDialog.dismiss();
                    bluetoothDialog.create().show();
                }
                for (BluetoothDevice device : pairedDevices) {
//                        getName = device.getName() + "#" + device.getAddress();
                    mpairedDeviceList.add(new BluetoothModelClass(device.getName(), device.getAddress()));
                }
                myBtAdapter.notifyDataSetChanged();
            } else {
                alertDialog.dismiss();
                bluetoothDialog.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog.show();
    }

    private void createConnection(String selectedAdd, AlertDialog alertDialog, String cart_id) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        dialog.setMessage("Connecting with your device please wait..");
        dialog.setCancelable(false);
        dialog.show();
        new Thread(() -> {
            selectedAddress = selectedAdd;
            String temString = selectedAdd;
            if (mBluetoothAdapter.getProfileConnectionState(BluetoothAdapter.STATE_CONNECTED) == BluetoothAdapter.STATE_CONNECTED) {
                try {
                    outputStream.close();
                    mBluetoothSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            temString = temString.substring(temString.length() - 17);
            try {
                if (mBluetoothSocket != null && mBluetoothSocket.isConnected()) {
                    mBluetoothSocket.close();
                    mBluetoothSocket = null;
                }
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(temString);
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                mBluetoothSocket.connect();
            } catch (Exception e) {
                try {
                    mBluetoothSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                mBluetoothSocket = null;
                e.printStackTrace();
            } finally {
                requireActivity().runOnUiThread(() -> {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (mBluetoothSocket != null && mBluetoothSocket.isConnected()) {
                        prinbill();
                    } else {
                        Toast.makeText(context, "No Printer Attach to the Device, Attach device first!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

//        temString=temString.substring(temString.length()-17);
//        try {
////            buttonConnect.setText(getString(R.string.Connecting));
//            mBluetoothDevice=mBluetoothAdapter.getRemoteDevice(temString);
//            mBluetoothSocket=mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
//            mBluetoothSocket.connect();
////            buttonConnect.setText(getString(R.string.Connected));
////            setButtonEnadle(true);
//        } catch (Exception e) {
//            e.printStackTrace();
////            PrintfLogs(getString(R.string.Disconnected));
////            buttonConnect.setText(getString(R.string.Disconnected));
////            setButtonEnadle(false);
////            PrintfLogs(getString(R.string.ConnectFailed)+e.toString());
//        }
    }

    private void prinbill() {
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        if (dialog != null) {
            dialog.show();
        }
        new Thread(() -> {
            OutputStream opstream = null;
            try {
                opstream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = opstream;

            try {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                outputStream = mBluetoothSocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
                outputStream.write(printformat);
//            outputStream.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
//                    printPhoto(R.drawable.icons);
                for (int i = 0; i < headListInvoice.size(); i++) {
                    if (i == 0) {
                        printCustom(headListInvoice.get(i), 2, 1);
//                            printNewLine();
                    } else {
                        printCustom(headListInvoice.get(i), 1, 1);
                    }
//                    printCustom(headListInvoice.get(i), 2, 1);
                }
//            outputStream.write((int)(1));
                printNewLine();

                printCustomNewleft_right_2(dateInvoiceList.get(0), dateInvoiceList.get(1));

                byte[] bb = new byte[]{0x1B, 0x21, 0x08};
//                for (int j = 0; j < invoiceStringList.size(); j++) {
////                    if (j==1){
////                        printCustom(invoiceStringList.get(j),1,0);
////                    }else {
////
////                    }
//
//                    if ()
//                    outputStream.write(bb);
//                    outputStream.write(invoiceStringList.get(j).getBytes());
//                    outputStream.write(PrinterCommands.LF);
//
//                }
                outputStream.write(bb);
                outputStream.write(invoiceStringList.get(0).getBytes());
                outputStream.write(PrinterCommands.LF);
                printCustomNewleft_right(invoiceStringList.get(1), invoiceStringList.get(2));
                outputStream.write(bb);
                outputStream.write(invoiceStringList.get(3).getBytes());
                outputStream.write(PrinterCommands.LF);

                for (int z = 0; z < newProductModels.size(); z++) {
                    printCustomNew(newProductModels.get(z).getProductName(), newProductModels.get(z).getProductDescp());
                }

                outputStream.write(bb);
                outputStream.write(newbill.getBytes());
                outputStream.write(PrinterCommands.LF);

                for (int k = 0; k < tailPartInvoice.size(); k++) {
                    printCustom(tailPartInvoice.get(k), 1, 2);
                }

                outputStream.write(bb);
                outputStream.write(totalPoint.get(0).getBytes());
                outputStream.write(PrinterCommands.LF);
                printCustomNewleft_right_2(totalPoint.get(1), totalPoint.get(2));
                outputStream.write(bb);
                outputStream.write(totalPoint.get(3).getBytes());
                outputStream.write(PrinterCommands.LF);
//                    for (int u=0;u<totalPoint.size();u++){
//
//                    }
                printNewLine();
                printCustom("Thank you", 1, 1);
                printCustom("Customer Care: xxx-xxxxxxxx", 1, 1);
//                printUnicode();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();
                printNewLine();


//outputStream.write(0x1B);
//outputStream.write("Ajit Singh".getBytes());


//            printCustom("forward to serve you again", 0, 1);
//            printNewLine();
//            printNewLine();
//            outputStream.write(new byte[]{0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00});
                // Setting height
//            int gs = 29;
//            outputStream.write(intToByteArray(gs));
//            int h = 104;
//            outputStream.write(intToByteArray(h));
//            int n = 162;
//            outputStream.write(intToByteArray(n));
//
//            // Setting Width
//            int gs_width = 29;
//            outputStream.write(intToByteArray(gs_width));
//            int w = 119;
//            outputStream.write(intToByteArray(w));
//            int n_width = 2;
//            outputStream.write(intToByteArray(n_width));
                outputStream.flush();

            } catch (IOException e) {
                try {
                    mBluetoothSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                mBluetoothSocket = null;
                outputStream = null;
                e.printStackTrace();
            } finally {
                requireActivity().runOnUiThread(() -> {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (mBluetoothSocket == null) {
                        Toast.makeText(context, "No Printer Attach to the Device, Attach device first!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }

    private void printCustomNew(List<String> productName, String productDescp) {
        try {
//            byte[] cc = new byte[]{0x1B, 0x21, 0x03};
            byte[] bb = new byte[]{0x1B, 0x21, 0x08};
            outputStream.write(bb);
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            outputStream.write(productDescp.getBytes());
            outputStream.write(PrinterCommands.LF);
            if (!productName.isEmpty()) {
                for (int i = 0; i < productName.size(); i++) {
                    outputStream.write(bb);
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    outputStream.write(productName.get(i).getBytes());
                    outputStream.write(PrinterCommands.LF);
                }
            }
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printCustomNewleft_right(String heading, String producttotal) {
        try {
//            byte[] cc = new byte[]{0x1B, 0x21, 0x03};
            byte[] bb = new byte[]{0x1B, 0x21, 0x08};
            outputStream.write(bb);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(heading.getBytes());
            outputStream.write(PrinterCommands.HT);
            outputStream.write(bb);
            outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            outputStream.write(producttotal.getBytes());
//            printText(leftRightAlign(heading,producttotal));
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printCustomNewleft_right_2(String heading, String producttotal) {
        try {
//            byte[] cc = new byte[]{0x1B, 0x21, 0x03};
            byte[] bb = new byte[]{0x1B, 0x21, 0x08};
            outputStream.write(bb);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(heading.getBytes());
//            outputStream.write(PrinterCommands.HT);
            outputStream.write(bb);
            outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            outputStream.write(producttotal.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        context = container.getContext();
        session_management = new Session_management(context);
        dialog = new ProgressDialog(container.getContext());
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        tabLayout.addTab(tabLayout.newTab().setText("Today's Order"));
//        tabLayout.addTab(tabLayout.newTab().setText("Assigned Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Nextday's Order"));
//        tabLayout.addTab(tabLayout.newTab().setText("UnAssigned Orders"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        swipe_to = view.findViewById(R.id.swipe_to);


        listAssignAndUnassigneds.add(new ListAssignAndUnassigned("assigned", movieList, nextdayOrderModels));
        listAssignAndUnassigneds.add(new ListAssignAndUnassigned("unassigned", movieList, nextdayOrderModels));

        final ViewPager2 viewPager = view.findViewById(R.id.pager);
        adapter = new AssignFragment(container.getContext(), listAssignAndUnassigneds, new TodayOrderClickListner() {
            @Override
            public void ClickGetBoys(int position, String viewType) {
                getBoysList(viewType, position);
            }

            @Override
            public void assignClickBoys(int position, String viewType) {
                try {
                    if (viewType.equalsIgnoreCase("today")) {
                        if (newCart_id.equalsIgnoreCase(movieList.get(position).getCart_id())) {
                            showBluetoothDialog(view, movieList.get(position).getCart_id());
                        } else {
                            getInvoice(movieList.get(position).getCart_id());
                        }
                    } else if (viewType.equalsIgnoreCase("tomorrow")) {
                        if (newCart_id.equalsIgnoreCase(nextdayOrderModels.get(position).getCart_id())) {
                            showBluetoothDialog(view, nextdayOrderModels.get(position).getCart_id());
                        } else {
                            getInvoice(nextdayOrderModels.get(position).getCart_id());
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
//                    adapter.notifyDataSetChanged();
                    e.printStackTrace();
                }
            }

            @Override
            public void forOrderTodayClick(int position, String viewType) {

                if (viewType.equalsIgnoreCase("today")) {
                    String saleid = movieList.get(position).getCart_id();
                    String userfullname = movieList.get(position).getUser_name();
                    String customerphone = movieList.get(position).getUser_phone();
                    String date = movieList.get(position).getDelivery_date();
                    String time = movieList.get(position).getTime_slot();
                    String ammount = movieList.get(position).getOrder_price();
                    String status = movieList.get(position).getOrder_status();
                    String cart_id = movieList.get(position).getCart_id();
                    Intent intent = new Intent(context, OrderDetails_today.class);
                    intent.putExtra("sale_id", saleid);
                    intent.putExtra("user_fullname", userfullname);
                    intent.putExtra("customer_phone", customerphone);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("cart_id", cart_id);
                    intent.putExtra("ammount", ammount);
                    intent.putExtra("address", movieList.get(position).getUser_address());
                    intent.putExtra("status", status);
                    intent.putExtra("data", movieList.get(position).getOrder_details());
                    startActivityForResult(intent, 7);
                } else if (viewType.equalsIgnoreCase("tomorrow")) {
                    String saleid = nextdayOrderModels.get(position).getCart_id();
                    String userfullname = nextdayOrderModels.get(position).getUser_name();
                    String customerphone = nextdayOrderModels.get(position).getUser_phone();
                    String date = nextdayOrderModels.get(position).getDelivery_date();
                    String time = nextdayOrderModels.get(position).getTime_slot();
                    String ammount = nextdayOrderModels.get(position).getOrder_price();
                    String status = nextdayOrderModels.get(position).getOrder_status();
                    String cart_id = nextdayOrderModels.get(position).getCart_id();
                    Intent intent = new Intent(context, OrderDetails_today.class);
                    intent.putExtra("sale_id", saleid);
                    intent.putExtra("user_fullname", userfullname);
                    intent.putExtra("customer_phone", customerphone);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("cart_id", cart_id);
                    intent.putExtra("ammount", ammount);
                    intent.putExtra("address", nextdayOrderModels.get(position).getUser_address());
                    intent.putExtra("status", status);
                    intent.putExtra("data", nextdayOrderModels.get(position).getOrder_details());
                    startActivityForResult(intent, 7);
                }


            }

            @Override
            public void forOrderNextClick(int position) {
                String saleid = nextdayOrderModels.get(position).getCart_id();
                String userfullname = nextdayOrderModels.get(position).getUser_name();
                String cart_id = nextdayOrderModels.get(position).getCart_id();
                String customerphone = nextdayOrderModels.get(position).getUser_phone();
                String date = nextdayOrderModels.get(position).getDelivery_date();
                String ammount = nextdayOrderModels.get(position).getOrder_price();
                String status = nextdayOrderModels.get(position).getOrder_status();
                Intent intent = new Intent(context, MyOrderDeatil.class);
                intent.putExtra("sale_id", saleid);
                intent.putExtra("user_fullname", userfullname);
                intent.putExtra("cart_id", cart_id);
                intent.putExtra("customer_phone", customerphone);
                intent.putExtra("address", nextdayOrderModels.get(position).getUser_address());
                intent.putExtra("date", date);
                intent.putExtra("time", "");
                intent.putExtra("ammount", ammount);
                intent.putExtra("status", status);
                intent.putExtra("data", nextdayOrderModels.get(position).getOrder_details());
                startActivityForResult(intent, 7);
            }
        });
        viewPager.setAdapter(adapter);
//        rv_today_orders = (RecyclerView) view.findViewById(R.id.rv_today_order);
//        rv_next_day_orders=(RecyclerView)view.findViewById(R.id.rv_next_order);
//        rv_today_orders.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv_next_day_orders.setLayoutManager(new LinearLayoutManager(getActivity()));


        sharedPreferences = container.getContext().getSharedPreferences("logindata", MODE_PRIVATE);
//        editor = sharedPreferences.edit();

//        Log.d("dd", store_id);

//        adapter = new PagerOrderAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
        wrapTabIndicatorToTitle(tabLayout, 80, 80);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
//                    tab.setText("Today's Order");
                    tab.setText("Today's Order");
                } else if (position == 1) {
                    tab.setText("Nextday's Order");
                }

            }
        });
        tabLayoutMediator.attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
//        viewPager.addon(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        swipe_to.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTodayOrders();
            }
        });

        getTodayOrders();
        return view;


    }

    private void getInvoice(String cart_ids) {
//        bill = "";
        newbill = "";
        dialog.show();
        headListInvoice.clear();
        invoiceStringList.clear();
        dateInvoiceList.clear();
        newProductModels.clear();
        tailPartInvoice.clear();
        totalPoint.clear();
        StringRequest request = new StringRequest(StringRequest.Method.POST, BaseURL.getInvoice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("invoice", response);
                if (response != null && response.length() > 2) {

                    Gson gson = new Gson();
                    invoiceModel = gson.fromJson(response, InvoiceModel.class);

                    headListInvoice.add("SORTTED DAILY");
                    headListInvoice.add("-----------------------------------------------");
                    List<String> addressstring = getSplittedString(invoiceModel.getAddress(), 25);
                    headListInvoice.add(invoiceModel.getName());
                    headListInvoice.addAll(addressstring);
                    headListInvoice.add(invoiceModel.getNumber());
                    headListInvoice.add("City: " + invoiceModel.getCity());
                    headListInvoice.add("Pin: " + invoiceModel.getPincode());
                    headListInvoice.add("-----------------------------------------------");
                    dateInvoiceList.add(String.format("%1$-30s", "Invoice No : " + invoiceModel.getInvoice_no()));
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String date = dateFormat.format(calendar.getTime());
                    dateInvoiceList.add("Date : " + date + "");
                    invoiceStringList.add("-----------------------------------------------");
                    invoiceStringList.add(String.format("%1$-17s", "Item"));
                    invoiceStringList.add("Qty      Price     Amt ");
                    invoiceStringList.add("-----------------------------------------------");
                    int qtyTotal = 0;
                    double orderAmount = 0.0;
                    for (int i = 0; i < invoiceModel.getOrder_details().size(); i++) {
                        qtyTotal = qtyTotal + Integer.valueOf(invoiceModel.getOrder_details().get(i).getQty().trim());
                        orderAmount = orderAmount + Double.parseDouble(invoiceModel.getOrder_details().get(i).getPrice().trim());
                        double priced = (Double.parseDouble(invoiceModel.getOrder_details().get(i).getPrice().trim()) / Double.parseDouble(invoiceModel.getOrder_details().get(i).getQty().trim()));
                        String productNewName = invoiceModel.getOrder_details().get(i).getProduct_name() + " (" + invoiceModel.getOrder_details().get(i).getQuantity() + "" + invoiceModel.getOrder_details().get(i).getUnit() + ")";
                        int productlen = productNewName.length();
                        if (productlen < 22) {
                            newProductModels.add(new InvoiceNewProductModel(new ArrayList<String>(), String.format("%1$-1s %2$4s %3$10s %4$9s", concatedString(productNewName, productNewName.length()), invoiceModel.getOrder_details().get(i).getQty().trim(), priced, Double.parseDouble(invoiceModel.getOrder_details().get(i).getPrice().trim()))));
                        } else {
                            String input = productNewName;
                            List<String> splitedString = getSplittedString(input, 22);
                            if (splitedString.size() > 1) {
                                List<String> newsss = new ArrayList<>();
                                newsss.addAll(splitedString);
                                newsss.remove(0);
                                Log.i("TAG", splitedString.toString());
                                Log.i("TAG", newsss.toString());
                                Log.i("TAG", splitedString.toString());
                                newProductModels.add(new InvoiceNewProductModel(newsss, String.format("%1$-1s %2$4s %3$10s %4$9s", splitedString.get(0), invoiceModel.getOrder_details().get(i).getQty().trim(), priced, Double.parseDouble(invoiceModel.getOrder_details().get(i).getPrice().trim()))));
                            } else {
                                newProductModels.add(new InvoiceNewProductModel(new ArrayList<String>(), String.format("%1$-1s %2$4s %3$10s %4$9s", splitedString.get(0), invoiceModel.getOrder_details().get(i).getQty().trim(), priced, Double.parseDouble(invoiceModel.getOrder_details().get(i).getPrice().trim()))));
                            }
                        }
                    }
                    newbill = newbill + "-----------------------------------------------\n";
                    String orderAmt = session_management.getCurrency() + "" + orderAmount;
                    switch (orderAmt.length()) {
                        case 9:
                            newbill = newbill + String.format("%1$-2s %2$20s %3$17s", "SubTotal", qtyTotal, "   " + orderAmt) + "\n";
                            break;
                        case 10:
                            newbill = newbill + String.format("%1$-2s %2$19s %3$16s", "SubTotal", qtyTotal, "   " + orderAmt) + "\n";
                            break;
                        case 11:
                            newbill = newbill + String.format("%1$-2s %2$18s %3$15s", "SubTotal", qtyTotal, "   " + orderAmt) + "\n";
                            break;
                        case 12:
                            newbill = newbill + String.format("%1$-2s %2$17s %3$14s", "SubTotal", qtyTotal, "   " + orderAmt) + "\n";
                            break;
                        case 13:
                            newbill = newbill + String.format("%1$-2s %2$16s %3$13s", "SubTotal", qtyTotal, "   " + orderAmt) + "\n";
                            break;
                        case 14:
                            newbill = newbill + String.format("%1$-2s %2$15s %3$12s", "SubTotal", qtyTotal, "   " + orderAmt) + "\n";
                            break;
                        default:
                            newbill = newbill + String.format("%1$-2s %2$19s %3$18s", "SubTotal", qtyTotal, "   " + orderAmt) + "\n";
                            break;
                    }

                    newbill = newbill + "-----------------------------------------------";
//                    tailPartInvoice.add("Total price   " + invoiceModel.getTotal_price());
                    tailPartInvoice.add("(Inc. all taxes)");
//                    tailPartInvoice.add("\n");
                    tailPartInvoice.add("Paid by wallet - " + invoiceModel.getPaid_by_wallet());
                    tailPartInvoice.add("Coupon discount - " + invoiceModel.getCoupon_discount());
                    tailPartInvoice.add("Delivery charges - " + invoiceModel.getDelivery_charge());
//                    tailPartInvoice.add("________________");
//                    tailPartInvoice.add("Price to pay   " + invoiceModel.getPrice_to_pay());
                    totalPoint.add("-----------------------------------------------");
                    String totalpay = session_management.getCurrency() + "" + Double.parseDouble(invoiceModel.getPrice_to_pay());
                    Log.i("length", "" + totalpay.length());

                    switch (totalpay.length()) {
                        case 9:
                            totalPoint.add(String.format("%1$-38s", "TOTAL"));
                            break;
                        case 10:
                            totalPoint.add(String.format("%1$-36s", "TOTAL"));
                            break;
                        case 11:
                            totalPoint.add(String.format("%1$-34s", "TOTAL"));
                            break;
                        case 12:
                            totalPoint.add(String.format("%1$-32s", "TOTAL"));
                            break;
                        case 13:
                            totalPoint.add(String.format("%1$-30s", "TOTAL"));
                            break;
                        case 14:
                            totalPoint.add(String.format("%1$-28s", "TOTAL"));
                            break;
                        default:
                            totalPoint.add(String.format("%1$-40s", "TOTAL"));
                            break;
                    }
                    totalPoint.add(totalpay);
                    totalPoint.add("-----------------------------------------------");
//                    bill = bill+"-----------------------------------------------\n";
//                    bill = bill+String.format("%1$-5s %2$30s","TOTAL",session_management.getCurrency()+""+invoiceModel.getPrice_to_pay())+"\n";
//                    bill = bill+"-----------------------------------------------";

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
//                    Log.i("tag", "\n" + headListInvoice.toString() + "\n" + invoiceStringList.toString() + "\n" + tailPartInvoice.toString());
                    newCart_id = cart_ids;
//                    for (int a1=0;a1<headListInvoice.size();a1++){
//                        Log.i("in",headListInvoice.get(a1)+"\n");
//                    }
//                    Log.i("in",dateInvoiceList.get(0)+""+dateInvoiceList.get(1));
//                    for (int a1=0;a1<invoiceStringList.size();a1++){
//                        Log.i("in",invoiceStringList.get(a1)+"\n");
//                    }
//                    for (int a1 = 0; a1 < newProductModels.size(); a1++) {
////                        Log.i("in",newProductModels.get(a1).getProductName()+"\n"+newProductModels.get(a1).getProductDescp()+"\n");
//                        Log.i("in", newProductModels.get(a1).getProductDescp() + "\n");
//                    }
//                    Log.i("in","\n"+newbill);
//                    for (int a1=0;a1<tailPartInvoice.size();a1++){
//                        Log.i("in",tailPartInvoice.get(a1)+"\n");
//                    }
//                    Log.i("in",headListInvoice.toString());
//                    Log.i("in",invoiceStringList.toString());
//                    Log.i("in",newProductModels.toString());
//                    Log.i("in",newbill);
//                    Log.i("in",tailPartInvoice.toString());
//                    Log.i("in",bill);
                    showBluetoothDialog(view, cart_ids);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cart_id", cart_ids);
                return params;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(request);

    }

    public List<String> getSplittedString(String stringtoSplit,
                                          int length) {

        List<String> returnStringList = new ArrayList<String>(
                (stringtoSplit.length() + length - 1) / length);

        for (int start = 0; start < stringtoSplit.length(); start += length) {
            returnStringList.add(stringtoSplit.substring(start,
                    Math.min(stringtoSplit.length(), start + length)));
        }

        return returnStringList;
    }

    public byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + byteToHex(b[k]));
        }

        return b[3];
    }

//    public String charToHex(char c) {
//        // Returns hex String representation of char c
//        byte hi = (byte) (c >>> 8);
//        byte lo = (byte) (c & 0xff);
//        return byteToHex(hi) + byteToHex(lo);
//    }

//    private void showBluetooh() {
//        if (mBluetoothSocket == null) {
//            Intent BTIntent = new Intent(context, BluetoothActivity.class);
//            this.startActivityForResult(BTIntent, 11);
//        } else {
//            prinbill();
//        }
//    }

    public String byteToHex(byte b) {
        // Returns hex String representation of byte b
        char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        char[] array = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
        return new String(array);
    }

    private String concatedString(String selectedstring, int slenght) {

        int restLength = 22 - slenght;
        String conString = selectedstring;
        if (restLength < 2) {
            conString = conString + " ";
        } else {
            for (int i = 0; i < restLength; i++) {
                conString = conString + " ";
            }
        }
        return conString;
    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                tabView.setMinimumWidth(0);
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        setMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        setMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        setMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void setMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    private void getTodayOrders() {
        store_id = sharedPreferences.getString("id", "");
        dialog.show();
        movieList.clear();
        nextdayOrderModels.clear();
        String tag_json_obj = "json_login_req";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("store_id", store_id);

        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.storeassigned_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response);
                try {
                    if (response.contains("no orders found")) {
                        Toast.makeText(context, "No Today's Order found!", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = new JSONArray(response);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<TodayOrderModel>>() {
                        }.getType();
                        List<TodayOrderModel> orderModels = gson.fromJson(jsonArray.toString(), listType);
                        movieList.addAll(orderModels);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    getNextDayOrders();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getNextDayOrders();
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


//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseURL.storeassigned_url, params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getNextDayOrders() {
        String tag_json_obj = "json_login_req";


        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.storeunassigned_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response);
                try {
                    if (response.contains("no orders found")) {
                        Toast.makeText(context, "No Nextday's Order found", Toast.LENGTH_SHORT).show();
                        if (movieList.size() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        JSONArray jsonArray = new JSONArray(response);
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NextdayOrderModel>>() {
                        }.getType();
                        List<NextdayOrderModel> nextdayOrderModels1 = gson.fromJson(jsonArray.toString(), listType);
                        nextdayOrderModels.addAll(nextdayOrderModels1);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    swipe_to.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (movieList.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
                swipe_to.setRefreshing(false);
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

    private void getBoysList(String viewType, int positiond) {
        dialog.show();
        boyModels.clear();
        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.getBoyList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewDeliveryBoyModel>>() {
                        }.getType();
                        List<NewDeliveryBoyModel> nextdayOrderModels1 = gson.fromJson(jsonObject.getString("data"), listType);
                        boyModels.addAll(nextdayOrderModels1);
                        selectBoyDialog(viewType, positiond);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
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

    private void selectBoyDialog(String viewType, int positiondd) {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delivery_boy_list);
//        dialog.getWindow().getDecorView().setTop(100);
//        dialog.getWindow().getDecorView().setLeft(100);

        RecyclerView listRecy = dialog.findViewById(R.id.boy_list);
        DeliveryBoyAdapter deliveryBoyAdapter = new DeliveryBoyAdapter(context, boyModels, new DeliveryBoyListClick() {
            @Override
            public void onClick(int position) {
                dialog.dismiss();
                boy_id = boyModels.get(position).getDboy_id();
                if (viewType.equalsIgnoreCase("today")) {
                    todayPosition = positiondd;
                    unassPosition = -1;
                } else {
                    unassPosition = positiondd;
                    todayPosition = -1;
                }
                if (boyModels.size() > 0 && !boy_id.equalsIgnoreCase("")) {
                    assignBoyToOrder(boyModels.get(position).getDboy_id());
                }

            }
        });
        listRecy.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listRecy.setAdapter(deliveryBoyAdapter);
        dialog.show();

    }

    private void assignBoyToOrder(final String dboy_id) {
        dialog.show();
//        boyModels.clear();
        StringRequest request = new StringRequest(Request.Method.POST, BaseURL.assignBoyToOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getTodayOrders();
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<NewDeliveryBoyModel>>() {
//                        }.getType();
//                        List<NewDeliveryBoyModel> nextdayOrderModels1 = gson.fromJson(jsonObject.getString("data"), listType);
//                        boyModels.addAll(nextdayOrderModels1);
                    } else {
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
                if (todayPosition != -1) {
                    params.put("cart_id", movieList.get(todayPosition).getCart_id());
                } else if (unassPosition != -1) {
                    params.put("cart_id", nextdayOrderModels.get(unassPosition).getCart_id());
                }
                params.put("dboy_id", dboy_id);
//                params.put("store_id", store_id);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7) {
            if (data != null && Objects.requireNonNull(data.getStringExtra("runapi")).equalsIgnoreCase("true")) {
                getTodayOrders();
            }
        } else if (requestCode == 11) {
            UUID uuid = UUID.fromString(data.getStringExtra("uuid"));
            if (mBluetoothSocket != null) {
                try {
                    mBluetoothSocket.close();
                    mBluetoothSocket = null;
                    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                    mBluetoothSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (mBluetoothSocket != null) {
                        prinbill();
                    } else {
                        Toast.makeText(context, "Connection not establish", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }
    }

    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
//            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), img);
            Bitmap bmp = drawableToBitmap(context.getResources().getDrawable(img));
            print_image(bmp);
//            if (bmp != null) {
//                byte[] command = Utils.decodeBitmap(bmp);
//                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
//                printText(command);
//            } else {
//                Log.e("Print Photo error", "the file isn't exists");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    private void print_image(Bitmap bmp) throws IOException {
        if (bmp != null) {
//            Bitmap bmp = BitmapFactory.decodeFile(file);
//            convertBitmap(bmp);
            outputStream.write(PrinterCommands.SET_LINE_SPACING_24);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                outputStream.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        outputStream.write(slice);
                    }
                }
                offset += 24;
                outputStream.write(PrinterCommands.FEED_LINE);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
//                mmOutputStream.write(PrinterCommands.FEED_LINE);
            }
            outputStream.write(PrinterCommands.SET_LINE_SPACING_30);


        } else {
//            Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //print unicode
    public void printUnicode() {
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void resetPrint() {
        try {
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 31) {
            int n = (22 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String[] dateTime = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

//    public Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}

