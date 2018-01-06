package kr.ac.gnu.bingbingcon;

import static googlecloudservice.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static googlecloudservice.CommonUtilities.EXTRA_MESSAGE;
import static googlecloudservice.CommonUtilities.SENDER_ID;
import googlecloudservice.AlertDialogManager;
import googlecloudservice.ConnectionDetector;
import googlecloudservice.ServerUtilities;
import googlecloudservice.WakeLocker;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

public class JoinActivity extends Activity implements OnClickListener, IBeaconConsumer{

		protected static final int PICK_FROM_GALLERY = 2;
		Button okBtn, cancelBtn,pushRegisterBtn,moduleBtn;
		EditText joinEdit[] = new EditText[9];//가입정보 EditText (ID,PW,name,phone,bikePhoto,area,school,major,schoolNum)
		ImageView joinPoto;
		ProgressDialog popProgress;
		HttpPostAsyncTask mClientRunnable;
		TextView moduleText;
		private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
		private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
		String major, minor;
		String Moduleid;
		//	GCM
		public static String userId ;
		// Alert dialog manager
		AlertDialogManager alert = new AlertDialogManager();
		// Connection detector
		ConnectionDetector cd;
		// Asyntask
		//--------------
		
	    private static final String TEMP_PHOTO_FILE = "temp.jpg";       // 임시 저장파일

	    /** SD카드가 마운트 되어 있는지 확인 */
	    private boolean isSDCARDMOUNTED() {
	        String status = Environment.getExternalStorageState();
	        if (status.equals(Environment.MEDIA_MOUNTED))
	            return true;
	  
	        return false;
	    }
	    /** 임시 저장 파일의 경로를 반환 */
	    private Uri getTempUri() {
	        return Uri.fromFile(getTempFile());
	    }
	    /** 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환  */
	    private File getTempFile() {
	        if (isSDCARDMOUNTED()) {
	            File f = new File(Environment.getExternalStorageDirectory(), // 외장메모리 경로
	                    TEMP_PHOTO_FILE);
	            try {
	                f.createNewFile();      // 외장메모리에 temp.jpg 파일 생성
	            } catch (IOException e) {
	            }
	  
	            return f;
	        } else
	            return null;
	    }
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.user_join);
			//imgview = (ImageView) findViewById(R.id.imageView1);
			Button buttonGallery = (Button) findViewById(R.id.btn_select_gallery);
			
			buttonGallery.setOnClickListener(new View.OnClickListener() {		
				@Override
				public void onClick(View v) {
					try {
						  Intent intent = new Intent(
			                        Intent.ACTION_PICK,      // 또는 ACTION_PICKACTION_GET_CONTENT
			                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			                intent.setType("image/*");              // 모든 이미지
			               intent.putExtra("crop", "true");        // Crop기능 활성화
			                intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());     // 임시파일 생성
			                intent.putExtra("outputFormat",         // 포맷방식//act=android.intent.action.GET_CONTENT
			                        Bitmap.CompressFormat.JPEG.toString());
			              
			                startActivityForResult(intent, PICK_FROM_GALLERY);
//					Intent intent = new Intent(Intent.ACTION_PICK);
//					intent.setType(Media.CONTENT_TYPE);
//					intent.setData(Media.EXTERNAL_CONTENT_URI);
//					intent.setAction(Intent.ACTION_GET_CONTENT);
					
//					intent.putExtra("crop", "true");
//					intent.putExtra("aspectX",0);
//					intent.putExtra("aspectY",0);
//					intent.putExtra("outputX",200);
//					intent.putExtra("outputY",150);
//					
//					try{
//						intent.putExtra("return-data", true);
//						startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
//						
//					}catch(ActivityNotFoundException e){
//					}
					
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});
			
			okBtn = (Button) findViewById(R.id.joinOkBtn);
			cancelBtn = (Button) findViewById(R.id.joinCancelBtn);
			joinEdit[0] = (EditText)findViewById(R.id.idEdit);
			joinEdit[1] = (EditText)findViewById(R.id.nameEdit);
			joinEdit[2] = (EditText)findViewById(R.id.passwordEdit);
			joinEdit[3] = (EditText)findViewById(R.id.emailEdit);
			//joinEdit[4] = (TextView)findViewById(R.id.modulIDEdit);
			moduleText = (TextView)findViewById(R.id.moduleID);
//			joinEdit[4] = (EditText)findViewById(R.id.photoEdit);
			joinPoto = (ImageView)findViewById(R.id.photoEdit);
			okBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			moduleBtn = (Button)findViewById(R.id.module);
			pushRegisterBtn = (Button)findViewById(R.id.pushRegisterBtn);
			pushRegisterBtn.setOnClickListener(this);
			
			moduleBtn.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Bind();
	            }
	        });
		}
		
