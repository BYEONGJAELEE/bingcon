package kr.ac.gnu.bingbingcon.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

public class HttpPostAsyncTask extends AsyncTask<Void, Void, String> {
	private Vector<NameValuePair> nameValue;
	private int nameValueLength;
	private URL url;
	private String receveSTR="null";
	public static String apInfoAddress ="http://14.63.226.196/apinfo.php";
	public static String userInfoAddress ="http://14.63.226.196/userinfo.php";
	public static String bikeMapAddress ="http://14.63.226.196/bikemap.php";
	public static String bikeInfoAddress ="http://14.63.226.196/bikeinfo.php";
	public static String LoginProcessAddress ="http://14.63.226.196/LoginProcess.php";
	public static String Login = "http://14.63.226.196/login.php";
	public static String ParkingAddress = "http://14.63.226.196/parking.php";
	public static String userUpdateAddress = "http://14.63.226.196/updateuserinfo.php";
	public static String RegisterIdAddress ="http://14.63.226.196/register.php";
	public static String GCMPushTestAddress ="http://14.63.226.196/bjbj.php";
	
	static public String IN_GPS_CHECK_STEAL_URL="http://14.63.226.196/IN_GPS_CHECK_STEAL_URL.php";
	static public String OUT_GPS_URL="http://14.63.226.196/bicon/OutBikeGPS2.php";
	static public String PARKING_URL="http://14.63.226.196/bicon/parking.php";
	
	// Bicon
//	static public String LOGIN_URL="http://14.63.226.196/bicon/join.php";
	
//	static public String USERINFO_URL="http://14.63.226.196/bicon/userinfo2.php";
//	static public String UPDATAINFO_URL="http://14.63.226.196/bicon/updateuserinfo.php";
//	static public String SHOWINFO_URL="http://14.63.226.196/bicon/showuserinfo.php";
//	
//	static public String IN_BIKEINFO="http://14.63.226.196/bicon/BikeInfo.php";
//	static public String OUT_BIKEINFO="http://14.63.226.196/bicon/OutBikeInfo.php";
	
	public HttpPostAsyncTask(Vector<NameValuePair> namevalue, String tmp_url ) {
		try {
			url = new URL(tmp_url);
			nameValue = namevalue;
			if(namevalue != null) {
				nameValueLength = namevalue.size();
				Log.d("HttpPostAsyncTask","������ nameValueLength:"+namevalue.size());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected String doInBackground(Void... params) {
		 try { 
             //-------------------------- 
             //   URL �����ϰ� �����ϱ� 
             //-------------------------- 
			 Log.i("now ", "now url "+ url) ;
             HttpURLConnection http = (HttpURLConnection) url.openConnection();   // ���� 
             //-------------------------- 
             //   ���� ��� ���� - �⺻���� �����̴� 
             //-------------------------- 
             http.setDefaultUseCaches(false);                                            
             http.setDoInput(true);                         // �������� �б� ��� ���� 
             http.setDoOutput(true);                       	// ������ ���� ��� ����  
             http.setRequestMethod("POST");         		// ���� ����� POST 

             // �������� ������ <Form>���� ���� �Ѿ�� �Ͱ� ���� ������� ó���϶�� �� �˷��ش� 
             http.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
             //-------------------------- 9
             //   ������ �� ���� 
             //-------------------------- 
             StringBuffer buffer = new StringBuffer();
			if (nameValue != null) {
				// nameVlue ����
				// (id,pw,name,phone,bikePhoto,area,school,major,schoolNum)
				Log.d("HttpPostAsync", "nameValue:" + nameValue);
				for (int i = 0; i < nameValueLength - 1; i++) {
					buffer.append(nameValue.get(i)).append("&"); // php ������ �� ����
				}
				buffer.append(nameValue.get(nameValueLength - 1));
			}
            
             OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(),HTTP.UTF_8);
             PrintWriter writer = new PrintWriter (outStream);
             writer.write(buffer.toString()); 
             writer.flush(); 
             Log.d("HttpPostAsync", "writer:"+writer);
             //-------------------------- 
             //   �������� ���۹ޱ� 
             //-------------------------- 
//             Log.i("receive !!", "!! " +receveSTR + " , value size : "+ nameValue.size());
             int status = http.getResponseCode();
             
             InputStream in;
			if(status >= HttpStatus.SC_BAD_REQUEST){
                 in = http.getErrorStream();
                 Log.e("Bad ", "Bad request " + in.toString() + "    "+ in);
			}
             else {
                 in = http.getInputStream();
//             
             
             InputStreamReader tmp = new InputStreamReader(in, "UTF-8");  
             BufferedReader reader = new BufferedReader(tmp); 
             String str;
             while ((str = reader.readLine()) != null) {       // �������� ���δ����� ������ ���̹Ƿ� ���δ����� �д´� 
                  if(str.toString().indexOf("<")!=1 || !str.toString().isEmpty()){
                 	 //�±װ� �ƴϰ�, ���� �ƴ϶��
                	  receveSTR = str;
                  }
             }
             
             Log.i("receveSTR ", receveSTR) ;
             }
             if (http != null) {
            	 //  http.setDefaultUseCaches(true);                                            
            	 http.disconnect();
 		    //	http = null;
             }
//             if (tmp !=null)
//            	 tmp.close();
             if (writer != null)
            	 writer.close();
             if (outStream != null)
            	 outStream.close();
        } catch (MalformedURLException e) { 
               // 
        	e.printStackTrace();
        } catch (IOException e) { 
               //  
        	e.printStackTrace();
        } // try 
		return receveSTR;
	}

}
//String[] arr = receiveSTR.split(",")