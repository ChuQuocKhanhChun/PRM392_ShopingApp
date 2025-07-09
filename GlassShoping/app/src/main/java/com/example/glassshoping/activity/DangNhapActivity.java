package com.example.glassshoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glassshoping.R;
import com.example.glassshoping.retrofit.ApiBanHang;
import com.example.glassshoping.retrofit.RetrofitClient;
import com.example.glassshoping.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki, txtquenmk;
    EditText email, pass;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }

    private void initControl() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dangki = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(dangki);
            }
        });
        txtquenmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dangki = new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(dangki);
            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                }else{
                    //save
                    Paper.book().write("email", str_email);
                    Paper.book().write("pass", str_pass);

                }
            }
        });

    }

    private void initView() {
        Paper.init(this);
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki=findViewById(R.id.txtdk);
        email=findViewById(R.id.dnEmail);
        pass=findViewById(R.id.dnPassword);
        btndangnhap=findViewById(R.id.btndangnhap);
        txtquenmk=findViewById(R.id.txtquenmk);
        //read data
        if(Paper.book().read("email")!=null&& Paper.book().read("pass")!=null){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
            if(Paper.book().read("isLogin")!=null){
                boolean flag = Paper.book().read("isLogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        dangNhap();    
                        }
                    });
                }
            }
        }
    }

    private void dangNhap(String str_email, String str_pass ) {

        compositeDisposable.add(apiBanHang.dangNhap(str_email,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( userModel -> {
                            if(userModel.isSuccess()){
                                Paper.book().write("isLogin", isLogin);


                                Utils.user_current= userModel.getResult().get(0);
                                Intent dn = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(dn);
                                finish();
                            }else{

                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail()!=null&&Utils.user_current.getPassword()!=null){
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPassword());

        }
    }
}