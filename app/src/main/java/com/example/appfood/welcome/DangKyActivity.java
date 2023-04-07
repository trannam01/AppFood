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

public class DangKyActivity extends AppCompatActivity {

    Button btnRegister_register;
    Toolbar toolbar_DangKy;
    EditText user_name_register, user_phone_register, user_mail_register, user_password_register, user_repassword_register;
    TextView tvLogin_register, tvForgetPassword_register, message_name, message_phone, message_mail, message_password, message_repassword;
    ImageButton imgShowpass;

    private static final String FILE_NAME = "myFile";
    boolean VISIBLE_PASSWORD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        getViewId();
        actionToolbar();
        event();
    }

    private void tooglePassWord() {
        if (VISIBLE_PASSWORD) {
            VISIBLE_PASSWORD = false;
            imgShowpass.setImageResource(R.drawable.ic_showpass);
            user_password_register.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            user_repassword_register.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            VISIBLE_PASSWORD = true;
            imgShowpass.setImageResource(R.drawable.ic_hintpass);
            user_password_register.setInputType(InputType.TYPE_CLASS_TEXT);
            user_repassword_register.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    private void event() {
        btnRegister_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = user_name_register.getText().toString();
                String user_phone = user_phone_register.getText().toString();
                String user_mail = user_mail_register.getText().toString();
                String user_password = user_password_register.getText().toString();
                String user_repassword = user_repassword_register.getText().toString();
                if(Validate.isValidValue(user_name, message_name) &&
                        Validate.isValidPhone(user_phone, 11, message_phone) &&
                        Validate.isValidEmail(user_mail, message_mail) &&
                        Validate.isValidValue(user_password, message_password) &&
                        Validate.isValidValue(user_repassword, message_repassword)){
                    if(user_repassword.equals(user_password)){
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.checkSigUp, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("Fail")){
                                    SigUp(user_name, user_phone, user_mail, user_password);
                                }else {
                                    Toast.makeText(DangKyActivity.this, "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(DangKyActivity.this, "e" + error.toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DangKyActivity.this, "Mật khẩu không trùng nhau!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Validate.isValidValue(user_name, message_name);
                    Validate.isValidPhone(user_phone,11 ,message_phone);
                    Validate.isValidEmail(user_mail, message_mail);
                    Validate.isValidValue(user_password, message_password);
                    Validate.isValidValue(user_repassword, message_repassword);
                }
            }
        });

        imgShowpass.setOnClickListener(view -> {
            tooglePassWord();
        });
    }

    private void SigUp(String user_name, String user_phone, String user_mail, String user_password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.sigUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Done")){
                    rememberPW(user_phone, user_password);
                    startActivity(new Intent(DangKyActivity.this, MainActivity.class));
                    Toast.makeText(DangKyActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                    //startActivity(getIntent());
                }else {
                    Toast.makeText(DangKyActivity.this, "Đăng ký tài khoản không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DangKyActivity.this, "e" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("tenkh", user_name);
                param.put("sdt", user_phone);
                param.put("email", user_mail);
                param.put("matkhau", user_password);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void rememberPW(String phone,String password){
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("user_phone",phone);
        editor.putString("user_password",password);
        editor.apply();
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_DangKy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_DangKy.setNavigationOnClickListener(view -> finish());
        tvLogin_register.setOnClickListener(view -> startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class)));
        tvForgetPassword_register.setOnClickListener(view -> startActivity(new Intent(DangKyActivity.this, QuenMatKhauActivity.class)));
    }

    private void getViewId() {
        toolbar_DangKy = findViewById(R.id.toolbar_DangKy);
        tvLogin_register = findViewById(R.id.tvLogin_register);
        tvForgetPassword_register = findViewById(R.id.tvForgetPassword_register);
        btnRegister_register = findViewById(R.id.btnRegister_register);
        message_name = findViewById(R.id.message_name);
        message_phone = findViewById(R.id.message_phone);
        message_mail = findViewById(R.id.message_mail);
        message_password = findViewById(R.id.message_password);
        message_repassword = findViewById(R.id.message_repassword);
        user_name_register = findViewById(R.id.user_name_register);
        user_phone_register = findViewById(R.id.user_phone_register);
        user_mail_register = findViewById(R.id.user_mail_register);
        user_password_register = findViewById(R.id.user_password_register);
        user_repassword_register = findViewById(R.id.user_repassword_register);
        imgShowpass = findViewById(R.id.imgshowhide1);
    }
}