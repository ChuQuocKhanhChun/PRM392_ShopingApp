package com.manager.glassshoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email,pass,repass,mobile,username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    FirebaseAuth firebaseAuth;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ki);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangki();
            }
        });
    }

    private void dangki() {
        String str_email = email.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập lại mật khẩu!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Số điện thoại!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_username)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên đăng nhập!", Toast.LENGTH_SHORT).show();
        }else {
            if(!str_pass.equals(str_repass)){
                    Toast.makeText(getApplicationContext(), "Mật khẩu chưa khớp!", Toast.LENGTH_SHORT).show();
            }else {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(str_email,str_pass)
                        .addOnCompleteListener(DangKiActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if(user!=null){
                                        postData(str_email,str_pass,str_username, str_mobile, user.getUid());
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        }

    }
    private void postData(String str_email, String str_pass,String str_username,String str_mobile,String uid ){
        compositeDisposable.add(apiBanHang.dangKi(str_email,str_pass,str_username,str_mobile,uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( userModel -> {
                            if(userModel.isSuccess()){
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setPassword(str_pass);

                                Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                                startActivity(dangnhap);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email= findViewById(R.id.editEmail);
        pass= findViewById(R.id.editPassword);
        repass=findViewById(R.id.editRePassword);
        button = findViewById(R.id.btndangki);
        mobile=findViewById(R.id.editMobile);
        username= findViewById(R.id.editUsername);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}