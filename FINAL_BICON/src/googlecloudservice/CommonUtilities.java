package googlecloudservice;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
//    static final String SERVER_URL = "http://117.16.158.205:8080/gcm_server_php/register.php"; 
	 static final String SERVER_URL = "http://14.63.226.196/register.php";
    // Google project id
    // 얘는 머냐면 구글에 Api키받는데 가면 서버 api 키도 받기때문에 서버 id다 프로젝트 번호: 562858460721 //https://console.developers.google.com/project/562858460721
    //public static final String SENDER_ID = "600010809503"; 
    public static final String SENDER_ID = "124335009809";
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "AndroidHive GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
   public static void displayMessage(Context context, String message) {
    	try {
		
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
}