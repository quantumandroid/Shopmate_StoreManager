package com.myshopmate.store.Dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.franmontiel.localechanger.LocaleChanger;
import com.myshopmate.store.AppController;
import com.myshopmate.store.Config.BaseURL;
import com.myshopmate.store.MainActivity;
import com.myshopmate.store.R;
import com.myshopmate.store.util.ConnectivityReceiver;
import com.myshopmate.store.util.CustomVolleyJsonRequest;
import com.myshopmate.store.util.Session_management;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = LoginActivity.class.getSimpleName();
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String token;
    private RelativeLayout btn_continue;
    private EditText et_password, et_email;
    private TextView tv_password, tv_email, btn_forgot;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);


//        token = FirebaseInstanceId.getInstance().getToken();

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                token = instanceIdResult.getToken();

            }
        });
        et_password = (EditText) findViewById(R.id.et_login_pass);
        et_email = (EditText) findViewById(R.id.et_login_email);
        tv_password = (TextView) findViewById(R.id.tv_login_password);
        tv_email = (TextView) findViewById(R.id.tv_login_email);
        btn_continue = (RelativeLayout) findViewById(R.id.btnContinue);
        btn_forgot = (TextView) findViewById(R.id.btnForgot);

        btn_continue.setOnClickListener(this);
        btn_forgot.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnContinue) {
            attemptLogin();
        } else if (id == R.id.btnForgot) {
            Intent startRegister = new Intent(LoginActivity.this, ForgotActivity.class);
            startActivity(startRegister);
        }
    }

    private void attemptLogin() {

        tv_email.setText(getResources().getString(R.string.tv_login_email));
        tv_password.setText(getResources().getString(R.string.tv_login_password));
        tv_password.setTextColor(getResources().getColor(R.color.green));
        tv_email.setTextColor(getResources().getColor(R.color.green));
        String getpassword = et_password.getText().toString();
        String getemail = et_email.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(R.color.green));
            focusView = et_password;
            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(R.string.password_too_short));
            tv_password.setTextColor(getResources().getColor(R.color.green));
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {

            tv_email.setTextColor(getResources().getColor(R.color.green));
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(R.string.invalide_email_address));
            tv_email.setTextColor(getResources().getColor(R.color.green));
            focusView = et_email;
            cancel = true;
        }

        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            if (ConnectivityReceiver.isConnected()) {

                progressDialog.show();
                makeLoginRequest(getemail, getpassword);
            }
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeLoginRequest(String email, final String password) {

        if (token!=null && !token.equalsIgnoreCase("")){
            String tag_json_obj = "json_login_req";
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);
            params.put("device_id", token);

            CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.LOGIN_URL, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Tag", response.toString());

                    try {
                        progressDialog.dismiss();
                        String status = response.getString("status");
                        String message = response.getString("message");

                        if (status.contains("1")) {

                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject obj = jsonArray.getJSONObject(i);

                                String user_id = obj.getString("store_id");
                                SharedPreferences.Editor editor = getSharedPreferences("logindata", MODE_PRIVATE).edit();
                                editor.putString("id", user_id);
                                editor.apply();
                                String user_fullname = obj.getString("employee_name");
                                String user_email = obj.getString("email");
                                String user_phone = obj.getString("phone_number");
                                String password = obj.getString("password");
                                Session_management sessionManagement = new Session_management(LoginActivity.this);
                                sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, "", "", "", "", password);
                                btn_continue.setEnabled(false);
                                Intent i1 = new Intent(LoginActivity.this, MainActivity.class);
                                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i1);
                                finish();
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    finishAndRemoveTask();
//                                }else {
//                                    finish();
//                                }

                            }
                        } else {
                            progressDialog.dismiss();
//                        String error = response.getString("error");
                            Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            btn_continue.setEnabled(true);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }else {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {

                    token = instanceIdResult.getToken();
                    makeLoginRequest(email,password);
                }
            });
        }
    }


}
