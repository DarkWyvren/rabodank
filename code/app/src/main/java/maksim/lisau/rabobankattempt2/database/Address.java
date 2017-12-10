package maksim.lisau.rabobankattempt2.database;

import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

/**
 * Created by Maks on 07-Oct-17.
 */

public class Address {
    public String country;
    public String state;
    public String streetHouseNumber;

    public Address(String country, String state, String street) {
        this.country = country;
        this.state = state;
        this.streetHouseNumber = street;
    }
    public Address(String s) {
        Geocoder geocoder=new Geocoder(DatabaseHandler.context);
        try {
            if (geocoder.getFromLocationName(s,1)!=null&&
                    geocoder.getFromLocationName(s,1).size()>0) {
                android.location.Address rabo = geocoder.getFromLocationName(s, 1).get(0);
                System.out.println("found Address~!");
                LatLng raboLocation = new LatLng(rabo.getLatitude(), rabo.getLongitude());
                country=rabo.getCountryName();
                state=rabo.getAdminArea();
                streetHouseNumber=rabo.getLocality();
            } else {
                System.out.println("No found address.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Used for internal storage purposes.
    public String getStorageAddress() {
        return streetHouseNumber+"."+"."+state+"."+country;
    }
    //Long display
    public String getFormattedAddressFull() {
        String s=streetHouseNumber+", "+state+", "+country;
        return s;
    }
    //Short display
    public String getFormattedAddressShort() {
        String s=streetHouseNumber;
        return s;
    }
}
