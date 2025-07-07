package com.example.glassshoping.retrofit;

import io.reactivex.rxjava3.core.Observable;

import com.example.glassshoping.model.SanPhamModel;
import com.example.glassshoping.model.SanPhamMoiModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiBanHang {

    @GET("getloaikinh.php")
    Observable<SanPhamModel> getSanpham();

    @GET("getkinhmoi.php")
    Observable<SanPhamMoiModel> getSPmoi();


}
