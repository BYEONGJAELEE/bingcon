package kr.ac.gnu.bingbingcon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;


public class BeaconDetactorService extends Service implements IBeaconConsumer {

    private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();

    LocationManager locationManager;
    double longitude, latitude;
    LocationService locationService;
    String Moduleid;
    String major, minor;
    String result;
    private Vector<NameValuePair> nameValue;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        iBeaconManager.bind(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationService=new LocationService(locationManager);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                stopSelf();
            }
        };
        handler.postDelayed(runnable, 10000);
    }

    @Override
    public void onDestroy() {
        iBeaconManager.unBind(this);
        super.onDestroy();
    }


    @Override
    public void onIBeaconServiceConnect() {
        /**
         * Specifies a class that should be called each time the IBeaconService gets ranging data,
         *  which is nominally once per second when iBeacons are detected.
         **/
        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
                locationService.registerLocationUpdates();
                latitude=locationService.getLatitude();
                longitude=locationService.getLongitude();
                if (iBeacons.size() > 0) {
                    try {
                        major = String.valueOf(((ArrayList<IBeacon>) iBeacons).get(0).getMajor());
                        minor = String.valueOf(((ArrayList<IBeacon>) iBeacons).get(0).getMinor());
                        Moduleid = major + minor;
                        if(latitude!=0&&longitude!=0) {
                            Log.d("search", Double.toString(latitude) + "    " + Double.toString(longitude));
                            makeVector(Double.toString(latitude), Double.toString(longitude), Moduleid);
                            result = new HttpPostAsyncTask(nameValue, HttpPostAsyncTask.IN_GPS_CHECK_STEAL_URL).execute().get().toString();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                arrayL.addAll((ArrayList<IBeacon>) iBeacons);
                //Notifies the attached observers that the underlying data has been changed and
                //any View reflecting the data set should refresh itself.
            }
        });

        /**
         * Tells the IBeaconService to start looking for iBeacons that match the passed Region object,
         * and providing updates on the estimated distance very seconds while iBeacons in the Region are visible.
         * Note that the Region's unique identifier must be retained to later call the stopRangingBeaconsInRegion method.
         * */
        try {
            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        /**
         * Tells the IBeaconService to start looking for iBeacons that match the passed Region object,
         * and providing updates on the estimated distance very seconds while iBeacons in the Region are visible.
         * Note that the Region's unique identifier must be retained to later call the stopRangingBeaconsInRegion method.
         * */
        try {
            iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void makeVector(String latitude, String longitude, String module_id) {
        nameValue = new Vector<NameValuePair>();
        nameValue.add(new BasicNameValuePair("latitude", latitude));
        nameValue.add(new BasicNameValuePair("longitude", longitude));
        nameValue.add(new BasicNameValuePair("module_id", module_id));
        SharedPreferences pre = getSharedPreferences("loginfo", MODE_PRIVATE);
        String user_id = pre.getString("user_id", "");
        nameValue.add(new BasicNameValuePair("userid", user_id));
    }

}