package com.example.lib.model;

import java.io.Serializable;
import java.util.List;

public class DonHang implements Serializable {
    public class Result implements Serializable {
        int id;
        String tenkhachhang;
        String diachi;
        String sodienthoai;
        double tongtien;
        String ghichu;

        public int getId() {return id;}

        public void setId(int id) {this.id = id;}

        public String getTenkhachhang() {return tenkhachhang;}

        public void setTenkhachhang(String tenkhachhang) {this.tenkhachhang = tenkhachhang;}

        public String getDiachi() {return diachi;}

        public void setDiachi(String diachi) {this.diachi = diachi;}

        public String getSodienthoai() {return sodienthoai;}

        public void setSodienthoai(String sodienthoai) {this.sodienthoai = sodienthoai;}

        public double getTongtien() {return tongtien;}

        public void setTongtien(double tongtien) {this.tongtien = tongtien;}

        public String getGhichu() {return ghichu;}

        public void setGhichu(String ghichu) {this.ghichu = ghichu;}
    }

    boolean success;
    String message;
    List<Result> result;

    public boolean isSuccess() {return success;}

    public void setSuccess(boolean success) {this.success = success;}

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public List<Result> getResult() {return result;}

    public void setResult(List<Result> result) {this.result = result;}
}
