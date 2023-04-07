package com.example.appfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.adapter.DonHangAdapter;
import com.example.lib.InterfaceResponsitory.AppFoodMethods;
import com.example.lib.RetrofitClient;
import com.example.lib.common.NetworkConnection;
import com.example.lib.common.Show;
import com.example.lib.common.Url;
import com.example.lib.model.DonHang;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LichSuDonHang extends AppCompatActivity {

    Toolbar toolbar_Donhang;
    RecyclerView recycleView_Donhang;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppFoodMethods appFoodMethods;

    List<DonHang.Result> listDonHangResult;
    DonHangAdapter donHangAdapter;

    TextView thongbao_soluong;
    private static final String FILE_NAME = "myFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_don_hang);

        getViewId();
        actionToolbar();
        khoitao();

        if(NetworkConnection.isConnected(this)) {
//            ShowToast.Notify(this,"Internet thành công!");
            GetDonHang();
            Show.thayDoiSoLuongGioHangNho(thongbao_soluong);
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Donhang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Donhang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(trangchu);
                finish();
            }
        });
    }

    private void khoitao() {
        listDonHangResult = new ArrayList<>();
        appFoodMethods = RetrofitClient.getRetrofit(Url.AppFood_Url).create(AppFoodMethods.class);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recycleView_Donhang.setLayoutManager(layoutManager);
        recycleView_Donhang.setHasFixedSize(true);
    }

    private void GetDonHang() {
        compositeDisposable.add(appFoodMethods.GET_DonHang()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHang -> {
                            if(donHang.isSuccess()) {
                                listDonHangResult = donHang.getResult();
                                donHangAdapter = new DonHangAdapter(this,listDonHangResult);
                                recycleView_Donhang.setAdapter(donHangAdapter);
                            }
                        },
                        throwable -> {
                            Show.Notify(this,getString(R.string.error_server));
                        }
                ));
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }

    public void openCart(View view) {
        Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
        startActivity(giohang);
    }

    private void getViewId() {
        toolbar_Donhang = findViewById(R.id.toolbar_DonHang);
        recycleView_Donhang = findViewById(R.id.recycleView_DonHang);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);
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