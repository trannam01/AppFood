package com.example.appfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appfood.R;
import com.example.appfood.welcome.WelcomeActivity;
import com.example.lib.common.NetworkConnection;
import com.example.lib.common.Show;
import com.example.lib.common.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TaiKhoanActivity extends AppCompatActivity {
    Toolbar toolbar_TaiKhoan;
    TextView thongbao_soluong, user_name, user_phone, user_mail;
    Button btnLogout;
    private static final String FILE_NAME = "myFile";
    private static final String KEY = "Show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);
        getViewId();
        actionToolbar();
        //postDL();
        checkCardView();
        //check network
        if(NetworkConnection.isConnected(this)) {
            Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
        eventLinearLogin();
    }

    private void eventLinearLogin() {
        btnLogout.setOnClickListener(view -> {
            //startActivity(new Intent(TaiKhoanActivity.this, WelcomeActivity.class));
            SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
            editor.putString("user_phone","");
            editor.putString("user_password","");
            editor.apply();
            finish();
            startActivity(getIntent());
        });
    }


    private void checkCardView() {
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String user_phone = sharedPreferences.getString("user_phone", "");
        String user_password = sharedPreferences.getString("user_password", "");
        if(user_phone.equals("") && user_password.equals("")){
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }
        getThongTinUser(user_phone);
    }

    private void getThongTinUser(String user_phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.thongTinKH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<=jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("tenkh");
                            String sdt = jsonObject.getString("sdt");
                            String email = jsonObject.getString("email");
                            HienThiDulieu(name, sdt, email);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TaiKhoanActivity.this, "e" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("sdt", user_phone);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void HienThiDulieu(String name, String sdt, String email) {
        user_name.setText(name);
        user_mail.setText(sdt);
        user_phone.setText(email);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_TaiKhoan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_TaiKhoan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getViewId() {
        toolbar_TaiKhoan = findViewById(R.id.toolbar_TaiKhoan);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);
        btnLogout = findViewById(R.id.btnLogout);
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        user_mail = findViewById(R.id.user_mail);
    }

    /*private void postDL() {
        String tenkh = ThongTinKhachHangActivity.user_name.getText().toString();
        String email = ThongTinKhachHangActivity.user_mail.getText().toString();
        String sdt = user_phone.getText().toString();

        Intent intent = new Intent(this, ThongTinKhachHangActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ThongTinKhachHangActivity.KEY, tenkh);
        bundle.putString(ThongTinKhachHangActivity.KEY, email);
        bundle.putString(ThongTinKhachHangActivity.KEY, sdt);
        intent.putExtras(bundle);
        startActivity(intent);
    }*/

    private void rememberPW(String phone,String password){
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("user_phone",phone);
        editor.putString("user_password",password);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
    }


    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }

    public void openCart(View view) {
        Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
        startActivity(giohang);
    }
}