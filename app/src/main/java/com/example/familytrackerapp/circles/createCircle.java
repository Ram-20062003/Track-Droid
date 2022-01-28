package com.example.familytrackerapp.circles;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.familytrackerapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createCircle extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    Button b_create;
    EditText createText;
    private static final String TAG = "create";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        b_create=findViewById(R.id.createCirclebutton);
        createText=findViewById(R.id.createText);
        List<String> list=new ArrayList<>();
        Map<String,List<String>> map= new HashMap<>();
        b_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String create=createText.getText().toString();
                list.add(firebaseAuth.getUid());
                map.put("id",list);
                firebaseFirestore.collection("CreateGroup").document(create).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Created successfully");
                    }
                });

            }
        });
    }
}