//		public String getPath(Uri uri) {
//		    String[] projection = {MediaStore.Images.Media.DATA};
//		    Cursor cursor = managedQuery(uri, projection, null, null, null);
//		    startManagingCursor(cursor);
//		    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		    cursor.moveToFirst();
//		    return cursor.getString(columnIndex);
//		}
		protected void onActivityResult(int requestCode, int resultCode, Intent data){
			Log.i("onActivityResult", "data  " + data.getDataString());
			if(requestCode == PICK_FROM_GALLERY){
				
			//	byte[] byteArray = data.getByteArrayExtra("Image");
				String filePath = Environment.getExternalStorageDirectory()
				            + "/temp.jpg";
				
				    System.out.println("path" + filePath); // logCat으로 경로확인.
				    
				    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
				    // temp.jpg파일을 Bitmap으로 디코딩한다.
				    if ( selectedImage != null ){
				    	Bitmap resized = Bitmap.createScaledBitmap(selectedImage, 500, 500, true);
				    	joinPoto.setImageBitmap(resized);
				    }
			}
		}
		
		public boolean inputUserData() throws InterruptedException, ExecutionException{
//			popProgress = new ProgressDialog(UserJoin.this);
//			popProgress.setProgress(RESULT_OK);
//			popProgress.show();
			
			String userData[] = new String[5];
			
			for(int i = 0 ; i < 4 ; i++) {
				try {
					if( joinEdit[i].length() > 0){
						//userData[4] = URLEncoder.encode(moduleText.getText().toString(),"UTF-8");
						userData[i] = URLEncoder.encode(joinEdit[i].getText().toString(),"UTF-8");
		            }
					else{
						new Toast(this).makeText(this, joinEdit[i].getHint().toString()+"을 입력해주세요", Toast.LENGTH_SHORT).show();
						return false;
					}
		         } catch (UnsupportedEncodingException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		         }
			}
			
			try {
				if( joinEdit[4] == null) {
					new Toast(this).makeText(this, "Moduleid를 입력해주세요", Toast.LENGTH_SHORT).show();
					return false;
				}else {
					userData[4] = URLEncoder.encode(moduleText.getText().toString(),"UTF-8");
	            }
//				else{
//					new Toast(this).makeText(this, joinEdit[4].getHint().toString()+"을 입력해주세요", Toast.LENGTH_SHORT).show();
//					return false;
//				}
	         } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	         }
			
			
			Log.i("inputUserData()", "사용자 입력데이터:"+userData);
		      Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
//		      param = new ArrayList();
		      
		      nameValue.add(new BasicNameValuePair("user_id", userData[0]));
		      nameValue.add(new BasicNameValuePair("user_name", userData[1]));
		      nameValue.add(new BasicNameValuePair("user_pw", userData[2]));
		      nameValue.add(new BasicNameValuePair("user_email",userData[3]));
		      nameValue.add(new BasicNameValuePair("module_id",userData[4]));
		      nameValue.add(new BasicNameValuePair("user_bike_image","null"));
