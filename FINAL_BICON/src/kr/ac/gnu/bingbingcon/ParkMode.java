package kr.ac.gnu.bingbingcon;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

public class ParkMode  {
	private Vector<NameValuePair> parkValue;
	public String IsPask(){
		String result = null;
		try {
			makeVector(MainActivity.LogID,"3");
			result = new HttpPostAsyncTask(parkValue, HttpPostAsyncTask.PARKING_URL).execute().get().toString();
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "Error";
		} catch (ExecutionException e) {
			e.printStackTrace();
			return "Error";
		}
	}
	public String parking(){
		String result = "empty";
		try {
			makeVector(MainActivity.LogID,"1");
			result = new HttpPostAsyncTask(parkValue, HttpPostAsyncTask.PARKING_URL).execute().get().toString();
			Log.d("ParkMode","ispark "+result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}
	public void riding(){
		String result = "emptys";
		try {
			makeVector(MainActivity.LogID,"2");
			result = new HttpPostAsyncTask(parkValue, HttpPostAsyncTask.PARKING_URL).execute().get().toString();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	private void makeVector(String user_id, String ispark) {
		parkValue = new Vector<NameValuePair>();
		parkValue.add(new BasicNameValuePair("user_id", user_id));
		parkValue.add(new BasicNameValuePair("ispark", ispark));
	}

}
