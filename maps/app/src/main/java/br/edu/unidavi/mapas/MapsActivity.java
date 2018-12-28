package br.edu.unidavi.mapas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean enableMyLocation = false;

    private LatLng previousPosition = new LatLng(-27.2104016,-49.6466032);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button buttonNormal = findViewById(R.id.button_map_normal);
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        Button buttonSatelite = findViewById(R.id.button_map_satellite);
        buttonSatelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        Button buttonTerrain = findViewById(R.id.button_map_terrain);
        buttonTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        Button markerChecker = findViewById(R.id.button_marker_checker);
        markerChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!markers.isEmpty()) {
                    Marker marker = markers.get(0);
                    if (mMap.getProjection().getVisibleRegion().latLngBounds.contains(marker.getPosition())) {
                        Toast.makeText(MapsActivity.this, "Marker está na Tela!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MapsActivity.this, "Marker não está na Tela!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            enableMyLocation = true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"Por favor, sua localização é necessária", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.setMyLocationEnabled(true);
                } else {
                    enableMyLocation = true;
                }

                drawCircleUserPosition();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void drawCircleUserPosition() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mMap.addCircle(new CircleOptions()
                        .radius(25)
                        .center(new LatLng(location.getLatitude(), location.getLongitude()))
                        .strokeColor(Color.CYAN)
                        .strokeWidth(10)
                );
            }
        });
    }

    private List<Marker> markers = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(enableMyLocation);
        if (enableMyLocation) {
            drawCircleUserPosition();
        }

        addDragListener();
        addToolTipListener();

        // Marker na Unidavi
        LatLng unidavi = new LatLng(-27.2104016,-49.6466032);
        MarkerOptions marker = new MarkerOptions()
                .draggable(true)
                .position(unidavi)
                .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
                .title("Remover");
        markers.add(mMap.addMarker(marker));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(unidavi, 17f));

        mMap.addMarker(new MarkerOptions()
            .draggable(true)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home))
            .position(unidavi));

        mMap.getUiSettings().setMyLocationButtonEnabled(enableMyLocation);
    }

    private void addToolTipListener() {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                markers.remove(marker);
                marker.remove();
                Toast.makeText(MapsActivity.this, "Marker Removido!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDragListener() {
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (markers.contains(marker)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    mMap.addPolyline(new PolylineOptions()
                      .add(previousPosition, marker.getPosition())
                      .width(10)
                      .color(Color.RED)
                    );
                    previousPosition = marker.getPosition();
                }
            }
        });
    }
}