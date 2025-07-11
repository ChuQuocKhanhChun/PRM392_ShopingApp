package com.manager.glassshoping.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.glassshoping.R;
import com.manager.glassshoping.adapter.DonHangAdapter;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;
import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhagn;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOders();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void getOders() {
        Log.d("CHECK_ID", "User ID: " + Utils.user_current.getId());

        compositeDisposable.add(apiBanHang.xemdonhang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( donHangModel -> {
                            Log.d("API_RESULT", new Gson().toJson(donHangModel));
                            DonHangAdapter donHangAdapter = new DonHangAdapter(donHangModel.getResult(),getApplicationContext());
                            Log.d("TAG", "getOders: "+donHangAdapter.getItemCount());
                            redonhagn.setAdapter(donHangAdapter);
                        },
                        throwable -> {

                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        }));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        redonhagn = findViewById(R.id.recycleview_donhang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        redonhagn.setLayoutManager(layoutManager);
        toolbar = findViewById(R.id.toolbardonhang);

    }
}