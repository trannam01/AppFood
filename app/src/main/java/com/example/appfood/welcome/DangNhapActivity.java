package com.example.appfood.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.appfood.activity.MainActivity;
import com.example.lib.common.Url;
import com.example.lib.common.Validate;

import java.util.HashMap;
import java.util.Map;

public class DangNhapActivity extends AppCompatActivity {
    Toolbar toolbar_DangNhap;
    TextView tvRegister_login, tvForgetPassword_login, message_phone, message_password;
    Button btnLogin_login;
    ImageButton imgShowpass;
    EditText user_phone_login, user_password_login;
    private static final String FILE_NAME = "myFile";
    boolean VISIBLE_PASSWORD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        getViewId();
        actionToolbar();
        event();
    }

    private void tooglePassWord() {
        if (VISIBLE_PASSWORD) {
            VISIBLE_PASSWORD = false;
            imgShowpass.setImageResource(R.drawable.ic_showpass);
            user_password_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            VISIBLE_PASSWORD = true;
            imgShowpass.setImageResource(R.drawable.ic_hintpass);
            user_password_login.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_DangNhap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_DangNhap.setNavigationOnClickListener(view -> finish());
        tvRegister_login.setOnClickListener(view -> startActivity(new Intent(DangNhapActivity.this, com.example.appfood.welcome.DangKyActivity.class)));
        tvForgetPassword_login.setOnClickListener(view -> startActivity(new Intent(DangNhapActivity.this, QuenMatKhauActivity.class)));
    }

    private void getViewId() {
        toolbar_DangNhap = findViewById(R.id.toolbar_DangNhap);
        btnLogin_login = findViewById(R.id.btnLogin_login);
        user_phone_login = findViewById(R.id.user_phone_login);
        user_password_login = findViewById(R.id.user_password_login);
        tvRegister_login = findViewById(R.id.tvRegister_login);
        tvForgetPassword_login = findViewById(R.id.tvForgetPassword_login);
        message_phone = findViewById(R.id.message_phone);
        message_password = findViewById(R.id.message_password);
        imgShowpass = findViewById(R.id.imgshowhide1);
    }

    private void event() {
        btnLogin_login.setOnClickListener(view -> {
            String user_phone = user_phone_login.getText().toString();
            String user_password = user_password_login.getText().toString();
            checkLogin(user_phone, user_password);
        });

        imgShowpass.setOnClickListener(view -> {
            tooglePassWord();
        });
    }

    private void checkLogin(String user_phone,String user_password) {
        if(Validate.isValidValue(user_phone, message_phone) && Validate.isValidValue(user_password, message_password)){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.checkSigIn, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("Success")){
                        rememberPW(user_phone, user_password);
                        startActivity(new Intent(DangNhapActivity.this, MainActivity.class));
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(DangNhapActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DangNhapActivity.this, "e" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("sdt", user_phone);
                    param.put("matkhau", user_password);
                    return param;
                }
            };
            requestQueue.add(stringRequest);
        }else {
            Validate.isValidValue(user_phone, message_phone);
            Validate.isValidValue(user_password, message_password);
        }
    }

    private void rememberPW(String phone,String password){
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("user_phone",phone);
        editor.putString("user_password",password);
        editor.apply();
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(trangchu);
    }
}