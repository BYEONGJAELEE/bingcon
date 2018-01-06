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
//    // PHP와 통신을 위한 변수들
//    private String result;
//    private Vector<NameValuePair> nameValue;
//    private String user_id;
//    // 다음 지도 사용을 위한 변수들
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
//    // activity 변수
//    private Button map;
//    private Button refresh;
//    private Button search;
//    private TextView location;
//    private BluetoothService btService;
//    private Button parkButton;
//
//    private ParkMode parkMode;
//    //위치 변수
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
//        //블루투스가 연결된 상태인지 확인
//        btService = new BluetoothService(getActivity(), mHandler);
//        btService.checkBluetooth();
//
//        map = (Button) v.findViewById(R.id.main_smap);
//        //parkButton = (Button) v.findViewById(R.id.main_mode);메인모드
//        search = (Button) v.findViewById(R.id.main_ssearch);
//
//        // 주소찾기
//        //searchAddress(latitude, longitude);
////        changePark();
//
///*        parkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (parkButton.getText().toString().trim().equals("주차중")) {
//                    parkMode.riding();
//                    changePark();
//                } else if (parkButton.getText().toString().trim().equals("주행중")) {
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
//            public void onClick(View v) {//길 찾기
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
//                            // Scheme를 활용해 다음 지도앱 실행
//                            intent.setAction("android.intent.action.VIEW");
//                            intent.setData(Uri.parse(uri.toString()));
//                            startActivity(intent);
//                        }
//                    }
//                } catch (Exception e) {// 다음 지도앱이 깔려있지않으면 Scheme를 통해 구글플레이 다음지도
//                    // 다운로드창으로 이동
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
//		if(MainActivity.pState.indexOf("1") >= 0  ||  MainActivity.pState.indexOf("4") >= 0){//1은 파킹상태, 4는 도난 상태
//			//파킹상태
//			modeSwitch.setChecked(true);
//		}else
//			modeSwitch.setChecked(false);
//		//모드 변환 스위치
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
//					//ON 선택시 서버에 등록
//					parkValue.add(new BasicNameValuePair("ispark", "1"));
//					Log.i("Main.java", "서버전달:"+parkValue);
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
//					//현재 자전거 위치 확인 및 저장 유무 알림창 생성
//					Log.i("Main.java", "recevie: "+ temp);
//					if(temp.indexOf("park")>=0){
//						StringTokenizer tokens = new StringTokenizer(temp);
//						if(temp.indexOf("empty")>=0){
//							toastOut("현재 자전거 위치를 찾을 수 없습니다.");
//							modeSwitch.setChecked(false);
//						}else{
//							String receve[] = temp.split("/");
//							String state = receve[0];
//							park_latitude = Double.parseDouble(receve[1]);
//							park_longitude = Double.parseDouble(receve[2]);
//							Log.i("Main.java", "응답 처리-> state:"+state+" latitude:"+park_latitude+" longitude:"+park_longitude);
//							if(state.equals("parkmode")){
//								//parking -> parking 상태 변화 없음
//							}else if(state.equals("park")){
//								//ridding -> parking 상태 변화
//								makeAlert("위치 확인","현재 위치:"+park_latitude+"/"+park_longitude);
//							}else{
//								Log.e("Main.java","응답 오류");
//							}
//						}
//						Toast.makeText(pActivity, "파킹모드", Toast.LENGTH_SHORT).show();
//					}
//					else{
//						Log.e("Main.java", "parking Erro! : "+ temp);
//						modeSwitch.setChecked(false);
//					}
//				}
//				else{
//					parkValue.add(new BasicNameValuePair("ispark", "2"));
//					HttpPostAsyncTask async_park = new HttpPostAsyncTask(parkValue,HttpPostAsyncTask.ParkingAddress);
//					Log.i("Main.java", "서버전달:"+parkValue);
//					try {
//						temp = async_park.execute().get();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					Log.i("Main.java", "서버응답:"+temp);
//					Toast.makeText(pActivity, "파킹해제", Toast.LENGTH_SHORT).show();
//					mGoogleMap.clear();
//				}
//			}
//		});
//		Button myGpsBtn = (Button) getActivity().findViewById(R.id.myGPSBtn);
//		myGpsBtn.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Log.i("Main.java", "내위치 확인 버튼 클릭");
//				getMyLocation();//내위치 확인용 버튼
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
//				case (int) R.id.bikeMapBtn://경로표시
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
//					Log.i("윤희 TEst", ""+array);
//
//					if(temp.indexOf("empty")>=0){
//						toastOut("자전거 위치를 찾을 수 없습니다.");
//					}else{
//					arrayPoints = new ArrayList<LatLng>();	
//					leng=array.length;
//						for(int i=0;i<leng;i+=2){	
//							double lat = Double.parseDouble(array[i]);
//							double lng = Double.parseDouble(array[i+1]);
//							Log.i("Main", "Bike 응답 lat("+lat+") lng("+lng+")");						
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
//						toastOut("AP위치를 찾을 수 없습니다.");
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
//							Log.i("Main", "MapAP 응답 lat("+lat+") lng("+lng+")");
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
//					Log.i("Main.java", "지도 새로고침");
//    			mGoogleMap.clear();
//					init();//새로고침
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
//    		while(MainActivity.pState == "4"){//도난중일 경우 일정 주기로 새로고침
//    			try {
//					Thread.sleep(3600);//1분 단위
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
//		// 여기서 부터는 알림창의 속성 설정
//		builder.setTitle(title)	// 제목 설정
//				.setMessage(msg)// 메세지 설정
//				.setCancelable(false)// 뒤로 버튼 클릭시 취소 가능 설정
//				.setPositiveButton("확인", new DialogInterface.OnClickListener() {	// 확인 버튼 클릭시 설정
//					public void onClick(DialogInterface dialog, int whichButton) {
//						dialog.dismiss();
//						 Intent getI = pActivity.getIntent();
//						   String title = getI.getStringExtra("title");
//						   String temp = null;
//						   
//						   if(MainActivity.pState.length() <= 0){
//							   toastOut("일시적인 오류입니다. 종료후 재시도 해주세요.");
//							   modeSwitch.setChecked(false);
//							   return;
//						   }
//							 try {
//							      Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
//							      nameValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
//							      
//						         HttpPostAsyncTask async_ap = new HttpPostAsyncTask(nameValue,HttpPostAsyncTask.bikeMapAddress);//자전거 위치 받음
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
//								if (MainActivity.pState.indexOf("1") >= 0 || MainActivity.pState.indexOf("2") >= 0) {//파킹 또는 라이딩 상태일떄
//									if (init_leng < 2) {//좌표가 없을 경우
//										Log.d("Main.java", "자전거의 좌표가 없음:(서버응답값)"+init);
//										toastOut("좌표 수신 오류입니다. 다시 시도 해주세요");
//
//									} else {//해당 좌표로 화면 이동 및 마커 생성
//										lat = Double.parseDouble(init[0]);
//						    		    lng = Double.parseDouble(init[1]);
//										addMapMarker(lat, lng,R.drawable.bike_location);// 마커
//										moveCamera(lat, lng, 18);// 화면 이동
//									}
//								}
//		    }
//				})
//				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//					// 취소 버튼 클릭시 설정
//					public void onClick(DialogInterface dialog, int whichButton) {
//						cancelParking();
//						dialog.cancel();
//						
//					}
//				});
//
//		AlertDialog dialog = builder.create(); // 알림창 객체 생성
//		dialog.show(); // 알림창 띄우기
//	}
//    private void toastOut(String str){
//    	//Toast 생성함수
//    	new Toast(getActivity()).makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//    }
//    public void cancelParking(){
//		Vector<NameValuePair> parkValue = new Vector<NameValuePair>();
//		parkValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
//    	parkValue.add(new BasicNameValuePair("ispark", "2"));
//		Log.i("Main.java", "서버전달(파킹취소):"+parkValue);
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
//		//현재 자전거 위치 확인 및 저장 유무 알림창 생성
//		Log.i("Main.java", "recevie: "+ recevie);
//		
//		//모드 스위치 일반상태로 변경
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
//		toastOut("내위치를 요청했습니다.");
//
//	}
//
//	@Override
//	public void onMapClick(LatLng point) {
//
//		// 현재 위도와 경도에서 화면 포인트를 알려준다
//		mGoogleMap.getProjection().toScreenLocation(point);
//		Point screenPt = mGoogleMap.getProjection().toScreenLocation(point);
//
//		// 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
//		LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(screenPt);
//
//		Log.d("맵좌표", "좌표: 위도(" + String.valueOf(point.latitude) + "), 경도("
//				+ String.valueOf(point.longitude) + ")");
//		Log.d("화면좌표", "화면좌표: X(" + String.valueOf(screenPt.x) + "), Y("
//				+ String.valueOf(screenPt.y) + ")");
//	}
//
//	/**
//	 * 초기화
//	 * 
//	 * @author gon 2014. 2. 16.
//	 */
//	private void init() {
//			double latitude= MainActivity.defaultLatitude,longitude = MainActivity.defaultLongitude;
//			float zoom = MainActivity.defaultZoom;
//				
//		//자전거 위치 조회 및 화면 초기화 BEGIN
//    	String res_STR = "";//응답 메세지
//    	int location_leng = 0;//수힌한 좌표 갯수 (위도,경도) 의 갯수)
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
//			 toastOut("좌표 오류");
//			 return;
//		 }
//		String[] location_cut = res_STR.split(",");
//		location_leng = location_cut.length;
//		
//		if(location_leng < 2){//수신된 좌표가 없는 경우
//			//초기값으로 지도 이동
//		}else{//마지막 위치로 이동
//			latitude = Double.parseDouble(location_cut[0]);
//			longitude = Double.parseDouble(location_cut[1]);
//			
//		}// 자전거 위치 조회 및 화면 초기화 END
//		
//      GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
//		// 터치이벤트 설정
////      mGoogleMap.setOnMapClickListener(this);
//      // 맵 위치이동.
//      moveCamera(latitude, longitude, zoom);
//      
////      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
//      if(MainActivity.pState.equals("1")){//파킹 상태라면
//    	  addMapMarker(latitude, longitude,R.drawable.bike_location);
//      }
//      else if(MainActivity.pState.equals("4")){//도난 상태
//    	  //최근 좌표는 마커
//    	  //이동 경로 확인
//    	  PolylineOptions line = new PolylineOptions()
//			.color(Color.BLUE)
//			.width(5);
//    	  LatLng position;
//    	  int loc_index;
//    	  for(loc_index=0;loc_index<location_leng;loc_index+=2){	
//				double lat = Double.parseDouble(location_cut[loc_index]);
//				double lng = Double.parseDouble(location_cut[loc_index+1]);
//				Log.i("Main", "Bike 응답 lat("+lat+") lng("+lng+")");	
//		    	position = new LatLng(lat, lng);					
////				arrayPoints.add(position);
////				line.addAll(arrayPoints);
////				Polyline pl = mGoogleMap.addPolyline(line);
//				line.add(position);
//				Polyline p1 = mGoogleMap.addPolyline(line);
//				
//				if(loc_index > 0 )	addMapMarker(lat,lng,R.drawable.move_point);//이동 경로상 마커 표시
//		}
//    	  double lastLatitude = Double.parseDouble(location_cut[loc_index-2]),lastLongitude = Double.parseDouble(location_cut[loc_index-1]);
//    	  addMapMarker(latitude, longitude, R.drawable.bike_location);//첫번째 위치
//    	  addMapMarker(lastLatitude, lastLongitude,R.drawable.move_first_point);//마지막 위치
////          moveCamera(lastLatitude, lastLongitude, zoom);
//      }
//      // 마커 클릭 리스너
//      mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//    	  public boolean onMarkerClick(Marker marker) {
//    		  Log.i("Main.java", "마커 클릭 로그");
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
//		Log.i("Main.jave", "화면이동:"+ lat_camera +" , "+ lng_camera);
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
////            String check;//마지막인지 체크하는변수
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
