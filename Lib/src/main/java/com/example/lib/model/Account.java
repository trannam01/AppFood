package com.example.lib.model;

import java.io.Serializable;
import java.util.List;

public class Account implements Serializable {
    public class Result implements Serializable {
        int id;
        String tenkh;
        String sdt;
        String email;
        String matkhau;

        public int getId() {return id;}

        public void setId(int id) {this.id = id;}

        public String getTenkh() {return tenkh;}

        public void setTenkh(String tenkh) {this.tenkh = tenkh;}

        public String getSdt() {return sdt;}

        public void setSdt(String sdt) {this.sdt = sdt;}

        public String getEmail() {return email;}

        public void setEmail(String email) {this.email = email;}

        public String getMatkhau() {return matkhau;}

        public void setMatkhau(String matkhau) {this.matkhau = matkhau;}
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
