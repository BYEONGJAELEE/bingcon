package kr.ac.gnu.bingbingcon;


import static googlecloudservice.CommonUtilities.SENDER_ID;
import static googlecloudservice.CommonUtilities.displayMessage;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import googlecloudservice.ServerUtilities;
import kr.ac.gnu.bingbingcon.R;
import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService  extends GCMBaseIntentService {
	private static final String TAG = "GCMIntentService";
	public static String GCMRedId;
    public GCMIntentService() {
        super(SENDER_ID);
    }
    HttpPostAsyncTask async;
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId + " R---Regi " +  JoinActivity.userId);
        displayMessage(context, "Your device registred with GCM");
        ServerUtilities.register(context, JoinActivity.userId , registrationId);
        GCMRedId = registrationId;

    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }

    /**
   //  * Method  on Receiving a new message
    // * */ // 푸시로 받은 메시지 
    @Override 
    protected void onMessage(Context context, Intent intent) {
    	try {
			
	
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("bingcon");
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, "자전거 도난중");	
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        }
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
    	try {
			
		
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        }
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
    	try {
			
		
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
    	}
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
    	try {
			
		
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        } catch (Exception e) {
        			// TODO: handle exception
        	e.printStackTrace();
        }
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
    	try {
		
        int icon = R.drawable.icon;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.putExtra("notification", "push");
        MainActivity.pState = "4";
        
        
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        
        if(NoticeSet.soundFlag == true) {        
        	// Play default notification sound
        	notification.defaults |= Notification.DEFAULT_SOUND;
        
        	// 사용자가 확인할 때까지 소리 출력.
        	notification.flags |= Notification.FLAG_INSISTENT;
        }
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        if(NoticeSet.vibrateFlag == true) {
        	// Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        
        notificationManager.notify(0, notification);      
    	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

}

