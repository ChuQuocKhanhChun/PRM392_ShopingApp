

package com.manager.glassshoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.manager.glassshoping.R;
import com.manager.glassshoping.model.GioHang;
import com.manager.glassshoping.model.SanPham;
import com.manager.glassshoping.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

import okhttp3.internal.Util;

public class ChitietActivity extends AppCompatActivity {

    TextView tensp, giasp, mota;
    Button bththemvaogiohang;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPham sanPham;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitiet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        ActionToolBar();
        initData();
        initControl();

    }

    private void initControl() {
        bththemvaogiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();

            }
        });
    }

    private void themGioHang(){
        boolean flag = false;
        if(Utils.manggiohang.size()>0){
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i=0;i< Utils.manggiohang.size();i++){
                if(Utils.manggiohang.get(i).getIdsp()== sanPham.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong+ Utils.manggiohang.get(i).getSoluong());
                    Double gia = sanPham.getPrice()+ Utils.manggiohang.get(i).getGiasp();
                    Utils.manggiohang.get(i).setGiasp(gia);
                    flag=true;
                }
            }
            if(!flag){
                Double gia = sanPham.getPrice()* soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPham.getId());
                gioHang.setHinhsp(sanPham.getImage());
                gioHang.setTensp(sanPham.getName());
                Utils.manggiohang.add(gioHang);
            }
        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            Double gia = sanPham.getPrice()* soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPham.getId());
            gioHang.setHinhsp(sanPham.getImage());
            gioHang.setTensp(sanPham.getName());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem= 0;
        for(int i=0;i<Utils.manggiohang.size();i++){
            totalItem= totalItem+Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }
    private void initData() {
         sanPham= (SanPham) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPham.getName());
        mota.setText(sanPham.getDescription());
        Glide.with(getApplicationContext()).load(sanPham.getImage()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+decimalFormat.format(sanPham.getPrice())+"VNĐ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item
                ,so);
        spinner.setAdapter(adapterspin);

    }

    private void initView() {
        tensp=findViewById(R.id.txttenspct);
        giasp=findViewById(R.id.txtgiaspct);
        mota = findViewById(R.id.txtmotachitiet);
        bththemvaogiohang= findViewById(R.id.btnthemvaogiohang);
        spinner = findViewById(R.id.spiner);
        imghinhanh = findViewById(R.id.itemchitiet_img);
        toolbar= findViewById(R.id.toolbarchitiet);
        badge= findViewById(R.id.menu_sl);
        FrameLayout frameLayout= findViewById(R.id.framegiohang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });



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

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.manggiohang !=null){
            int totalItem= 0;
            for(int i=0;i<Utils.manggiohang.size();i++){
                totalItem= totalItem+Utils.manggiohang.get(i).getSoluong();
            }

            badge.setText(String.valueOf(totalItem));
        }
    }
}