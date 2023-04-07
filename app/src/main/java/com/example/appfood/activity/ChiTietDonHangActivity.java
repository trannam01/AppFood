package com.example.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.adapter.ChiTietDonHangAdapter;
import com.example.lib.InterfaceResponsitory.AppFoodMethods;
import com.example.lib.RetrofitClient;
import com.example.lib.common.NetworkConnection;
import com.example.lib.common.Show;
import com.example.lib.common.Url;
import com.example.lib.model.ChiTietDonHang;
import com.example.lib.model.DonHang;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChiTietDonHangActivity extends AppCompatActivity {
    Toolbar toolbar_Chitietdonhang;
    RecyclerView recycleView_ChiTietDonHang;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppFoodMethods appFoodMethods;

    List<ChiTietDonHang.Result> listChiTietDonHang;
    ChiTietDonHangAdapter chiTietDonHangAdapter;

    TextView thongbao_soluong;

    int madonhang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);

        getViewId();
        actionToolbar();
        khoitao();

        //check network
        if(NetworkConnection.isConnected(this)) {
            getChiTietDonHang();
//                actionLoading();
            Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    private void khoitao() {
        listChiTietDonHang = new ArrayList<>();
        appFoodMethods = RetrofitClient.getRetrofit(Url.AppFood_Url).create(AppFoodMethods.class);

        //set kiểu layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView_ChiTietDonHang.setLayoutManager(layoutManager);
        recycleView_ChiTietDonHang.setHasFixedSize(true);
    }

    private void getChiTietDonHang() {
        DonHang.Result donhangResult = (DonHang.Result) getIntent().getSerializableExtra("chitietdonhang");
        madonhang = donhangResult.getId();
        compositeDisposable.add(appFoodMethods.POST_ChiTietDonHang(madonhang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ctdh -> {
                            if (ctdh.isSuccess()) {
                                listChiTietDonHang = ctdh.getResult();
                                chiTietDonHangAdapter = new ChiTietDonHangAdapter(this, listChiTietDonHang);
                                recycleView_ChiTietDonHang.setAdapter(chiTietDonHangAdapter);
                                toolbar_Chitietdonhang.setTitle(String.valueOf(donhangResult.getId()));
                            }
                        },
                        throwable -> {
                            Show.Notify(this,getString(R.string.error_server));
                        }
                ));
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Chitietdonhang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Chitietdonhang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getViewId() {
        toolbar_Chitietdonhang = findViewById(R.id.toolbar_Chitietdonhang);
        recycleView_ChiTietDonHang = findViewById(R.id.recycleView_ChiTietDonHang);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }

    public void openCart(View view) {
        Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
        startActivity(giohang);
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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}