//		      nameValue.add(new BasicNameValuePair("ispark",userData[6]));
		      Log.i("addTest", "userData_ID:"+userData[0]);
		      Log.i("addTest", "nameValue:"+nameValue);
		      HttpPostAsyncTask async = new HttpPostAsyncTask(nameValue,HttpPostAsyncTask.userInfoAddress);
		      String tt = async.execute().get();
		      Log.i("UserJoin Test", tt);
		      if(tt.indexOf("success")>=0) {
		    	Toast toast = new Toast(this);
				toast.makeText(this, "등록성공!", Toast.LENGTH_SHORT).show();
				finish();
		      }else if(tt.indexOf("duplication")>=0){
			    	Toast toast = new Toast(this);
					toast.makeText(this, "아이디중복!", Toast.LENGTH_SHORT).show();
					return false;
			  }
		      else{
		    	Toast toast = new Toast(this);
				toast.makeText(this, "등록실패!", Toast.LENGTH_SHORT).show();
				return false;
		      }
		      return true;
//			mClientRunnable = new ServerConect();
//			mClientRunnable.updateMessage(userData, joinHandler);
//			new Thread(mClientRunnable).start();
		}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if (v.getId() == pushRegisterBtn.getId()) {
//			//Handler handle = new Handler();
//			try {
//				Context conthis = getBaseContext();
//				// cd = new ConnectionDetector(getApplicationContext());
//				GCMRegistrar.checkDevice(this);
//				GCMRegistrar.checkManifest(this);
//				final String MobileDeviceID = GCMRegistrar
//						.getRegistrationId(this);
//				if ("".equals(MobileDeviceID)) {
//					GCMRegistrar.register(this, SENDER_ID);
//					Log.i("GCM", "GCM "+ "su");
//					
//				} else {
//					GCMRegistrar.unregister(this);
//					GCMRegistrar.register(this, SENDER_ID);
//					Log.i("GCM", "GCM "+ "sudd");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
		String regId ;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.joinOkBtn:
			try {
				if(!checkConnection(cd)){//인터넷 체크
					return;
				}
				userId = joinEdit[0].getText().toString();
				Log.i("ok ", "ok btn ");
				if(inputUserData()){//회원 정보 등록
					try {//GCM 등록
						// Make sure the device has the proper dependencies.
						GCMRegistrar.checkDevice(this);
						// Make sure the manifest was properly set - comment out this line
						// while developing the app, then uncomment it when it's ready.
						GCMRegistrar.checkManifest(this);
						
						registerReceiver(mHandleMessageReceiver, new IntentFilter( //BroadcastReceiver
								DISPLAY_MESSAGE_ACTION));
						GCMRegistrar.register(this, SENDER_ID);
						// Get GCM registration id
						final String regId = GCMRegistrar.getRegistrationId(this);
	
						// Check if regid already presents
						if (regId.equals("")) {
							Log.i("id ", "id null"+ regId);
							// Registration is not present, register now with GCM			
							GCMRegistrar.register(this, SENDER_ID);
						} else {
							// Device is already registered on GCM
							if (GCMRegistrar.isRegisteredOnServer(this)) {
								// Skips registration.				
								Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
							} else {
								Log.i("JoinActivity.java", "userID :"+userId+" / regId: "+ regId);
								final Context context = this;
								ServerUtilities.register(context, userId,regId);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}//GCM 등록 END
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.joinCancelBtn:
			finish();
			break;
			
		
		}
	
	}
	private boolean checkConnection(ConnectionDetector cd) {
		cd = new ConnectionDetector(getApplicationContext());
		// Check if Internet present
		if (!cd.isConnectingToInternet()) { //인터넷 되는지 체크 
			// Internet Connection is not present
			alert.showAlertDialog(this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return false;
		}
		return true;
	}
	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
			
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext()); // 얘는 자는애 깨우는 역할
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
			//lblMessage.append(newMessage + "\n");			
//			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
	
	
	public void onIBeaconServiceConnect() {

        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
                arrayL.addAll((ArrayList<IBeacon>) iBeacons);
                if (iBeacons.size() > 0) {

                    major = String.valueOf(((ArrayList<IBeacon>) iBeacons).get(0).getMajor());
                    minor = String.valueOf(((ArrayList<IBeacon>) iBeacons).get(0).getMinor());
                    Moduleid = major + minor;
                    moduleText.setText(Moduleid);
                    unBind();
                }
            }
        });

        try {
            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
	
	private void Bind() {
        iBeaconManager.bind(this);
    }
	private void unBind() {
        iBeaconManager.unBind(this);
    }

}
