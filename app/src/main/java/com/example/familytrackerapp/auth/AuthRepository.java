package com.example.familytrackerapp.auth;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AuthRepository {
    public String TAG="auth";
    private Application application;
    private final FirebaseAuth firebaseAuth;
    private  FirebaseFirestore firebaseFirestore;
    private  MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> check;
    public AuthRepository(Application application)
    {
        this.application=application;
       this. firebaseAuth=FirebaseAuth.getInstance();
       this.firebaseFirestore=FirebaseFirestore.getInstance();
       this.firebaseUserMutableLiveData=new MutableLiveData<>();
       this.check=new MutableLiveData<Boolean>();
       this.check.postValue(false);


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void sign_up(String email, String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(application.getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                        firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                        insertUsr(email);
                        check.postValue(true);
                     }
                    else
                    {
                        check.postValue(false);
                        Toast.makeText(application.getApplicationContext(), "Login Failed"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    public MutableLiveData<Boolean> getCheck() {
        return check;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void sigin_in(String email, String password)
    {
       firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful())
               {
                   Toast.makeText(application.getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                    check.postValue(true);
                   firebaseUserMutableLiveData.postValue(firebaseAuth.getCurrentUser());
               }
               else
               {
                   check.postValue(false);
                   Toast.makeText(application.getApplicationContext(),"Login Failed"+task.getException(),Toast.LENGTH_SHORT).show();
               }
           }
       });
    }
    public void insertUsr(String email)
    {
        HashMap<String,String> map=new HashMap<>();
        map.put("email",email);
        firebaseFirestore.collection("Users").document(email).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(application.getApplicationContext(), "Successful",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(), "Failed"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();


            }
        });
    }
    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }
}
