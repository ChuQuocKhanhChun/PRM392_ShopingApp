package com.manager.glassshoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.glassshoping.R;
import com.manager.glassshoping.adapter.SanPhamMoiAdapter;
import com.manager.glassshoping.model.EventBus.SuaXoaEvent;
import com.manager.glassshoping.model.SanPham;
import com.manager.glassshoping.model.SanPhamMoi;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import soup.neumorphism.NeumorphCardView;

public class QuanLiActivity extends AppCompatActivity {
    ImageView themsp;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<SanPhamMoi> sanPhams;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    SanPhamMoi sanPhamSuaXoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_li);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        toolbar = findViewById(R.id.toolbarquanli);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // đóng Activity hiện tại
            }
        });
        Log.d("DEBUG", "Toolbar: " + toolbar);


        initControl();
        getSPmoi();
    }
    private void initControl() {
//        themsp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent them = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
//                startActivity(them);
//            }
//        });
    }
    private void getSPmoi() {
        compositeDisposable.add(apiBanHang.getSPmoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sanPhamMoiModel -> {
                    if(sanPhamMoiModel.isSuccess()){
                        sanPhams = sanPhamMoiModel.getResult();
                        sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(), sanPhams);
                        recyclerView.setAdapter(sanPhamMoiAdapter);
                    }
                }, throwable -> {
                    Toast.makeText(getApplicationContext(),"Không kết nối được server"+ throwable.getMessage(),Toast.LENGTH_LONG).show();
                }));
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý sản phẩm");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Quay về màn hình trước
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycleview_ql_sp);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        toolbar = findViewById(R.id.toolbarquanli);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            // Xử lý khi nhấn vào nút thêm
            Intent intent = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish(); // hoặc onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suaSanPham();
        }else{
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable.add(apiBanHang.xoaSP(sanPhamSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( messageModel -> {
                    Toast.makeText(getApplicationContext(), messageModel.getMessage(),Toast.LENGTH_LONG).show();
                    getSPmoi();
                },throwable -> {
                    Toast.makeText(getApplicationContext(), throwable.getMessage(),Toast.LENGTH_LONG).show();
                }));
    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(),ThemSanPhamActivity.class);
        intent.putExtra("sua",sanPhamSuaXoa);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenSuaXoa(SuaXoaEvent event){
        if(event !=null){
            sanPhamSuaXoa = event.getSanPhamMoi();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}