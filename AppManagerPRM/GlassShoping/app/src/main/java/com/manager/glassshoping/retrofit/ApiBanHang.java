package com.manager.glassshoping.retrofit;

import io.reactivex.rxjava3.core.Observable;

import com.manager.glassshoping.model.DonHangModel;
import com.manager.glassshoping.model.MessageModel;
import com.manager.glassshoping.model.SanPham2Model;
import com.manager.glassshoping.model.SanPhamModel;
import com.manager.glassshoping.model.SanPhamMoiModel;
import com.manager.glassshoping.model.UserModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangKi(
            @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("password") String password

    );
    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> reset(
            @Field("email") String email

    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int iduser,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet


    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemdonhang(
            @Field("iduser") int iduser

    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPham2Model> timkiem(
            @Field("keyword") String keyword

    );
    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> addSP(
            @Field("name") String name,
            @Field("category_id") int category_id,
            @Field("price") Double price,
            @Field("description") String description,
            @Field("stock") int stock,
            @Field("image") String image);
    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);


    @POST("xoaSP.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSP(
            @Field("id") int id

    );
    @POST("update.php")
    @FormUrlEncoded
    Observable<MessageModel> updateSP(
            @Field("id") int id,
            @Field("name") String name,
            @Field("category_id") int category_id,
            @Field("price") Double price,
            @Field("description") String description,
            @Field("stock") int stock,
            @Field("image") String image);
    @POST("updatetk.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token);

    @POST("update_status_order.php")
    @FormUrlEncoded
    Observable<MessageModel> updatestatusOrder(
            @Field("idorder") int idorder,
            @Field("status") int status);

}
