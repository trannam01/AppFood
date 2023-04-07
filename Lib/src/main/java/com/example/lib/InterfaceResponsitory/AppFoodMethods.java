package com.example.lib.InterfaceResponsitory;

import com.example.lib.model.BinhLuan;
import com.example.lib.model.ChiTietDonHang;
import com.example.lib.model.DanhMuc;
import com.example.lib.model.DonHang;
import com.example.lib.model.Mon;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AppFoodMethods {
    @GET("danhmuc.php")
    Observable<DanhMuc> GET_DanhMuc();

    @GET("donhang.php")
    Observable<DonHang> GET_DonHang();

    @GET("monngaunhien.php")
    Observable<Mon> GET_MonNgauNhien();

    @POST("chitietdanhmuc.php")
    @FormUrlEncoded
    Observable<Mon> POST_MonTheoDanhMuc(
//            @Field("page") int page,
//            @Field("select") int select,
            @Field("madanhmuc") int madanhmuc
    );

    @POST("chitietlichsudonhang.php")
    @FormUrlEncoded
    Observable<ChiTietDonHang> POST_ChiTietDonHang(
//            @Field("page") int page,
//            @Field("select") int select,
            @Field("madonhang") int madonhang
    );

    @POST("timkiemmon.php")
    @FormUrlEncoded
    Observable<Mon> POST_TimKiemMon(
            @Field("tenmon") String tenmon
    );

    @POST("binhluan.php")
    @FormUrlEncoded
    Observable<BinhLuan> POST_BinhLuan(
            @Field("mamon") String mamon
    );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHang> POST_LichSuDonHang(
            @Field("tenkhachhang") String tenkhachhang
    );

}
