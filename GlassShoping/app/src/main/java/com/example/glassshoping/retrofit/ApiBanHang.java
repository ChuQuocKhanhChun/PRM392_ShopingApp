package com.example.glassshoping.retrofit;

import io.reactivex.rxjava3.core.Observable;

import com.example.glassshoping.model.SanPham2Model;
import com.example.glassshoping.model.SanPhamModel;
import com.example.glassshoping.model.SanPhamMoiModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiBanHang {

    @GET("getloaikinh.php")
    Observable<SanPhamModel> getSanpham();

    @GET("getkinhmoi.php")
    Observable<SanPhamMoiModel> getSPmoi();

    @POST("getsanpham.php")
    @FormUrlEncoded
    Observable<SanPham2Model> getKinh(
        @Field("page") int page,
        @Field("loai") int loai
    );


}
