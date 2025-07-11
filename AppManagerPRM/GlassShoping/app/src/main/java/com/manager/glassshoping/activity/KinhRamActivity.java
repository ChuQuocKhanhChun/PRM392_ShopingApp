package com.manager.glassshoping.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.manager.glassshoping.adapter.KinhCanAdapter;
import com.manager.glassshoping.adapter.KinhRamAdapter;
import com.manager.glassshoping.model.SanPham;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class KinhRamActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page =1;
    int loai= 1;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isloading = false;
    KinhRamAdapter kinhCanAdapter;
    List<SanPham> sanPhamList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kinh_ram);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
            Anhxa();
            ActionToolBar();
            getData(page);
            addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isloading==false ){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition()== sanPhamList.size()-1){
                        isloading= true;
                        loadMore();
                    }
                }
            }
        });
    }
    private void loadMore(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                sanPhamList.add(null);
                kinhCanAdapter.notifyItemInserted(sanPhamList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remover null
                sanPhamList.remove(sanPhamList.size()-1);
                kinhCanAdapter.notifyItemRemoved(sanPhamList.size());
                page=page+1;
                getData(page);
                kinhCanAdapter.notifyDataSetChanged();
                isloading=false;

            }
        },2000);
    }
    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getKinh(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            if(sanPhamModel.isSuccess()){
                                if(kinhCanAdapter==null){
                                    sanPhamList = sanPhamModel.getResult();
                                    kinhCanAdapter = new KinhRamAdapter(getApplicationContext(), sanPhamList);
                                    recyclerView.setAdapter(kinhCanAdapter);
                                }else{
                                    int vitri = sanPhamList.size()-1;
                                    int soluongadd = sanPhamModel.getResult().size();
                                    for(int i=0;i<soluongadd;i++){
                                        sanPhamList.add(sanPhamModel.getResult().get(i));
                                    }
                                    kinhCanAdapter.notifyItemRangeInserted(vitri,soluongadd);
                                }


                            }
                        },throwable -> {
                            Log.e("API_ERROR", "Lỗi kết nối: " + throwable.getMessage());
                            Toast.makeText(getApplicationContext(),"Không kết nối được server"+ throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbar= findViewById(R.id.toolbarkinhram);
        recyclerView = findViewById(R.id.recycleview_kinhram);
        linearLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamList = new ArrayList<>();

    }
}