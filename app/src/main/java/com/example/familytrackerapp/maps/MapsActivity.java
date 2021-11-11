package com.example.familytrackerapp.maps;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private static final String key="COORDINATES";
    private Coordinates Coordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

            mapFragment.getMapAsync(MapsActivity.this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getdata();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void getdata()
    {
        MainActivity.firebaseFirestore.collection("Locations").document("RamGanesh").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d(TAG, "Latitude from firestore "+documentSnapshot.getString(key));
                    Toast.makeText(getApplicationContext(),"Latitude:"+ documentSnapshot.getString(constant.lat), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Longitude:" +documentSnapshot.getString(constant.longi),Toast.LENGTH_SHORT).show();
                    LatLng marker = new LatLng(Double.parseDouble(documentSnapshot.getString(constant.lat)),Double.parseDouble(documentSnapshot.getString(constant.longi)));
                    mMap.addMarker(new MarkerOptions().position(marker).title("Your Location"));
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

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.firebaseFirestore.collection("Locations").document("RamGanesh").addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Toast.makeText(getApplicationContext(), "Error ocuured", Toast.LENGTH_SHORT).show();
                }
                if(value.exists()){
                     mMap.clear();
                     Toast.makeText(getApplication().getApplicationContext(), value.getString(constant.lat), Toast.LENGTH_SHORT).show();
                    LatLng marker = new LatLng(Double.parseDouble(value.getString(constant.lat)),Double.parseDouble(value.getString(constant.longi)));
                    mMap.addMarker(new MarkerOptions().position(marker).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                }
            }
        });
    }
}