package kr.ac.gnu.bingbingcon;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Taehoon on 2015-09-29.
 */
public class LocationService {
    LocationManager locationManager;
    double longitude, latitude;

    public LocationService(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void registerLocationUpdates() {
    	if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
    	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener);

    	if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
    	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
    	
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(android.location.Location location) {
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                // Gps 
                longitude = location.getLongitude();  
                latitude = location.getLatitude(); 
                Log.d("register12","longitude : "+longitude + " latitude : " + latitude);
            } else {
                longitude = location.getLongitude(); 
                latitude = location.getLatitude(); 
                Log.d("register13","longitude : "+longitude + " latitude : " + latitude);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
}
