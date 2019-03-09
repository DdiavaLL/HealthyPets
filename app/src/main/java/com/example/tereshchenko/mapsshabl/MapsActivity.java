package com.example.tereshchenko.mapsshabl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker q;

    //Варианты для разрешения на использования геолокации
    private Boolean my_locationPermissonsGranted = false;

    //Для местоположения
    private FusedLocationProviderClient mFusedLocationProviderClient;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Карты готовы!", Toast.LENGTH_SHORT).show();

        //Отслеживание по терминалу::::::::
        Log.d("onMapReady", "onMapReady: MAP IS READY __________________");

        mMap = googleMap;
        //Marker q;
        // Add a marker in Sydney and move the camera
        //LatLng rostov = new LatLng(47.258016, 39.651040);
        //Создадим маркер!
        //mMap.addMarker(new MarkerOptions().position(rostov).title("Marker in Rostov!"));

        //Аля по нажатиюпоказывается текст маркера
        /*q = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Hello world")
                .snippet("Additional text"));*/

        //Приблизим камеру!
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rostov,15F));

        if (my_locationPermissonsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getLocalPermission();
    }

    //Отслеживаем местоположение усройства
    private void getDeviceLocation()
    {
        Log.d("getDeviceLocation","getDeviceLocation: местоположение отслеживается!");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try
        {
            if (my_locationPermissonsGranted)
            {
                Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null)
                        {
                            Log.d("addOnCompleteListener","addOnCompleteListener: локация найдена!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);
                        }
                        else
                        {
                            Log.d("addOnCompleteListener","addOnCompleteListener: локация не найдена!");
                            Toast.makeText(MapsActivity.this,"Невозможно определить ваше местоположение", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e)
        {
            Log.e("getDeviceLocation","getDeviceLocation: " + e.getMessage());
        }
    }

    //Пишем свою камеру??????
    private void moveCamera(LatLng latLng,float zoom)
    {
        Log.d("moveCamera","moveCamera: двигаем камеру на: lat: " + latLng.latitude + " ,lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    //Инициализация карты
    private void InitMap()
    {
        Log.d("InitMap","InitMap: MAP IS init ___");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

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

    //Проверка на наличие необходимых разрешений на использование геолокации
    private void getLocalPermission()
    {
        Log.d("getLocalPermission","getLocalPermission: разрешение получено! ___");

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                my_locationPermissonsGranted = true;
                InitMap();
            }
            else
            {
                ActivityCompat.requestPermissions(this,permissions,1234);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this,permissions,1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        my_locationPermissonsGranted = false;
        switch (requestCode)
        {
            case 1234:
            {
                if(grantResults.length > 0)
                {
                    for (int i = 0; i < grantResults.length; i++)
                    {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            my_locationPermissonsGranted=false;
                            Log.d("onRequestPerm","onRequestPermissionsResult: разрешение не получено!!!!! ___");
                            return;
                        }
                    }
                    my_locationPermissonsGranted = true;
                    Log.d("onRequestPerm","onRequestPermissionsResult: разрешение получено!!!!!!! ___");
                    //инициализируем карты
                    InitMap();
                }
            }
        }

    }

    /*public void onClickTest(View view) {
        q.showInfoWindow();
    }*/
}
