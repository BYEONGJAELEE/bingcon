package kr.ac.gnu.bingbingcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyLocationListener implements LocationListener {
	Activity mContext;
	GoogleMap mGoogleMap;
	//현재 안드로이드 GPS를 이용하여 내위치 확인 클래스
	public MyLocationListener(Activity context,GoogleMap googlemap){
		mContext = context;
		mGoogleMap = googlemap;
	}

	public void onLocationChanged(Location location) {

		Intent getI = mContext.getIntent();
		String title = getI.getStringExtra("title");
		Log.i("MylocationListener", "location" + location);
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		Log.i("MylocationListener", "test!!!" + latitude);
		LatLng position = new LatLng(latitude, longitude);
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,
				15));

		mGoogleMap.addMarker(
				new MarkerOptions().position(position).title(title))
				.showInfoWindow();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public void onProviderEnabled(String provider) {

	}

	public void onProviderDisabled(String provider) {

	}
}