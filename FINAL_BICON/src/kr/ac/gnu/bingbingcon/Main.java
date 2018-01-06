/*
 * We use MainActivity. So We don't use this Class.
 * 2015.10.24 - From BJ 
 */


//package kr.ac.gnu.bingbingcon;
//
//
//import java.util.ArrayList;
//import java.util.StringTokenizer;
//import java.util.Vector;
//import java.util.concurrent.ExecutionException;
//
//import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;
//import net.daum.mf.map.api.MapView;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Point;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.method.ScrollingMovementMethod;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
//import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.swteam.bicon.bicon.BeaconServiceUtility;
//import com.swteam.bicon.bicon.BluetoothService;
//import com.swteam.bicon.bicon.LocationService;
//import com.swteam.bicon.bicon.ParkMode;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link TextFragment#newInstance} factory method to
// * create an instance of this fragment.
// *
// */
//public class Main extends Fragment implements OnMapClickListener{
//	GoogleMap mGoogleMap;
//	TextView text01;
//	Button bikeBtn,apBtn,reloadBtn;
//    Switch modeSwitch;
//	OnClickListener mClick;
//	private String recevie="";
//	private ArrayList<LatLng> arrayPoints;
//	private int leng;
//	
//	private Activity pActivity;
//	
//	//bj start 1
//	static final String EXTRA_DATA = "bicon.extra";
//
//    private static final int H_STATE_OFF = 1;
//    private static final int H_STATE_ON = 2;
//
//    // PHP�� ����� ���� ������
//    private String result;
//    private Vector<NameValuePair> nameValue;
//    private String user_id;
//    // ���� ���� ����� ���� ������
//    private final String LOCAL_API_KEY = "bbf1737e59b902dc7243352b825ccf05";
//    private String address;
//    private String latitude = null;
//    private String longitude = null;
//    private static MapView mapView;
//    private ArrayList<String> locData = new ArrayList<String>();
//    private ArrayList<String> bigReceive = new ArrayList<String>();
//    private ArrayList<String> receive = new ArrayList<String>();
//    private ArrayList<String> send = new ArrayList<String>();
//    private String lostLoc = null;
//
//    // activity ����
//    private Button map;
//    private Button refresh;
//    private Button search;
//    private TextView location;
//    private BluetoothService btService;
//    private Button parkButton;
//
//    private ParkMode parkMode;
//    //��ġ ����
//    LocationManager locationManager;
//    double curLong, curLati;
//    LocationService locationService;
//    //bj end1
//	
//    public static Main newInstance() {
//    	Main fragment = new Main();
//        return fragment;
//    }
//    public Main() {
//    	
//        // Required empty public constructor
//    }
//    @Override
//    public void onAttach(Activity activity) {
//    	// TODO Auto-generated method stub
//    	super.onAttach(activity);
//    	pActivity = activity;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        MapsInitializer.initialize(pActivity);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.map_layout, container, false);
//        mapView = new MapView(getActivity());
//        mapView.setDaumMapApiKey("API_KEY");
//        send = (ArrayList<String>) getActivity().getIntent().getSerializableExtra(EXTRA_DATA);
//        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
//        mapViewContainer.addView(mapView);
//        initMarker();
//
//        refresh = (Button)v.findViewById(R.id.map_refresh);
//        mapViewContainer.bringChildToFront(refresh);
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationService = new LocationService(locationManager);
//        locationService.registerLocationUpdates();
//
//        //���������� ����� �������� Ȯ��
//        btService = new BluetoothService(getActivity(), mHandler);
//        btService.checkBluetooth();
//
//        map = (Button) v.findViewById(R.id.main_smap);
//        //parkButton = (Button) v.findViewById(R.id.main_mode);���θ��
//        search = (Button) v.findViewById(R.id.main_ssearch);
//
//        // �ּ�ã��
//        //searchAddress(latitude, longitude);
////        changePark();
//
///*        parkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (parkButton.getText().toString().trim().equals("������")) {
//                    parkMode.riding();
//                    changePark();
//                } else if (parkButton.getText().toString().trim().equals("������")) {
//                    String getLocation;
//                    getLocation = parkMode.parking();
////                    parseLocation(getLocation);
//                    changePark();
//                }
//
//            }
//        });*/
//        map.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {//�� ã��
//
//                try {
//                    final Uri.Builder uri = new Uri.Builder();
//                    final Intent intent = new Intent();
//
//                    if (!lostLoc.equals(null)) {
//                        curLati = locationService.getLatitude();
//                        curLong = locationService.getLongitude();
//                        if(curLati>0&&curLong>0) {
//                            String startLoc = Double.toString(curLati) + "," + Double.toString(curLong);
//                            Log.d("startLoc", startLoc);
//                            uri.scheme("daummaps").authority("route").appendQueryParameter("sp", startLoc)
//                                    .appendQueryParameter("ep", lostLoc).appendQueryParameter("by", "CAR");
//
//                            // Scheme�� Ȱ���� ���� ������ ����
//                            intent.setAction("android.intent.action.VIEW");
//                            intent.setData(Uri.parse(uri.toString()));
//                            startActivity(intent);
//                        }
//                    }
//                } catch (Exception e) {// ���� �������� ������������� Scheme�� ���� �����÷��� ��������
//                    // �ٿ�ε�â���� �̵�
//                    Intent intent = new Intent();
//                    intent.setAction("android.intent.action.VIEW");
//                    intent.setData(Uri.parse("market://details?id=net.daum.android.map"));
//                    startActivity(intent);
//                }
//
//            }
//        });
//        search.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //btService.enableBluetooth();
//                if(btService.getDeviceState()){
//                    btService.enableBluetooth();
//                }
//                chkGpsService();
//                if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)&&!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    BeaconServiceUtility serviceUtility = new BeaconServiceUtility(getActivity());
//                    serviceUtility.onStart();
//                }
//
//            }
//        });
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initMarker();
//            }
//        });
//        return v;
//        
//        
//    }
//    
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//    	// TODO Auto-generated method stub
//    	super.onActivityCreated(savedInstanceState);
//    	text01 = (TextView) getActivity().findViewById(R.id.text01);
//		text01.setMaxLines(100);
//		text01.setVerticalScrollBarEnabled(true);
//		text01.setMovementMethod(new ScrollingMovementMethod());
//		modeSwitch = (Switch)getActivity().findViewById(R.id.modSwitch);
//		if(MainActivity.pState.indexOf("1") >= 0  ||  MainActivity.pState.indexOf("4") >= 0){//1�� ��ŷ����, 4�� ���� ����
//			//��ŷ����
//			modeSwitch.setChecked(true);
//		}else
//			modeSwitch.setChecked(false);
//		//��� ��ȯ ����ġ
//		modeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				Vector<NameValuePair> parkValue = new Vector<NameValuePair>();
//				parkValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
//				String temp=null;
//				double park_latitude,park_longitude;
//				if(isChecked){
//					//ON ���ý� ������ ���
//					parkValue.add(new BasicNameValuePair("ispark", "1"));
//					Log.i("Main.java", "��������:"+parkValue);
//					HttpPostAsyncTask async_park = new HttpPostAsyncTask(parkValue,HttpPostAsyncTask.ParkingAddress);
//					try {
//						temp = async_park.execute().get();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					//���� ������ ��ġ Ȯ�� �� ���� ���� �˸�â ����
//					Log.i("Main.java", "recevie: "+ temp);
//					if(temp.indexOf("park")>=0){
//						StringTokenizer tokens = new StringTokenizer(temp);
//						if(temp.indexOf("empty")>=0){
//							toastOut("���� ������ ��ġ�� ã�� �� �����ϴ�.");
//							modeSwitch.setChecked(false);
//						}else{
//							String receve[] = temp.split("/");
//							String state = receve[0];
//							park_latitude = Double.parseDouble(receve[1]);
//							park_longitude = Double.parseDouble(receve[2]);
//							Log.i("Main.java", "���� ó��-> state:"+state+" latitude:"+park_latitude+" longitude:"+park_longitude);
//							if(state.equals("parkmode")){
//								//parking -> parking ���� ��ȭ ����
//							}else if(state.equals("park")){
//								//ridding -> parking ���� ��ȭ
//								makeAlert("��ġ Ȯ��","���� ��ġ:"+park_latitude+"/"+park_longitude);
//							}else{
//								Log.e("Main.java","���� ����");
//							}
//						}
//						Toast.makeText(pActivity, "��ŷ���", Toast.LENGTH_SHORT).show();
//					}
//					else{
//						Log.e("Main.java", "parking Erro! : "+ temp);
//						modeSwitch.setChecked(false);
//					}
//				}
//				else{
//					parkValue.add(new BasicNameValuePair("ispark", "2"));
//					HttpPostAsyncTask async_park = new HttpPostAsyncTask(parkValue,HttpPostAsyncTask.ParkingAddress);
//					Log.i("Main.java", "��������:"+parkValue);
//					try {
//						temp = async_park.execute().get();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					Log.i("Main.java", "��������:"+temp);
//					Toast.makeText(pActivity, "��ŷ����", Toast.LENGTH_SHORT).show();
//					mGoogleMap.clear();
//				}
//			}
//		});
//		Button myGpsBtn = (Button) getActivity().findViewById(R.id.myGPSBtn);
//		myGpsBtn.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Log.i("Main.java", "����ġ Ȯ�� ��ư Ŭ��");
//				getMyLocation();//����ġ Ȯ�ο� ��ư
//			}
//		});
//		bikeBtn = (Button)getActivity().findViewById(R.id.bikeMapBtn);
//		apBtn = (Button)getActivity().findViewById(R.id.apMapBtn);
//		reloadBtn = (Button)getActivity().findViewById(R.id.reloadBtn);
//		arrayPoints = new ArrayList<LatLng>();
//		mClick = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				switch (v.getId()) {
//				case (int) R.id.bikeMapBtn://���ǥ��
//
//					String temp = null;
//					try {
//					    Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
//					    nameValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
//						HttpPostAsyncTask async_ap = new HttpPostAsyncTask(nameValue,HttpPostAsyncTask.bikeMapAddress);
//						temp = async_ap.execute().get();
//						
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (ExecutionException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					String[] array = temp.split(",");
//					
//					Log.i("���� TEst", ""+array);
//
//					if(temp.indexOf("empty")>=0){
//						toastOut("������ ��ġ�� ã�� �� �����ϴ�.");
//					}else{
//					arrayPoints = new ArrayList<LatLng>();	
//					leng=array.length;
//						for(int i=0;i<leng;i+=2){	
//							double lat = Double.parseDouble(array[i]);
//							double lng = Double.parseDouble(array[i+1]);
//							Log.i("Main", "Bike ���� lat("+lat+") lng("+lng+")");						
//							LatLng position = new LatLng(lat, lng);
//							PolylineOptions line = new PolylineOptions()
//								.color(Color.RED)
//								.width(5);
//							arrayPoints.add(position);
//							line.addAll(arrayPoints);
//							Polyline pl = mGoogleMap.addPolyline(line);
//							
//						}
//					}
//					
//					
//					break;
//				case (int)R.id.apMapBtn:
//					
//			      	String tmp = null;
//					try {
////						Vector<NameValuePair> nameValue = null;
//						HttpPostAsyncTask async_ap = new HttpPostAsyncTask(null,HttpPostAsyncTask.apInfoAddress);
//						tmp = async_ap.execute().get();
//						
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (ExecutionException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					if(tmp.indexOf("empty")>=0){
//						toastOut("AP��ġ�� ã�� �� �����ϴ�.");
//					}else{
//						String[] arr = tmp.split(",");
//						leng=arr.length;
//						Intent getI = getActivity().getIntent();
//						String title = getI.getStringExtra("title");
//						
//						Log.i("length", ""+leng);
//						for(int i=0;i<leng;i+=2){	
//							double lat = Double.parseDouble(arr[i]);
//							double lng = Double.parseDouble(arr[i+1]);
//							Log.i("Main", "MapAP ���� lat("+lat+") lng("+lng+")");
//							LatLng position = new LatLng(lat, lng);
//							
//							mGoogleMap.addMarker(
//									new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ap_location)).position(position).title(title))
//									.showInfoWindow();				
//						}
//					}
//					break;
//				
//				case (int)R.id.reloadBtn:
//					Log.i("Main.java", "���� ���ΰ�ħ");
//    			mGoogleMap.clear();
//					init();//���ΰ�ħ
//					break;
//					
//				}
//			}
//		};
//
//		bikeBtn.setOnClickListener(mClick);
//		apBtn.setOnClickListener(mClick);
//		reloadBtn.setOnClickListener(mClick);
//		
//    	mGoogleMap = ((SupportMapFragment)getChildFragmentManager()
//				.findFragmentById(R.id.map)).getMap();
//    	
//    	
//    }
//    @Override
//    public void onResume() {
//    	// TODO Auto-generated method stub
//    	super.onResume();
//    	init();
//    		while(MainActivity.pState == "4"){//�������� ��� ���� �ֱ�� ���ΰ�ħ
//    			try {
//					Thread.sleep(3600);//1�� ����
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    			mGoogleMap.clear();
//    			init();
//    		}
//	}
//
//	public void makeAlert(String title,String msg) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(pActivity); 
//
//		// ���⼭ ���ʹ� �˸�â�� �Ӽ� ����
//		builder.setTitle(title)	// ���� ����
//				.setMessage(msg)// �޼��� ����
//				.setCancelable(false)// �ڷ� ��ư Ŭ���� ��� ���� ����
//				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {	// Ȯ�� ��ư Ŭ���� ����
//					public void onClick(DialogInterface dialog, int whichButton) {
//						dialog.dismiss();
//						 Intent getI = pActivity.getIntent();
//						   String title = getI.getStringExtra("title");
//						   String temp = null;
//						   
//						   if(MainActivity.pState.length() <= 0){
//							   toastOut("�Ͻ����� �����Դϴ�. ������ ��õ� ���ּ���.");
//							   modeSwitch.setChecked(false);
//							   return;
//						   }
//							 try {
//							      Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
//							      nameValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
//							      
//						         HttpPostAsyncTask async_ap = new HttpPostAsyncTask(nameValue,HttpPostAsyncTask.bikeMapAddress);//������ ��ġ ����
//						         temp = async_ap.execute().get();
//						         
//						      } catch (InterruptedException e1) {
//						         // TODO Auto-generated catch block
//						         e1.printStackTrace();
//						      } catch (ExecutionException e1) {
//						         // TODO Auto-generated catch block
//						         e1.printStackTrace();
//						      }
//							 
//						      String[] init = temp.split(",");
//						      
//						      int init_leng;
//						      init_leng=init.length;
//
//			    		      double lat = 0,lng = 0;
//						       Log.i("parking!!!yuni11111111", "MainActivity.pState : "+ MainActivity.pState);
//						       
//								if (MainActivity.pState.indexOf("1") >= 0 || MainActivity.pState.indexOf("2") >= 0) {//��ŷ �Ǵ� ���̵� �����ϋ�
//									if (init_leng < 2) {//��ǥ�� ���� ���
//										Log.d("Main.java", "�������� ��ǥ�� ����:(�������䰪)"+init);
//										toastOut("��ǥ ���� �����Դϴ�. �ٽ� �õ� ���ּ���");
//
//									} else {//�ش� ��ǥ�� ȭ�� �̵� �� ��Ŀ ����
//										lat = Double.parseDouble(init[0]);
//						    		    lng = Double.parseDouble(init[1]);
//										addMapMarker(lat, lng,R.drawable.bike_location);// ��Ŀ
//										moveCamera(lat, lng, 18);// ȭ�� �̵�
//									}
//								}
//		    }
//				})
//				.setNegativeButton("���", new DialogInterface.OnClickListener() {
//					// ��� ��ư Ŭ���� ����
//					public void onClick(DialogInterface dialog, int whichButton) {
//						cancelParking();
//						dialog.cancel();
//						
//					}
//				});
//
//		AlertDialog dialog = builder.create(); // �˸�â ��ü ����
//		dialog.show(); // �˸�â ����
//	}
//    private void toastOut(String str){
//    	//Toast �����Լ�
//    	new Toast(getActivity()).makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//    }
//    public void cancelParking(){
//		Vector<NameValuePair> parkValue = new Vector<NameValuePair>();
//		parkValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
//    	parkValue.add(new BasicNameValuePair("ispark", "2"));
//		Log.i("Main.java", "��������(��ŷ���):"+parkValue);
//		HttpPostAsyncTask async_park = new HttpPostAsyncTask(parkValue,HttpPostAsyncTask.ParkingAddress);
//		try {
//			String recevie = async_park.execute().get();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//���� ������ ��ġ Ȯ�� �� ���� ���� �˸�â ����
//		Log.i("Main.java", "recevie: "+ recevie);
//		
//		//��� ����ġ �Ϲݻ��·� ����
//		modeSwitch.setChecked(false);
//    }
//    
//	private void getMyLocation() {
//		LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//		long minTime = 10000;
//		float minDistance = 0;
//		Log.i("getMyLocation", "test!!!" + manager);
//		MyLocationListener listener = new MyLocationListener(getActivity(),mGoogleMap);
//
//		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
//				minDistance, listener);
//
//		toastOut("����ġ�� ��û�߽��ϴ�.");
//
//	}
//
//	@Override
//	public void onMapClick(LatLng point) {
//
//		// ���� ������ �浵���� ȭ�� ����Ʈ�� �˷��ش�
//		mGoogleMap.getProjection().toScreenLocation(point);
//		Point screenPt = mGoogleMap.getProjection().toScreenLocation(point);
//
//		// ���� ȭ�鿡 ���� ����Ʈ�� ���� ������ �浵�� �˷��ش�.
//		LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(screenPt);
//
//		Log.d("����ǥ", "��ǥ: ����(" + String.valueOf(point.latitude) + "), �浵("
//				+ String.valueOf(point.longitude) + ")");
//		Log.d("ȭ����ǥ", "ȭ����ǥ: X(" + String.valueOf(screenPt.x) + "), Y("
//				+ String.valueOf(screenPt.y) + ")");
//	}
//
//	/**
//	 * �ʱ�ȭ
//	 * 
//	 * @author gon 2014. 2. 16.
//	 */
//	private void init() {
//			double latitude= MainActivity.defaultLatitude,longitude = MainActivity.defaultLongitude;
//			float zoom = MainActivity.defaultZoom;
//				
//		//������ ��ġ ��ȸ �� ȭ�� �ʱ�ȭ BEGIN
//    	String res_STR = "";//���� �޼���
//    	int location_leng = 0;//������ ��ǥ ���� (����,�浵) �� ����)
//    	try {
//	      Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
//	      nameValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
//	      
//	      HttpPostAsyncTask async_ap = new HttpPostAsyncTask(nameValue,HttpPostAsyncTask.bikeMapAddress);
//	      res_STR = async_ap.execute().get();
//	         
//	      } catch (InterruptedException e1) {
//	         // TODO Auto-generated catch block
//	         e1.printStackTrace();
//	      } catch (ExecutionException e1) {
//	         // TODO Auto-generated catch block
//	         e1.printStackTrace();
//	      }
//		 if(res_STR.equals("empty")){
//			 toastOut("��ǥ ����");
//			 return;
//		 }
//		String[] location_cut = res_STR.split(",");
//		location_leng = location_cut.length;
//		
//		if(location_leng < 2){//���ŵ� ��ǥ�� ���� ���
//			//�ʱⰪ���� ���� �̵�
//		}else{//������ ��ġ�� �̵�
//			latitude = Double.parseDouble(location_cut[0]);
//			longitude = Double.parseDouble(location_cut[1]);
//			
//		}// ������ ��ġ ��ȸ �� ȭ�� �ʱ�ȭ END
//		
//      GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
//		// ��ġ�̺�Ʈ ����
////      mGoogleMap.setOnMapClickListener(this);
//      // �� ��ġ�̵�.
//      moveCamera(latitude, longitude, zoom);
//      
////      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
//      if(MainActivity.pState.equals("1")){//��ŷ ���¶��
//    	  addMapMarker(latitude, longitude,R.drawable.bike_location);
//      }
//      else if(MainActivity.pState.equals("4")){//���� ����
//    	  //�ֱ� ��ǥ�� ��Ŀ
//    	  //�̵� ��� Ȯ��
//    	  PolylineOptions line = new PolylineOptions()
//			.color(Color.BLUE)
//			.width(5);
//    	  LatLng position;
//    	  int loc_index;
//    	  for(loc_index=0;loc_index<location_leng;loc_index+=2){	
//				double lat = Double.parseDouble(location_cut[loc_index]);
//				double lng = Double.parseDouble(location_cut[loc_index+1]);
//				Log.i("Main", "Bike ���� lat("+lat+") lng("+lng+")");	
//		    	position = new LatLng(lat, lng);					
////				arrayPoints.add(position);
////				line.addAll(arrayPoints);
////				Polyline pl = mGoogleMap.addPolyline(line);
//				line.add(position);
//				Polyline p1 = mGoogleMap.addPolyline(line);
//				
//				if(loc_index > 0 )	addMapMarker(lat,lng,R.drawable.move_point);//�̵� ��λ� ��Ŀ ǥ��
//		}
//    	  double lastLatitude = Double.parseDouble(location_cut[loc_index-2]),lastLongitude = Double.parseDouble(location_cut[loc_index-1]);
//    	  addMapMarker(latitude, longitude, R.drawable.bike_location);//ù��° ��ġ
//    	  addMapMarker(lastLatitude, lastLongitude,R.drawable.move_first_point);//������ ��ġ
////          moveCamera(lastLatitude, lastLongitude, zoom);
//      }
//      // ��Ŀ Ŭ�� ������
//      mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//    	  public boolean onMarkerClick(Marker marker) {
//    		  Log.i("Main.java", "��Ŀ Ŭ�� �α�");
//    		  return false;
//    	  }
//      });
//	}
//	
//	private void addMapMarker(double lat_marker,double lng_marker,int drawableRes){
//		if(lat_marker < 0 || lng_marker < 0 ){
//			lat_marker = 0;lng_marker=0;
//		}
//		Intent getI = pActivity.getIntent();
//		String title = getI.getStringExtra("title");
//		LatLng markerPosition = new LatLng(lat_marker, lng_marker);
//		
//		mGoogleMap.addMarker(  new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(drawableRes)).position(markerPosition).title(title)).showInfoWindow();
//	}
//	
//	private void moveCamera(double lat_camera,double lng_camera,float zoom_camera){
//		Log.i("Main.jave", "ȭ���̵�:"+ lat_camera +" , "+ lng_camera);
//		if(lat_camera < 0 || lng_camera < 0 || zoom_camera < 0){
//			lat_camera = 0;lng_camera=0;zoom_camera=0;
//		}
//		LatLng cameraPosition = new LatLng(lat_camera, lng_camera);
//	      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition, zoom_camera));
//	}
//	
//	//bj start2
////	private void initMarker() {
////        try {
////            String check;//���������� üũ�ϴº���
////
////            if (send.size() > 1) {
////                makeVector(send.get(0));
////                result = new HttpPostAsyncTask(nameValue, HttpPostAsyncTask.OUT_GPS_URL).execute().get().toString();
////                makeBigParsing(result);
////                for (int i = 0; i < bigReceive.size(); i++) {
////                    if (i == (bigReceive.size() - 1)) {
////                        check = "end";
////                        makeParsing(bigReceive.get(i), check);
////                    } else {
////                        check = "not";
////                        makeParsing(bigReceive.get(i), check);
////                    }
////                }
////            }
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        } catch (ExecutionException e) {
////            e.printStackTrace();
////        }
////    }
//	
//}
//