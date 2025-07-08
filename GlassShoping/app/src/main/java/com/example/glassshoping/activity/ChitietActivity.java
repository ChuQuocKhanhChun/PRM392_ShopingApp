

package com.example.glassshoping.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.glassshoping.R;
import com.example.glassshoping.model.SanPham;
import com.example.glassshoping.utils.Utils;

import java.text.DecimalFormat;

public class ChitietActivity extends AppCompatActivity {

    TextView tensp, giasp, mota;
    Button bththemvaogiohang;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPham sanpham;
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
        if(Utils.manggiohang.size()>0){
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong()
        }
    }
    private void initData() {
        SanPham sanPham= (SanPham) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPham.getName());
        mota.setText(sanPham.getDescription());
        Glide.with(getApplicationContext()).load(sanPham.getImage()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+decimalFormat.format(sanPham.getPrice())+"VNĐ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin= new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
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
}