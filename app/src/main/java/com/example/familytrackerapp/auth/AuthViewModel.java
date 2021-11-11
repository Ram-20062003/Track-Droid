package com.example.familytrackerapp.auth;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> check_sts;
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository=new AuthRepository(application);
        firebaseUserMutableLiveData= authRepository.getFirebaseUserMutableLiveData();
        check_sts= authRepository.getCheck();
    }
    public void sign_up(String email,String password)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            authRepository.sign_up(email,password);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void sign_in(String email, String password)
    {
        authRepository.sigin_in(email, password);
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getCheck_sts() {
        return check_sts;
    }
}
