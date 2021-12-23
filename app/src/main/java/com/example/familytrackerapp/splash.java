package com.example.familytrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.familytrackerapp.auth.sign_up;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(5500);

                }catch (Exception e){
                    e.printStackTrace();

                }finally {
                    Intent intent =new Intent(splash.this, sign_up.class);
                    startActivity(intent);
                    finish();

                }
            }
        };thread.start();

    }
}