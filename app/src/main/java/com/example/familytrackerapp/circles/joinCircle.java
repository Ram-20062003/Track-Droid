package com.example.familytrackerapp.circles;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.familytrackerapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class joinCircle extends AppCompatActivity {
    Button b_join;
    EditText joinText;
    int t=1;
    private static final String TAG="join";
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        List<String> list=new ArrayList<>();
        List<String> reverselist=new ArrayList<>();


        Map<String,List<String>> map=new HashMap<String,List<String>>();
        Map<String,List<String>> reverseMap=new HashMap<String,List<String>>();
        Map<String,String> map2= new HashMap<>();
        b_join=findViewById(R.id.joinCirclebutton);
        joinText=findViewById(R.id.joinText);
        firebaseFirestore.collection("groups").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    list.addAll((Collection<? extends String>) documentSnapshot.get("Group"));
                    Log.d(TAG, "onSuccess: "+documentSnapshot.get("Group"));
                }
                else
                {
                }
            }
        });

        b_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t++;
                joinText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        t=1;
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                String join=joinText.getText().toString();
                list.add(join);
                map.put("Group",list);
                firebaseFirestore.collection("groups").document(firebaseAuth.getUid().toString()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Success: Added successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e);

                    }
                });
                Log.d(TAG, "join: "+join);

                firebaseFirestore.collection("CreateGroup").document(join).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists() && t ==2 )
                        {
                            reverselist.addAll((Collection<? extends String>) documentSnapshot.get("id"));
                            reverselist.add(firebaseAuth.getUid());
                            reverseMap.put("id",reverselist);
                            Log.d(TAG, "join: "+join);
                            firebaseFirestore.collection("CreateGroup").document(join).set(reverseMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Success: Added successfully");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+e);

                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
