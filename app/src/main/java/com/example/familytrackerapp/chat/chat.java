package com.example.familytrackerapp.chat;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familytrackerapp.R;
import com.example.familytrackerapp.chat.adapters.chatAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class chat extends AppCompatActivity {
    private static final String TAG = "chat";
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RecyclerView recyclerView;
    chatAdapter ca;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        List<String> list = new ArrayList<>();
        recyclerView=findViewById(R.id.chatView);
        firebaseFirestore.collection("groups").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    Log.d(TAG, "onSuccess: "+documentSnapshot.get("Group"));
                    list.addAll((Collection<? extends String>) documentSnapshot.get("Group"));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    ca=new chatAdapter(list);
                    recyclerView.setAdapter(ca);

                }
            }
        });


    }
}
