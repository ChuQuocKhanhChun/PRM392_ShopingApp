package com.manager.glassshoping.activity;

import static android.content.ContentValues.TAG;

import android.app.ComponentCaller;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.manager.glassshoping.R;
import com.manager.glassshoping.databinding.ActivityThemSanPhamBinding;
import com.manager.glassshoping.model.MessageModel;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;

import java.io.File;
import java.nio.channels.ConnectionPendingException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import soup.neumorphism.LightSource;
import soup.neumorphism.NeumorphCardView;

public class ThemSanPhamActivity extends AppCompatActivity {
    Spinner spinner;
    int loai=0;
    ActivityThemSanPhamBinding binding;
    ApiBanHang apiBanHang;
    String mediaPath;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemSanPhamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        apiBanHang  = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initData();
    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Chọn loại kính");
        stringList.add("Kính cận");
        stringList.add("Kính râm");
        stringList.add("Kính thời trang");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnAddSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themSanPham();
            }
        });
        binding.imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ThemSanPhamActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        super.onActivityResult(requestCode, resultCode, data, caller);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d(TAG, "onActivityResult: " + mediaPath);
    }
    private  String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor == null){
            result = uri.getPath();

        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return  result;
    }


    // Uploading Image/Video
    private void uploadMultipleFiles() {
        Uri uri =Uri.parse(mediaPath);
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(getPath(uri));
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file.getName(), requestBody1);
        ApiBanHang getResponse = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Call<MessageModel> call = getResponse.uploadFile(fileToUpload1);
        call.enqueue(new Callback< MessageModel >() {


            @Override
            public void onResponse(Call<MessageModel> call, retrofit2.Response<MessageModel> response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call < MessageModel > call, Throwable t) {

            }
        });
    }
    private void themSanPham() {
        String name = binding.txttenspadd.getText().toString().trim();
        String str_price = binding.txtGiasanphamAdd.getText().toString().trim();
        String str_mota = binding.txtMotaAdd.getText().toString().trim();
        String str_img = binding.txtHinhanhAdd.getText().toString().trim();
        String str_solg = binding.txtSoluongAdd.getText().toString().trim();
        if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(str_price)||TextUtils.isEmpty(str_mota)||TextUtils.isEmpty(str_img)||TextUtils.isEmpty(str_solg)||loai==0){
            Toast.makeText(getApplicationContext(), "Bạn cần nhập đủ thông tin ", Toast.LENGTH_LONG).show();
        }else {
            compositeDisposable.add(apiBanHang.addSP(name,
                    loai,
                    Double.parseDouble(str_price),
                    str_mota,
                    Integer.parseInt(str_solg),
                    str_img).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( messageModel -> {
                            if(messageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    }, throwable -> {
                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                    }));
        }
    }

    private void initView() {
        spinner = findViewById(R.id.spiner_soluong_add);
    }


}