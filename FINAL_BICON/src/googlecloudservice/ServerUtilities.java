package googlecloudservice;

import static googlecloudservice.CommonUtilities.SERVER_URL;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import kr.ac.gnu.bingbingcon.JoinActivity;
import kr.ac.gnu.bingbingcon.R;
import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class ServerUtilities {
	private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
static String TAG = "ServerUtils";

//    /**
//     * Register this account/device pair within the server.
//     *
//     */
    public static void register(final Context context,final String userId ,final String regId) {
    	
        Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
//	      param = new ArrayList();
      nameValue.add(new BasicNameValuePair("userId", userId));
      nameValue.add(new BasicNameValuePair("regId", regId));
//	      nameValue.add(new BasicNameValuePair("ispark",userData[6]));
	      Log.i("re addTest", "re userData_ID:"+JoinActivity.userId);
	      Log.i("re addTest", "re nameValue:"+nameValue);
	      HttpPostAsyncTask async = new HttpPostAsyncTask(nameValue,HttpPostAsyncTask.RegisterIdAddress);
	      String tt = null;
		try {
			if ( AsyncTask.Status.PENDING == async.getStatus())
				tt = async.execute().get();
		      Log.i("register return  Test", tt + "");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
        Log.i(TAG, "unregistering device (regId = " + regId + ")");
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            CommonUtilities.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            CommonUtilities.displayMessage(context, message);
        }
    }

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {   	
        
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        //regId=APA91bEYxO2gxPCWY1JA348EyUKU32Luu0F-C-qitWV58LtinppZueNfUr_TF1ZjhFc0KnlzYXd1-1I-T_oaXtDSfqnQACnmJxLFL5Ih3pdap71rVkkATHH-F9wI9_Dd1NN6sSD7ay7MpoZYoLqKgB3v8bGreM8yHg&userId=asdf
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
        	Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
          //  conn.setDefaultUseCaches(false);                                            
            conn.setDoOutput(true);//is used for POST and PUT requests. Specifies whether this URLConnection allows sending data.
            conn.setUseCaches(false);//Specifies whether the using of caches is enabled or the data has to be recent for every request.
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        }
         finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
}

