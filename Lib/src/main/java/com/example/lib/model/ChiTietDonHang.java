package com.example.lib.model;

import java.io.Serializable;
import java.util.List;

public class ChiTietDonHang implements Serializable {
    public class Result implements Serializable {
        int id;
        int madonhang;
        int mamon;
        String tenmon;
        String gia;
        String soluong;
        String hinhmon;

        public String getHinhmon() {return hinhmon;}

        public void setHinhmon(String hinhmon) {this.hinhmon = hinhmon;}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMadonhang() {
            return madonhang;
        }

        public void setMadonhang(int madonhang) {
            this.madonhang = madonhang;
        }

        public int getMamon() {
            return mamon;
        }

        public void setMamon(int mamon) {
            this.mamon = mamon;
        }

        public String getTenmon() {
            return tenmon;
        }

        public void setTenmon(String tenmon) {
            this.tenmon = tenmon;
        }

        public String getGia() {
            return gia;
        }

        public void setGia(String gia) {
            this.gia = gia;
        }

        public String getSoluong() {
            return soluong;
        }

        public void setSoluong(String soluong) {
            this.soluong = soluong;
        }
    }

    boolean success;
    String message;
    List<ChiTietDonHang.Result> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
}
