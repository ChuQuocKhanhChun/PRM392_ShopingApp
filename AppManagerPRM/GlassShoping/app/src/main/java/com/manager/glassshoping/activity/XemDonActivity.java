package com.manager.glassshoping.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.glassshoping.R;
import com.manager.glassshoping.adapter.DonHangAdapter;
import com.manager.glassshoping.model.DonHang;
import com.manager.glassshoping.model.EventBus.DonHangEvent;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhagn;
    Toolbar toolbar;
    DonHang donHang;
    int tinhtrang;
    AlertDialog dialog;
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventDonhang(DonHangEvent event){
        if(event!= null){
            donHang = event.getDonHang();
            showCustomDialog();
        }
    }

    private void showCustomDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donhang,null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton button = view.findViewById(R.id.btn_dialog);
        List<String> trangthai = new ArrayList<>();
        trangthai.add("Đơn hàng đang được xử lí");
        trangthai.add("Đơn hàng đã chấp nhận");
        trangthai.add("Đơn hàng đã giao cho đơn vị vẫn chuyển");
        trangthai.add("Thành công");
        trangthai.add("Đã huỷ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, io.paperdb.R.layout.support_simple_spinner_dropdown_item,trangthai);
        spinner.setAdapter(adapter);
        spinner.setSelection(donHang.getTrangthai());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tinhtrang=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capnhatDonHang();
                dialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog= builder.create();
        dialog.show();
    }

    private void capnhatDonHang() {
        compositeDisposable.add(apiBanHang.updatestatusOrder(donHang.getId(),tinhtrang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageModel -> {
                    getOders();
                }, throwable -> {
                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}