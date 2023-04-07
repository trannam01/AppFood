package com.example.appfood.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appfood.R;
import com.example.lib.common.NetworkConnection;
import com.example.lib.common.Show;
import com.example.lib.common.Url;
import com.example.lib.common.Validate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ThongTinKhachHangActivity extends AppCompatActivity {
    Toolbar toolbarThanhToan;
    static Button btn_xacnhanthanhtoan;
    public static EditText user_name;
    static EditText user_address;
    static EditText user_phone;
    static EditText user_note;
    TextView textview_tongtien;
    static TextView message_name;
    static TextView message_address;
    static TextView message_phone;

    String sdt;

    public static final String KEY = "Show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);
        getViewId();
        actionToolbar();
        if(NetworkConnection.isConnected(this)) {
            tinhTongTienGioHang();
            xacnhanthanhtoan();
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    public void xacnhanthanhtoan() {
        btn_xacnhanthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            //TaoDonHang
            public void onClick(View view) {
                final String name = user_name.getText().toString();
                final String address = user_address.getText().toString();
                final String phone = user_phone.getText().toString();
                final String totalPrice = String.valueOf(Show.tinhTongTien());;
                final String note = user_note.getText().toString();

                if(Validate.isValidName(name,message_name)
                        && Validate.isValidAddress(address,message_address)
                        && Validate.isValidPhone(phone,11,message_phone)) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.postUserInfo, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
                            Log.d("id",madonhang);

                            //post chitietdonhang
                            if(Integer.parseInt(madonhang) > 0)
                            {
                                RequestQueue detailQueue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest detailRequest = new StringRequest(Request.Method.POST, Url.postBillDetail, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("1")) {
                                            Show.Notify(getApplicationContext(),getString(R.string.order_send_success));
                                            createNotification();
                                            //trở về home
                                            Intent thanhcong = new Intent(getApplicationContext(),SuccessCheckoutActivity.class);
                                            startActivity(thanhcong);
                                        }else{
                                            Show.Notify(getApplicationContext(),getString(R.string.order_send_error));
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Show.Notify(getApplicationContext(),getString(R.string.order_send_error));
                                    }
                                }){
                                    @Nullable
                                    @Override
                                    //post chitietdonhang
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for(int i=0; i < Show.listGiohang.size();i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("madonhang",madonhang);
                                                jsonObject.put("mamon",Show.listGiohang.get(i).getMamon());
                                                jsonObject.put("tenmon",Show.listGiohang.get(i).getTenmon());
                                                jsonObject.put("gia",Show.listGiohang.get(i).getGia());
                                                jsonObject.put("soluong",Show.listGiohang.get(i).getSoluong());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        //Gửi dữ liệu lên
                                        HashMap<String,String> hashMap = new HashMap<String,String>();
                                        hashMap.put("json",jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                detailQueue.add(detailRequest);
                            }
                        }
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Show.Notify(getApplicationContext(),getString(R.string.order_send_error));
                        }
                    }){
                        @Nullable
                        @Override
                        //post donhang
                        protected Map<String, String> getParams() throws AuthFailureError
                        {
                            HashMap<String,String> hashMap = new HashMap<String,String>();
                            hashMap.put("tenkhachhang",name);
                            hashMap.put("diachi",address);
                            hashMap.put("sodienthoai",phone);
                            hashMap.put("tongtien",totalPrice);
                            hashMap.put("ghichu",note);

                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                }else{
                    Validate.isValidName(name,message_name);
                    Validate.isValidAddress(address,message_address);
                    Validate.isValidPhone(phone,11,message_phone);
                }
            }
        });
    }

    private void getViewId() {
        toolbarThanhToan = findViewById(R.id.toolbarThanhToan);
        btn_xacnhanthanhtoan = findViewById(R.id.btn_xacnhanthanhtoan);
        user_name = findViewById(R.id.user_name);
        user_address = findViewById(R.id.user_address);
        user_phone = findViewById(R.id.user_phone);
        textview_tongtien= findViewById(R.id.textview_tongtien);
        message_name= findViewById(R.id.message_name);
        message_address= findViewById(R.id.message_address);
        message_phone= findViewById(R.id.message_phone);
        user_note= findViewById(R.id.user_note);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void tinhTongTienGioHang() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        textview_tongtien.setText(decimalFormat.format(Show.tinhTongTien())+" đ");
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tinhTongTienGioHang();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tinhTongTienGioHang();
    }

    /*private void getDL(String name, String email) {
        user_name.setText(name);
        user_email.setText(email);
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
                        String email = jsonObject.getString("email");
                        getDL(name, email);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThongTinKhachHangActivity.this, "e" + error.toString(), Toast.LENGTH_SHORT).show();
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
    }*/

    private void createNotification(){
        String id = "my_channel_id_01";
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = manager.getNotificationChannel(id);
            if(channel==null){
                channel = new NotificationChannel(id,"Chanel Title",NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("[Channel descriptioin]");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100,1000,200,340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);

            }
        }
        Intent notificationIntent = new Intent(this,NotificationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,1,notificationIntent,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id)
                .setSmallIcon(R.drawable.logo_app)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.checkin))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.checkin))
                        .bigLargeIcon(null))
                .setContentTitle("My Restaurant App")
                .setContentText("Bạn đã đặt hàng thành công!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100,1000,200,340})
                .setAutoCancel(false)
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(getApplicationContext());
        m.notify(1,builder.build());
    }
}