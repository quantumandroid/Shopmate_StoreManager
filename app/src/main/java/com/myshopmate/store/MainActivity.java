package com.myshopmate.store;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.franmontiel.localechanger.LocaleChanger;
import com.google.android.material.navigation.NavigationView;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.Dashboard.AllProducts;
import com.myshopmate.store.Dashboard.AppUserActivity;
import com.myshopmate.store.Dashboard.EarningsActivity;
import com.myshopmate.store.Dashboard.EditProfile;
import com.myshopmate.store.Dashboard.OrdersActivity;
import com.myshopmate.store.Dashboard.Select_Products;
import com.myshopmate.store.Dashboard.StockList;
import com.myshopmate.store.Dashboard.StockShow;
import com.myshopmate.store.Fonts.CustomTypefaceSpan;
import com.myshopmate.store.Fragments.Home_fragment;
import com.myshopmate.store.NetworkConnectivity.NoInternetConnection;
import com.myshopmate.store.util.ConnectivityReceiver;
import com.myshopmate.store.util.CustomVolleyJsonRequest;
import com.myshopmate.store.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static String currency_sign;
    ImageView imageView;
    Toolbar toolbar;
    int padding = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView tv_name;
    private CircleImageView iv_profile;
    private Menu nav_menu;
    private Session_management sessionManagement;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar
        sharedPreferences = getSharedPreferences("lan", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();

        editor.putString("language", "english");
        editor.apply();


        editor.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());


        setSupportActionBar(toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "Font/Bold.otf");
                textView.setTypeface(myCustomFont);
            }


        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }


        sessionManagement = new Session_management(MainActivity.this);
//        //Navigation Menu

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (
                int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        View headerView = navigationView.getHeaderView(0);
        navigationView.getBackground().

                setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        iv_profile = header.findViewById(R.id.iv_header_img);
        tv_name = (TextView) header.findViewById(R.id.tv_header_name);
        new Thread(this::getCurrency).start();
        updateHeader();
        sideMenu();
//        currency();

        if (savedInstanceState == null) {
            Fragment fm = new Home_fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {

                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);

                    if (fm_name.contentEquals("Home_fragment")) {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        toggle.setDrawerIndicatorEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();

                    } else if (fm_name.contentEquals("My_order_fragment") ||
                            fm_name.contentEquals("Thanks_fragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment fm = new Home_fragment();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                        .addToBackStack(null).commit();
                            }
                        });
                    } else {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                onBackPressed();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void updateHeader() {
        if (sessionManagement.isLoggedIn()) {
            String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
            String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
            if (getimage != null && !getimage.equalsIgnoreCase("")) {
                Glide.with(this)
                        .load(BaseURL.IMG_PROFILE_URL + getimage)
                        .placeholder(R.drawable.icons)
                        .into(iv_profile);
            }
            tv_name.setText(getname);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Font/Bold.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);

    }

    public void sideMenu() {
        if (sessionManagement.isLoggedIn()) {
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
        } else {
            /*tv_name.setText(getResources().getString(R.string.btn_login));*/
            /*tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });*/
            nav_menu.findItem(R.id.nav_logout).setVisible(false);
        }
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.nav_dashboard) {
            Fragment fm_home = new Home_fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm_home, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        } else if (id == R.id.nav_selectproduct) {
            Intent intent = new Intent(MainActivity.this, Select_Products.class);
            startActivity(intent);
        } else if (id == R.id.nav_app_user) {
            Intent intent = new Intent(MainActivity.this, AppUserActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(MainActivity.this, EditProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_product_list) {
            Intent intent = new Intent(MainActivity.this, AllProducts.class);
            startActivity(intent);
        } else if (id == R.id.nav_stock_update) {
            Intent intent = new Intent(MainActivity.this, StockShow.class);
            startActivity(intent);
        } else if (id == R.id.nav_earnings) {
            Intent intent = new Intent(MainActivity.this, EarningsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_order) {
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stock) {
            Intent intent = new Intent(MainActivity.this, StockList.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            sessionManagement.logoutSession();
        }

        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception ignored) {
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {

        if (!isConnected) {
            Intent intent = new Intent(MainActivity.this, NoInternetConnection.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.language, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    private void openLanguageDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_language, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        TextView lEnglish = v.findViewById(R.id.l_english);
        TextView lSpanish = v.findViewById(R.id.l_arabic);
        final AlertDialog dialog = builder.create();

        lEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleChanger.setLocale(Locale.ENGLISH);
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                editor.putString("language", "english");
                editor.apply();


                recreate();
                dialog.dismiss();
            }
        });
        lSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleChanger.setLocale(new Locale("ar", "ARABIC"));
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                editor.putString("language", "spanish");
                editor.apply();
                recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void currency() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        //params.put("cityadmin_id","1");

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.GET, BaseURL.currency, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONObject jsonObject = response.getJSONObject("data");
                        currency_sign = jsonObject.getString("currency_sign");


//                            Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_SHORT).show();

                    } else {

//                        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void getCurrency() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseURL.currencyApi, response -> {
            Log.d("currency api", response);

            try {

                JSONObject currencyObject = new JSONObject(response);
                if (currencyObject.getString("status").equalsIgnoreCase("1") && currencyObject.getString("message").equalsIgnoreCase("currency")) {

                    JSONObject dataObject = currencyObject.getJSONObject("data");

                    sessionManagement.setCurrency(dataObject.getString("currency_name"), dataObject.getString("currency_sign"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}