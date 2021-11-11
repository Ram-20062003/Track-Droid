package com.example.familytrackerapp.auth;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.familytrackerapp.MainActivity;
import com.example.familytrackerapp.R;

public class sign_in extends AppCompatActivity {
    EditText t_user ,t_pass;
    Button b_signin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
       t_user=findViewById(R.id.user_signin);
        t_pass=findViewById(R.id.pass_sigin);
        b_signin=findViewById(R.id.sign);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void log_in(View view) {
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        String email=t_user.getText().toString();
        String pass=t_pass.getText().toString();
        authViewModel.sign_in(email,pass);
        authViewModel.getCheck_sts().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean==true)
                {
                    Toast.makeText(getApplicationContext(),"true ",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });
     }
}
