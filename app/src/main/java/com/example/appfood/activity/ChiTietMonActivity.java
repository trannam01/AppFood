package com.example.appfood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.appfood.R;
import com.example.appfood.adapter.BinhLuanAdapter;
import com.example.lib.InterfaceResponsitory.AppFoodMethods;
import com.example.lib.RetrofitClient;
import com.example.lib.common.NetworkConnection;
import com.example.lib.common.Show;
import com.example.lib.common.Url;
import com.example.lib.model.BinhLuan;
import com.example.lib.model.ChiTietDonHang;
import com.example.lib.model.GioHang;
import com.example.lib.model.Mon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChiTietMonActivity extends AppCompatActivity {

    private static final String FILE_NAME = "myFile";
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppFoodMethods appFoodMethods;
    Toolbar toolbar_Chitietmon;
    ImageView hinhmon_chitiet,tru_giohang_ct,tru_giohang_ct_min, cong_giohang_ct;
    TextView tenmon_chitiet,gia_chitiet,mota_chitiet;
    Button btn_mua;
    TextView soluong_mon;
    EditText themBinhLuan;

    List<BinhLuan.Result> listBinhLuan;
    BinhLuanAdapter binhLuanAdapter;
    RecyclerView recyclerViewBinhLuan;

    TextView thongbao_soluong;
    Mon.Result monResult;
    ChiTietDonHang.Result donhangResult;
    String mamon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon);
        getViewId();
        actionToolbar();

        //check network
        if(NetworkConnection.isConnected(this)) {
            getChiTietMon();
            GetBinhLuan();
            Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
            actionBuy();
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    private void binhLuan() {
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String user_phone = sharedPreferences.getString("user_phone", "");
        String user_password = sharedPreferences.getString("user_password", "");
        if(user_phone.equals("") && user_password.equals("")){
            Toast.makeText(this, "Vui lòng nhập đăng nhập để bình luận!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            String binhluan = themBinhLuan.getText().toString();
            if(binhluan.equals("")){
                Toast.makeText(this, "Vui lòng nhập nội dung bình luận!", Toast.LENGTH_SHORT).show();
                return;
            }else{
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest request = new StringRequest(Request.Method.POST, Url.postBinhLuan
                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        themBinhLuan.setText("");
                        finish();
                        startActivity(getIntent());
                    }
                }, error -> {
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("mamon", mamon);
                        params.put("sdt", user_phone);
                        params.put("noidung", binhluan);
                        return params;
                    }
                };
                requestQueue.add(request);
            }
        }
    }

    private void actionBuy() {
        btn_mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
        tru_giohang_ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(soluong_mon.getText().toString()) - 1;
                soluong_mon.setText(""+soluong);
                if(Integer.parseInt(soluong_mon.getText().toString()) == 1){
                    tru_giohang_ct.setVisibility(View.GONE);
                    tru_giohang_ct_min.setVisibility(View.VISIBLE);
                }
            }
        });
        cong_giohang_ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(soluong_mon.getText().toString()) + 1;
                soluong_mon.setText(""+soluong);
                if(Integer.parseInt(soluong_mon.getText().toString()) > 1){
                    tru_giohang_ct.setVisibility(View.VISIBLE);
                    tru_giohang_ct_min.setVisibility(View.GONE);
                }
            }
        });
    }

    private void themGioHang() {
        if(Show.listGiohang.size() > 0) {
            boolean isExist = false;
            int soluong = Integer.parseInt(soluong_mon.getText().toString());
            for(int i = 0;i < Show.listGiohang.size(); i++ ) {
                if(Show.listGiohang.get(i).getMamon() == monResult.getId()) {
                    isExist = true;
                    //cộng dồn
                    int checkSoluong = soluong + Show.listGiohang.get(i).getSoluong();
                    Show.listGiohang.get(i).setSoluong(checkSoluong > 200 ? 200 : checkSoluong);
//                    long thanhtien = Long.parseLong(monResult.getGia()) * Show.listGiohang.get(i).getSoluong();
                }
            }
            if(!isExist) {
//                long thanhtien = Long.parseLong(monResult.getGia()) * soluong;
                GioHang giohang = new GioHang();
                giohang.setGia(Long.parseLong(monResult.getGia()));
                giohang.setMamon(monResult.getId());
                giohang.setTenmon(monResult.getTenmon());
                giohang.setHinhmon(monResult.getHinhmon());
                giohang.setMota(monResult.getMota());
                giohang.setSoluong(soluong);
                //Thêm vào giỏ
                Show.listGiohang.add(giohang);
            }
        }else{
            int soluong = Integer.parseInt(soluong_mon.getText().toString());
//            long thanhtien = Long.parseLong(monResult.getGia()) * soluong;
            GioHang giohang = new GioHang();
            giohang.setGia(Long.parseLong(monResult.getGia()));
            giohang.setMamon(monResult.getId());
            giohang.setTenmon(monResult.getTenmon());
            giohang.setHinhmon(monResult.getHinhmon());
            giohang.setMota(monResult.getMota());
            giohang.setSoluong(soluong);
            //Thêm vào giỏ
            Show.listGiohang.add(giohang);
        }
        // thongbao_soluong.setText(String.valueOf(Show.listGiohang.size())); // đếm theo loại
        Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
    }

    private void getChiTietMon() {
        monResult = (Mon.Result) getIntent().getSerializableExtra("chitietmon");
        mamon = String.valueOf(monResult.getId());
        tenmon_chitiet.setText(monResult.getTenmon());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        gia_chitiet.setText(decimalFormat.format(Double.parseDouble(monResult.getGia())) + " VNĐ");
        mota_chitiet.setText(monResult.getMota());
        Glide.with(getApplicationContext()).load(monResult.getHinhmon())
                .placeholder(R.drawable.img_default)
                .error(R.drawable.img_error)
                .into(hinhmon_chitiet);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Chitietmon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Chitietmon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @SuppressLint("WrongViewCast")
    private void getViewId() {
        themBinhLuan = findViewById(R.id.themBinhLuan);
        toolbar_Chitietmon = findViewById(R.id.toolbar_Chitietmon);
        hinhmon_chitiet = findViewById(R.id.hinhmon_chitiet);
        tenmon_chitiet = findViewById(R.id.tenmon_chitiet);
        gia_chitiet = findViewById(R.id.gia_chitiet);
        mota_chitiet = findViewById(R.id.mota_chitiet);
        soluong_mon = findViewById(R.id.soluong_mon);
        tru_giohang_ct = findViewById(R.id.tru_giohang_ct);
        tru_giohang_ct_min = findViewById(R.id.tru_giohang_ct_min);
        cong_giohang_ct = findViewById(R.id.cong_giohang_ct);
        btn_mua = findViewById(R.id.btn_mua);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);
        tru_giohang_ct.setVisibility(View.GONE);
        tru_giohang_ct_min.setVisibility(View.VISIBLE);
        recyclerViewBinhLuan = findViewById(R.id.listBinhLuan);
        listBinhLuan = new ArrayList<>();
        appFoodMethods = RetrofitClient.getRetrofit(Url.AppFood_Url).create(AppFoodMethods.class);
        //set kiểu layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerViewBinhLuan.setLayoutManager(layoutManager);
        recyclerViewBinhLuan.setHasFixedSize(true);
        themBinhLuan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                binhLuan();
                return false;
            }
        });
    }

    private void GetBinhLuan() {
        compositeDisposable.add(appFoodMethods.POST_BinhLuan(mamon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        binhluan -> {
                            if (binhluan.isSuccess()) {
//                                Toast.makeText(this, "_" + binhluan.getResult(), Toast.LENGTH_SHORT).show();
                                listBinhLuan = binhluan.getResult();
                                binhLuanAdapter = new BinhLuanAdapter(this, listBinhLuan);
                                recyclerViewBinhLuan.setAdapter(binhLuanAdapter);
                            }
//                            else {
//                                Toast.makeText(this, "_" + binhluan.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
                        },
                        throwable -> {
                            Show.Notify(this,getString(R.string.error_server));
                        }
                ));
    }


    public void openCart(View view) {
        Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
        startActivity(giohang);
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
    }
}