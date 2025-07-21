package com.manager.glassshoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.manager.glassshoping.R;
import com.manager.glassshoping.retrofit.ApiBanHang;
import com.manager.glassshoping.retrofit.RetrofitClient;
import com.manager.glassshoping.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki, txtquenmk;
    EditText email, pass;
    AppCompatButton btndangnhap;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
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
                    if(firebaseUser!=null){
                        dangNhap(str_email,str_pass);

                    }else {
                        //user đã signout
                        firebaseAuth.signInWithEmailAndPassword(str_email,str_pass).addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    dangNhap(str_email,str_pass);

                                }
                            }
                        });
                    }
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
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
                        dangNhap(Paper.book().read("email"),Paper.book().read("pass"));
                        }
                    },1000);
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
                                //lưu lại thogn tin ngươi dung
                                Paper.book().write("user",userModel.getResult().get(0) );
                                Intent dn = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(dn);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Sai email hoặc mật khẩu ", Toast.LENGTH_SHORT).show();
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