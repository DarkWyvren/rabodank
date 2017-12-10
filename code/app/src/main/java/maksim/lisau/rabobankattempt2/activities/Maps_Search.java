package maksim.lisau.rabobankattempt2.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import maksim.lisau.rabobankattempt2.R;
import maksim.lisau.rabobankattempt2.database.Branch;
import maksim.lisau.rabobankattempt2.database.DatabaseHandler;

public class Maps_Search extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int PLACE_PICKER_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps__search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        Branch[] branches=new Branch[DatabaseHandler.branchHashMap.size()];
        DatabaseHandler.branchHashMap.values().toArray(branches);
        for (int i=0; i< DatabaseHandler.branchHashMap.size(); i++) {
            Geocoder geocoder=new Geocoder(this);
            try {
                System.out.println(branches[i].address.getFormattedAddressFull());
                if (geocoder.getFromLocationName(branches[i].address.getFormattedAddressFull(),1)!=null&&
                        geocoder.getFromLocationName(branches[i].address.getFormattedAddressFull(),1).size()>0) {
                    Address rabo = geocoder.getFromLocationName(branches[i].address.getFormattedAddressFull(), 1).get(0);
                    System.out.println("found Address~!");
                    LatLng raboLocation = new LatLng(rabo.getLatitude(), rabo.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(raboLocation).title(branches[i].name));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Intent intent = new Intent(Maps_Search.this, ViewBranch.class);
                            Bundle b = new Bundle();
                            b.putString("Branch key", marker.getTitle());
                            intent.putExtras(b);
                            startActivity(intent);
                            return false;
                        }
                    });
                    branches[i].address.country=rabo.getCountryName();
                } else {
                    System.out.println("No found address.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
