package com.manager.glassshoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.glassshoping.R;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.internal.Util;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tongtien, txtsdt, txtemail;
    EditText editdiachi;
    AppCompatButton btndathang;
    ApiBanHang apiBanHang;
    int totalItem;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        countItem();
        initControl();
        
    }

    private void countItem() {
        if(Utils.manggiohang==null){
            totalItem= 0;
        }else {
            totalItem= 0;
            for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
            }
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled( true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        Double tongtin = getIntent().getDoubleExtra("tongtien",0.0);
        tongtien.setText(decimalFormat.format(tongtin)+"VND");
        txtemail.setText(Utils.user_current.getEmail());
        txtsdt.setText(Utils.user_current.getMobile());
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = editdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ", Toast.LENGTH_LONG).show();
                }else{
                    //post data
                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOder(Utils.user_current.getEmail(),
                            Utils.user_current.getMobile(),
                            String.valueOf(tongtin),
                            Utils.user_current.getId(),
                            str_diachi,totalItem,new Gson().    toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(userModel -> {
                                Toast.makeText(getApplicationContext(), "Them don hang", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                                    int idmuahang = Utils.mangmuahang.get(i).getIdsp();
                                    for (int j = 0; j < Utils.manggiohang.size(); j++) {
                                        if (Utils.manggiohang.get(j).getIdsp() == idmuahang) {
                                            Utils.manggiohang.remove(j);
                                            j--; // cần lùi lại vì đã remove
                                            break;
                                        }
                                    }
                                }

                                // Xóa danh sách mua hàng sau khi đã chuyển sang thanh toán
                                Utils.mangmuahang.clear();

                                // Cập nhật lại giao diện (nếu cần)
                                startActivity(intent);
                            }, throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }));

                }
            }
        });

    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbarthanhtoan);
        tongtien = findViewById(R.id.txttongt);
        txtsdt = findViewById(R.id.txtsdtdh);
        txtemail= findViewById(R.id.txtemaildh);
        editdiachi = findViewById(R.id.txtdiachidathang);
        btndathang = findViewById(R.id.btndathang);
    }
}