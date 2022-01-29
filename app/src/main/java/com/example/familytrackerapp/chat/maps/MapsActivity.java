package com.example.familytrackerapp.chat.maps;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.familytrackerapp.Constants.constant;
import com.example.familytrackerapp.Coordinates;
import com.example.familytrackerapp.MainActivity;
import com.example.familytrackerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private static final String key="COORDINATES";
    private Coordinates Coordinates;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent=getIntent();
        name=intent.getStringExtra("locations");
        Log.d(TAG, "mapsLocations: "+intent.getStringExtra("locations"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

            mapFragment.getMapAsync(MapsActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MainActivity.firebaseFirestore.collection("CreateGroup").document(name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    List<String> list=new ArrayList();
                    list.addAll((Collection<? extends String>) documentSnapshot.get("id"));
                    for(int i=0;i<list.size();i++)
                    {
                        Log.d(TAG, "id: "+list.get(i));
                        getdata(list.get(i));

                    }
                }
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void getdata(String uid)
    {
        MainActivity.firebaseFirestore.collection("Locations").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Latitude from firestore "+documentSnapshot.getString(key));
                    Toast.makeText(getApplicationContext(),"Latitude:"+ documentSnapshot.getString(constant.lat), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Longitude:" +documentSnapshot.getString(constant.longi),Toast.LENGTH_SHORT).show();
                    LatLng marker = new LatLng(Double.parseDouble(documentSnapshot.getString(constant.lat)),Double.parseDouble(documentSnapshot.getString(constant.longi)));
                    mMap.addMarker(new MarkerOptions().position(marker).title(uid));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,20));
                }
                else
                {
                    Log.d(TAG, "Error!! does not exist ");
                }
             }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e);
            }
        });
    }
}