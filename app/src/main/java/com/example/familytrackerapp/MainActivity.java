package com.example.familytrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.familytrackerapp.chat.chat;
import com.example.familytrackerapp.chat.maps.MapsActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    public static  FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    Button joinCircle,createCircle,bChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinCircle=findViewById(R.id.joinCircle);
        createCircle=findViewById(R.id.createCircle);
        bChat=findViewById(R.id.chatButton);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        joinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, com.example.familytrackerapp.circles.joinCircle.class);
                startActivity(intent);
            }
        });

        createCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, com.example.familytrackerapp.circles.createCircle.class);
                startActivity(intent);
            }
        });

        bChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, chat.class);
                startActivity(intent);
            }
        });
      }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //getLastLocation();
            Intent intent=new Intent(MainActivity.this,MyService.class);
            startService(intent);
        } else {
            requestlocationpermissons();
        }
    }
    private void requestlocationpermissons() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1001&& grantResults.length>0&&grantResults!=null&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
           // getLastLocation();
            Intent intent=new Intent(MainActivity.this,MyService.class);
            startService(intent);
        }
    }

}