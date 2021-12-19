package com.example.familytrackerapp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.familytrackerapp.MainActivity;
import com.example.familytrackerapp.R;

public class sign_up extends AppCompatActivity {
    EditText t_user,t_pass,t_confo,t_phone;
    Button b_signup,b_signin;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        t_user=findViewById(R.id.user);
        t_pass=findViewById(R.id.pass);
        t_confo=findViewById(R.id.confo_pass);
        t_phone=findViewById(R.id.phone);
        b_signup=findViewById(R.id.signup);
        b_signin=findViewById(R.id.signin);

    }

    public void sign_in(View view) {
        Intent intent=new Intent(sign_up.this,sign_in.class);
        startActivity(intent);
    }

    public void sign_up(View view) {
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        String email=t_user.getText().toString();
        String pass=t_pass.getText().toString();
        authViewModel.sign_up(email,pass);
        authViewModel.getCheck_sts().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean==true)
                {
                    Intent intent=new Intent(getApplicationContext(), sign_in.class);
                    startActivity(intent);
                }
            }
        });
    }
}
