package com.example.appfood.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.adapter.MonNgauNhienAdapter;
import com.example.lib.InterfaceResponsitory.AppFoodMethods;
import com.example.lib.RetrofitClient;
import com.example.lib.common.Show;
import com.example.lib.common.Url;
import com.example.lib.model.Mon;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TimKiemActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppFoodMethods appFoodMethods;
    EditText seach_mon;
    List<Mon.Result> listMonTimKiem;
    MonNgauNhienAdapter monNgauNhienAdapter;
    Toolbar toolbar_Timkiem;
    RecyclerView recycleView_TimKiem;
    String tenmon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);
        getViewId();
        actionToolbar();
        actionTimKiem();
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Timkiem);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Timkiem.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getViewId() {
        toolbar_Timkiem = findViewById(R.id.toolbar_TimKiem);
        recycleView_TimKiem = findViewById(R.id.recycleView_TimKiem);
        seach_mon = findViewById(R.id.seach_mon);
        listMonTimKiem = new ArrayList<>();
        appFoodMethods = RetrofitClient.getRetrofit(Url.AppFood_Url).create(AppFoodMethods.class);

        //set kiểu layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recycleView_TimKiem.setLayoutManager(layoutManager);
        recycleView_TimKiem.setHasFixedSize(true);
    }

    private void actionTimKiem(){
        seach_mon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Search();
                closeKeyboard();
                return false;
            }
        });
    }

    private void Search() {
        tenmon = seach_mon.getText().toString();
        compositeDisposable.add(appFoodMethods.POST_TimKiemMon(tenmon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mon -> {
                            if (mon.isSuccess()) {
                                listMonTimKiem = mon.getResult();
                                monNgauNhienAdapter = new MonNgauNhienAdapter(this, listMonTimKiem);
                                recycleView_TimKiem.setAdapter(monNgauNhienAdapter);
                            }
                            else {
                                Show.Notify(this,"Không có món ăn trùng tên!");
                            }
                        },
                        throwable -> {
                            Show.Notify(this,getString(R.string.error_server));
                        }
                ));
    }

    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            getApplicationContext().INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}