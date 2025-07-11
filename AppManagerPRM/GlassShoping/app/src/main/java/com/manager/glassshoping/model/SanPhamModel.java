package com.manager.glassshoping.model;

import android.hardware.lights.LightsManager;

import java.util.List;

public class SanPhamModel {
    boolean success;
    String message;
    List<LoaiSp> result;

    public List<LoaiSp> getResult() {
        return result;
    }

    public void setResult(List<LoaiSp> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
