package kr.ac.gnu.bingbingcon;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import kr.ac.gnu.bingbingcon.Login.BingActivity;
import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends ActionBarActivity{
	static final String EXTRA_DATA = "bicon.extra";
	public static Context mainContext;
	public static int MainActivity = 1010;
	public static String pState = null;
	public static String LogID = null;
	public static final Double defaultLatitude = 35.154514,defaultLongitude = 128.098480;//�����б� �����
	public static final float defaultZoom = 15;
	private int fragmentNow=0;
	DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;
    GoogleMap mGoogleMap;
    
    ListView lvDrawerList;
    ArrayAdapter<String> adtDrawerList;
    private ArrayList<String> send = new ArrayList<String>();
    String[] menuItems = new String[]{"��������", "�˸�����","����ڼ���","�α׾ƿ�"};
    
    //bj
//    Main main;
    Fragment main;
    
    NoticeSet noticeset;
    UserSet userset;
    
    //ImageFragment fragImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = MainActivity.this;
//        send = (ArrayList<String>)getIntent().getSerializableExtra(EXTRA_DATA);
        Intent intent = getIntent();
        if(intent.hasExtra("notification")){//notifiaction Click ���� ȣ��Ǵ� ���
        	String getNotification = intent.getStringExtra("notification");
        	Log.i("MainActivity.java","@@�˸� ���� �׽�Ʈ:"+getNotification);
        }
        
        Intent joinIntent = new Intent(this,BingActivity.class);
        startActivityForResult(joinIntent, MainActivity);
        
        main=MainFragment.newInstance();
        
        //findap = FindAp.newInstance();
        noticeset=NoticeSet.newInstance();
        userset=UserSet.newInstance(main);
        
        //fragImage = new ImageFragment().newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, main).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.fl_activity_main, main).commit();

        // Navigation drawer : menu lists
        lvDrawerList = (ListView) findViewById(R.id.lv_activity_main);
        adtDrawerList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        lvDrawerList.setAdapter(adtDrawerList);
        lvDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
	                case 0:
	                	fragmentNow = 0;
	                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, main).commit();
	                    break;  
                    case 1:
                    	fragmentNow = 1;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, noticeset).commit();
                        break;
                    case 2:
                    	fragmentNow = 2;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, userset).commit();
                        break;
                    case 3:
                    	fragmentNow = 0;
                    	SharedPreferences pref = getSharedPreferences("loginfo", MODE_PRIVATE);
            	        SharedPreferences.Editor editor = pref.edit();
            	        editor.remove("user_id");
            	        editor.remove("user_pw");
            	        editor.commit();
            	        loging();
            	        break;
                }
                dlDrawer.closeDrawer(lvDrawerList);
            }
        });

        // Navigation drawer : ActionBar Toggle
        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4EA67B));
        
        checkParking();
        
    }
    
    
	@SuppressWarnings("deprecation")
	private void checkParking() {
		//������ �� Parking ���� Ȯ��----------------------
  		Vector<NameValuePair> parkState = new Vector<NameValuePair>();
  		parkState.add(new BasicNameValuePair("ispark", "3"));
  		parkState.add(new BasicNameValuePair("user_id", LogID));
  		HttpPostAsyncTask async_park = new HttpPostAsyncTask(parkState,	HttpPostAsyncTask.ParkingAddress);
  		try {
  			pState = async_park.execute().get();
  		} catch (InterruptedException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (ExecutionException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		Log.i("Main.java", "��������:" + pState);
  		//--------------------------------------------------END Parking Ȯ��
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	Log.i("MainActivty.java", "requestCode: "+requestCode);
    	if(requestCode == 67556){
        	Log.i("MainActivty.java", "if (requestCode) : "+requestCode);
    		//GPS ON,off�� ���ϵǴ� ��
    	}
    	else if(requestCode == 1010){
  		  checkParking();
    	}
    	else if(resultCode != BingActivity._ACTVTY_FINISH){
    		finish();
    	}
    }
    public void loging(){
        Intent joinIntent = new Intent(this,BingActivity.class);
        startActivityForResult(joinIntent, MainActivity);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
//    	getMenuInflater().inflate(R.menu.main, menu);
    	return true;
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
   public boolean onOptionsItemSelected(MenuItem item){
    	
    	if ((item != null) && (item.getItemId() == 16908332) && (dtToggle.isDrawerIndicatorEnabled())) {
    		if (this.dlDrawer.isDrawerVisible(Gravity.RIGHT))
    			this.dlDrawer.closeDrawer(Gravity.RIGHT);
    		else {
    			this.dlDrawer.openDrawer(Gravity.RIGHT);
    		}
    		return true;
    	}
//    	if (item != null && item.getItemId() == android.R.id.home) {
//            if (dlDrawer.isDrawerOpen(Gravity.RIGHT)) {
//            	dlDrawer.closeDrawer(Gravity.RIGHT);
//            } else {
//            	dlDrawer.openDrawer(Gravity.RIGHT);
//            }
//            return true;
//        }
    /*	System.out.println(dtToggle);
    	try {
    		 if(dtToggle.onOptionsItemSelected(item)){
    	            return true;
    	        }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
       
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated` method stub
    	
		if(fragmentNow != 0) {
			fragmentNow = 0;
			Log.i("MainActivity.java", "��Ű���κ���");
			checkParking();
			getSupportFragmentManager().beginTransaction().replace(R.id.fl_activity_main, main).commit();
			
		}else{
			super.onBackPressed();
		}
    	
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	BluetoothService.DestroyReceiver();
    	
    }
   
}
