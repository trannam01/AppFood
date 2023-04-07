package com.example.appfood.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.lib.common.Url;
import com.example.lib.common.Validate;

import java.util.HashMap;
import java.util.Map;

public class QuenMatKhauActivity extends AppCompatActivity {
    TextView tvLogin_forgetpw, tvRegister_forgetpw, message_phone, message_mail, message_password, message_repassword;;
    Toolbar toolbar_QuenMatKhau;
    Button btnConfirm_forgetpw;
    EditText user_phone_forget, user_mail_forget, user_password_fogetpw, user_repassword_forgetpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        getViewId();
        actionToolbar();
        event();
    }

    private void event() {
        btnConfirm_forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_phone = user_phone_forget.getText().toString();
                String user_mail = user_mail_forget.getText().toString();
                String user_password = user_password_fogetpw.getText().toString();
                String user_repassword = user_repassword_forgetpw.getText().toString();
                if(Validate.isValidPhone(user_phone, 11, message_phone) &&
                        Validate.isValidEmail(user_mail, message_mail) &&
                        Validate.isValidValue(user_password, message_password) &&
                        Validate.isValidValue(user_repassword, message_repassword)){
                    if(user_repassword.equals(user_password)){
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.checkSigUp, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("Fail")){
                                    Toast.makeText(QuenMatKhauActivity.this, "Số điện thoại chưa đăng ký!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Forget(user_phone, user_mail, user_password);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(QuenMatKhauActivity.this, "e" + error.toString(), Toast.LENGTH_SHORT).show();
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
                    }else {
                        Toast.makeText(QuenMatKhauActivity.this, "Mật khẩu không trùng nhau!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Validate.isValidPhone(user_phone,11 ,message_phone);
                    Validate.isValidEmail(user_mail, message_mail);
                    Validate.isValidValue(user_password, message_password);
                    Validate.isValidValue(user_repassword, message_repassword);
                }
            }
        });
    }

    private void Forget(String user_phone, String user_mail, String user_password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.forgetPass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    //rememberPW(user_phone, user_password);
                    startActivity(new Intent(QuenMatKhauActivity.this, DangNhapActivity.class));
                    Toast.makeText(QuenMatKhauActivity.this, "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                    //startActivity(getIntent());
                }else {
                    Toast.makeText(QuenMatKhauActivity.this, "Thay đổi mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuenMatKhauActivity.this, "e" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("sdt", user_phone);
                param.put("email", user_mail);
                param.put("matkhau", user_password);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_QuenMatKhau);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_QuenMatKhau.setNavigationOnClickListener(view -> finish());
        tvLogin_forgetpw.setOnClickListener(view -> startActivity(new Intent(QuenMatKhauActivity.this, com.example.appfood.welcome.DangNhapActivity.class)));
        tvRegister_forgetpw.setOnClickListener(view -> startActivity(new Intent(QuenMatKhauActivity.this, com.example.appfood.welcome.DangKyActivity.class)));
    }

    private void getViewId() {
        toolbar_QuenMatKhau = findViewById(R.id.toolbar_QuenMatKhau);
        tvLogin_forgetpw = findViewById(R.id.tvLogin_forgetpw);
        tvRegister_forgetpw = findViewById(R.id.tvRegister_forgetpw);
        btnConfirm_forgetpw = findViewById(R.id.btnConfirm_forgetpw);
        message_phone = findViewById(R.id.message_phone);
        message_mail = findViewById(R.id.message_mail);
        message_password = findViewById(R.id.message_password);
        message_repassword = findViewById(R.id.message_repassword);
        user_phone_forget= findViewById(R.id.user_phone_forgetpw);
        user_mail_forget = findViewById(R.id.user_mail_forgetpw);
        user_password_fogetpw = findViewById(R.id.user_password_fogetpw);
        user_repassword_forgetpw = findViewById(R.id.user_repassword_forgetpw);
    }
}