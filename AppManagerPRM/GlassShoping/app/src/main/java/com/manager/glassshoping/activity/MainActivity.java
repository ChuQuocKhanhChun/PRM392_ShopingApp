package com.manager.glassshoping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.manager.glassshoping.R;
import com.manager.glassshoping.adapter.LoaiSpAdapter;
import com.manager.glassshoping.adapter.SanPhamMoiAdapter;
import com.manager.glassshoping.model.LoaiSp;
import com.manager.glassshoping.model.SanPham;
import com.manager.glassshoping.model.SanPhamModel;
import com.manager.glassshoping.model.SanPhamMoi;
import com.manager.glassshoping.model.SanPhamMoiModel;
import com.manager.glassshoping.model.User;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import org.jetbrains.annotations.Async;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationViewManHinhChinh;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangsp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSPmoi;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    NotificationBadge notificationBadge;
    FrameLayout frameLayout;
    ImageView search_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if(Paper.book().read("user")!=null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getToken();
        Anhxa();
        ActionBar();
        if(isConnected(this)){
            ActionViewFilpper();
            getSanpham();
            getSPmoi();
            getEventClick();
        }else{
            Toast.makeText(getApplicationContext(),"không có internet",Toast.LENGTH_LONG).show();
        }
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TOKEN", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM token
                    String token = task.getResult();
                    Log.d("TOKEN", "Token: " + token);
                    // Bạn có thể gửi token lên server nếu cần
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1001);
            }
        }

    }
    public void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getId(), s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(messageModel -> {

                                    }, throwable -> {
                                        Log.d("Log", throwable.getMessage());
                                    }));
                        }
                    }
                });
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent donhang = new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 1:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 2:
                        Intent kinhthoitrang = new Intent(getApplicationContext(), KinhThoiTrangActivity.class);
                        kinhthoitrang.putExtra("loai",3);
                        startActivity(kinhthoitrang);
                        break;
                    case 3:
                        Intent kinhram = new Intent(getApplicationContext(), KinhRamActivity.class);
                        kinhram.putExtra("loai",2);
                        startActivity(kinhram);
                        break;
                    case 4:
                        Intent kinhcan = new Intent(getApplicationContext(), KinhCanActivity.class);
                        kinhcan.putExtra("loai",1);
                        startActivity(kinhcan);
                        break;
                    case 5:
                        Intent quanli = new Intent(getApplicationContext(), QuanLiActivity.class);
                        startActivity(quanli);
                        finish();
                        break;
                    case 6:
                        //xoa key user

                        Paper.book().delete("user");
                        FirebaseAuth.getInstance().signOut();
                        Intent dangnhap = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangnhap);
                        finish();
                        break;


                }
            }
        });
    }

    private void getSPmoi() {
        compositeDisposable.add(apiBanHang.getSPmoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sanPhamMoiModel -> {
                        if(sanPhamMoiModel.isSuccess()){
                            mangSPmoi = sanPhamMoiModel.getResult();
                            sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSPmoi);
                            recyclerViewManHinhChinh.setAdapter(sanPhamMoiAdapter);
                        }else{
                            Log.e("API_ERROR", "Server trả về success = false");
                        }
                }, throwable -> {
                    Log.e("API_ERROR", "Lỗi khi lấy dữ liệu", throwable);
                    Toast.makeText(getApplicationContext(),"Không kết nối được server"+ throwable.getMessage(),Toast.LENGTH_LONG).show();
                }));
    }

    private void getSanpham() {
        compositeDisposable.add(
                apiBanHang.getSanpham()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                sanPhamModel -> {
                                    if(sanPhamModel.isSuccess()){
                                        mangsp= sanPhamModel.getResult();
                                        mangsp.add(new LoaiSp("Quản lí","https://e7.pngegg.com/pngimages/125/935/png-clipart-computer-icons-user-symbol-manager-miscellaneous-computer-network-thumbnail.png"));
                                        mangsp.add(new LoaiSp("Đăng xuất","https://static.vecteezy.com/system/resources/previews/006/606/705/non_2x/sign-out-logout-icon-in-circle-line-vector.jpg"));
                                        //khoi tao adapter
                                        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangsp);
                                        listViewManHinhChinh.setAdapter(loaiSpAdapter);
                                    }
                                }
                        )

        );

    }

    private void ActionViewFilpper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://file.hstatic.net/200000665395/file/bai-viet-quang-cao-kinh-mat-2_5afadd9204564db9b77cacaae55cf78f_grande.jpg");
        mangquangcao.add("https://chotdonhang.com/wp-content/uploads/2024/01/quang-cao-kinh-mat-4.jpg");
        mangquangcao.add("https://matkinhtamduc.com/wp-content/uploads/2024/06/s523c25.jpg");
        for(int i=0;i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);


    }

    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void Anhxa(){
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper= findViewById(R.id.viewfilppermanhinhchinh);
        recyclerViewManHinhChinh= findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        navigationViewManHinhChinh = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        notificationBadge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        search_img = findViewById(R.id.img_search);
        //khoi tao danh sach san pham
        mangsp = new ArrayList<>();
        mangSPmoi= new ArrayList<>();
        if(Utils.manggiohang==null){
            Utils.manggiohang= new ArrayList<>();
        }else {
            int totalItem= 0;
            for(int i=0;i<Utils.manggiohang.size();i++){
                totalItem= totalItem+Utils.manggiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(search);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem= 0;
        for(int i=0;i<Utils.manggiohang.size();i++){
            totalItem= totalItem+Utils.manggiohang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi!=null && wifi.isConnected())||(mobile!=null && mobile.isConnected())){
            return true;

        }else{
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}