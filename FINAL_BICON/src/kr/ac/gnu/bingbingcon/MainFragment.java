package kr.ac.gnu.bingbingcon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import kr.ac.gnu.bingbingcon.LocationService;
import kr.ac.gnu.bingbingcon.Login.Splash;
import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

/**
 * Created by Taehoon on 2015-09-04.
 */
public class MainFragment extends Fragment {
	static final String EXTRA_DATA = "bicon.extra";

	private static final int H_STATE_OFF = 1;
	private static final int H_STATE_ON = 2;

	// PHP와 통신을 위한 변수들
	private String result;
	private Vector<NameValuePair> nameValue;
	private String user_id;
	// 다음 지도 사용을 위한 변수들
	private final String LOCAL_API_KEY = "bbf1737e59b902dc7243352b825ccf05";
	private String address;
	private String latitude = null;
	private String longitude = null;
	private static MapView mapView;
	private ArrayList<String> locData = new ArrayList<String>();
	private ArrayList<String> bigReceive = new ArrayList<String>();
	private ArrayList<String> receive = new ArrayList<String>();
	// private ArrayList<String> send = new ArrayList<String>();
	private String lostLoc = null;

	// activity 변수
	private Button map;
	private Button refresh;
	private Button search;
	private TextView location;
	private BluetoothService btService;
	private Button parkButton;

	private ParkMode parkMode;
	// 위치 변수
	LocationManager locationManager;
	double curLong, curLati;
	LocationService locationService;
	View v;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		parkMode = new ParkMode();
	}

	// bj
	public static Fragment newInstance() {
		MainFragment fragment = new MainFragment();
		return fragment;
	}

	public MainFragment() {
		// TODO Auto-generated constructor stub
	}

	private final Handler mHandler = new Handler() {// 탐색버튼 변환을 위한 Handler

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case H_STATE_OFF:
				search.setText("탐색 시작");
				break;
			case H_STATE_ON:
				search.setText("탐색 중");
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			v = inflater.inflate(R.layout.activity_main2, container, false);
			mapView = new MapView(getActivity());
			mapView.setDaumMapApiKey("API_KEY");
			// send = (ArrayList<String>)
			// getActivity().getIntent().getSerializableExtra(EXTRA_DATA);
			ViewGroup mapViewContainer = (ViewGroup) v
					.findViewById(R.id.map_view);
			mapViewContainer.addView(mapView);
			initMarker();

			refresh = (Button) v.findViewById(R.id.map_refresh);
			map = (Button) v.findViewById(R.id.main_smap);
			parkButton = (Button) v.findViewById(R.id.main_mode); // 메인모드
			search = (Button) v.findViewById(R.id.main_ssearch);

			mapViewContainer.bringChildToFront(refresh);
			mapViewContainer.bringChildToFront(parkButton);
			locationManager = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);
			locationService = new LocationService(locationManager);
			locationService.registerLocationUpdates();

			// 블루투스가 연결된 상태인지 확인
			btService = new BluetoothService(getActivity(), mHandler);
			btService.checkBluetooth();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		// 주소찾기
		if (locationService.getLatitude() > 0
				&& locationService.getLongitude() > 0) {
			curLati = locationService.getLatitude();
			curLong = locationService.getLongitude();
			searchAddress("curLati", "curLong");
		} else
			searchAddress("35.15798", "128.099207");
		changePark();

		parkButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (parkButton.getText().toString().trim().equals("주차중")) {
					parkMode.riding();
					changePark();
				} else if (parkButton.getText().toString().trim().equals("주행중")) {
					String getLocation;
					getLocation = parkMode.parking(); //
					parseLocation(getLocation);
					changePark();
				}
			}
		});

		map.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {// 길 찾기

				try {
					final Uri.Builder uri = new Uri.Builder();
					final Intent intent = new Intent();

					if (!lostLoc.equals(null)) {
						curLati = locationService.getLatitude();
						curLong = locationService.getLongitude();
						if (curLati > 0 && curLong > 0) {
							String startLoc = Double.toString(curLati) + ","
									+ Double.toString(curLong);
							Log.d("startLoc", startLoc);
							uri.scheme("daummaps").authority("route")
									.appendQueryParameter("sp", startLoc)
									.appendQueryParameter("ep", lostLoc)
									.appendQueryParameter("by", "CAR");

							// Scheme를 활용해 다음 지도앱 실행
							intent.setAction("android.intent.action.VIEW");
							intent.setData(Uri.parse(uri.toString()));
							startActivity(intent);
						}
					}
				} catch (Exception e) {// 다음 지도앱이 깔려있지않으면 Scheme를 통해 구글플레이
										// 다음지도
					// 다운로드창으로 이동
					try {
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.setData(Uri
								.parse("market://details?id=net.daum.android.map"));
						startActivity(intent);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			}
		});
		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// btService.enableBluetooth();
					if (btService.getDeviceState()) {
						btService.enableBluetooth();
					}
					chkGpsService();
					if (!locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
							&& !locationManager
									.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						BeaconServiceUtility serviceUtility = new BeaconServiceUtility(
								getActivity());
						serviceUtility.onStart();
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initMarker();
			}
		});
		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initMarker();
	}

	// 다음 지도 API를 사용하여 좌포의 동/리까지 표시해준다
	private void searchAddress(String latitude, String longitude) {
		Log.d("searchAddress", "111 " + latitude + " ss : " + longitude);
		if (latitude != null && longitude != null) {
			Log.d("searchAddress", "Enter searchAddress Function2");
			double mLatitude = Double.parseDouble(latitude);
			double mLongitude = Double.parseDouble(longitude);
			MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mLatitude,
					mLongitude);// 위도와
			// 경도를
			// 입력하여
			// 포인트
			// 설정
			MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder(
					LOCAL_API_KEY, mapPoint, // 포인트에
					// 설정된
					// 위도와
					// 경도로
					// 주소찾기
					new MapReverseGeoCoder.ReverseGeoCodingResultListener() {

						@Override
						public void onReverseGeoCoderFailedToFindAddress(
								MapReverseGeoCoder arg0) {
							// TODO Auto-generated method stub
							Log.d("check", "실패");
						}

						@Override
						public void onReverseGeoCoderFoundAddress(
								MapReverseGeoCoder arg0, String arg1) {
							// TODO Auto-generated method stub
							// location.setText(arg1);// 찾은 주소를 EditText에 표시
						}
					}, getActivity());

			reverseGeoCoder.startFindingAddress();
		} else {
			// location.setText("GPS좌표를 가지고올수 없습니다");
		}
	}

	// ParkButton의 Text를 바구기 위하여
	private void parseLocation(String data) {
		Log.d("MainFragment", data);
		StringTokenizer str = new StringTokenizer(data, "/");
		while (str.hasMoreTokens()) {
			// 아직 파싱할 토큰이 더 있는지 여부를 확인한다
			String next = str.nextToken();
			locData.add(next);
			// 파싱해서 구한 다음토큰을 반환한다.
		}
		latitude = locData.get(1);
		longitude = locData.get(2);
	}

	private void changePark() {
		String isPark = parkMode.IsPask();
		Log.d("ispark", isPark);
		if (isPark.trim().equals("1")) {
			searchAddress(latitude, longitude);
			parkButton.setText("주차중");
			parkButton.setBackgroundColor(Color.CYAN);
		} else if (isPark.trim().equals("2")) {
			parkButton.setText("주행중");
			parkButton.setBackgroundColor(Color.YELLOW);
		}
	}

	private void makeMarker(String moduleId, Double mLatitude, Double mLongitude) {
		Log.d("makeMarker", mLatitude + " " + mLongitude);
		MapPoint mapPoint = MapPoint
				.mapPointWithGeoCoord(mLatitude, mLongitude);
		MapPOIItem marker = new MapPOIItem();
		marker.setItemName("모듈ID: " + moduleId);
		marker.setTag(0);
		marker.setMapPoint(mapPoint);
		marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는
																// BluePin 마커
																// 모양.
		marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를
																	// 클릭했을때,
																	// 기본으로 제공하는
																	// RedPin 마커
																	// 모양.
		mapView.addPOIItem(marker);
	}

	private void makeVector(String user_id) {
		nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("user_id", user_id));
	}

	private void makeBigParsing(String data) {
		bigReceive.clear();
		StringTokenizer str = new StringTokenizer(data, "/");
		while (str.hasMoreTokens()) {
			// 아직 파싱할 토큰이 더 있는지 여부를 확인한다
			String next = str.nextToken();

			if (next.equals("")) {
			} else {
				bigReceive.add(next);
				// 파싱해서 구한 다음토큰을 반환한다.
			}
		}
	}

	private void makeParsing(String data, String check) {
		Log.d("makeParsing", data);
		receive.clear();
		StringTokenizer str = new StringTokenizer(data, ",");
		while (str.hasMoreTokens()) {
			// 아직 파싱할 토큰이 더 있는지 여부를 확인한다
			String next = str.nextToken();
			if (next.equals("")) {
			} else {
				receive.add(next);
				// 파싱해서 구한 다음토큰을 반환한다.
			}
		}

		if (receive.size() > 1 && receive.get(0) != null) {
			if (check.equals("end")) {

				lostLoc = receive.get(1) + "," + receive.get(2);
				makeMarker(receive.get(0), Double.parseDouble(receive.get(1)),
						Double.parseDouble(receive.get(2)));
				// 마지막 위치 반원표시
				MapCircle circle1 = new MapCircle(
						MapPoint.mapPointWithGeoCoord(
								Double.parseDouble(receive.get(1)),
								Double.parseDouble(receive.get(2))), // center
						100, // radius
						Color.argb(128, 255, 0, 0), // strokeColor
						Color.argb(128, 0, 255, 0) // fillColor
				);
				circle1.setTag(1234);
				mapView.addCircle(circle1);
				mapView.setMapCenterPointAndZoomLevel(
						MapPoint.mapPointWithGeoCoord(
								Double.parseDouble(receive.get(1)),
								Double.parseDouble(receive.get(2))), 2, true);
			} else if (check.equals("not")) {
				makeMarker(receive.get(0), Double.parseDouble(receive.get(1)),
						Double.parseDouble(receive.get(2)));
			}
		}
	}

	private void initMarker() {
		try {
			String check;// 마지막인지 체크하는변수

			// if (send.size() > 1) {
			// 주리가 모듈id탐색하는 부분 구현하면 이부분과 연동.
			// 이병재 수정할부분!!!!!!!!!!!!!!!! - 수정함 - 2015-10-25수정.주리소스랑 통합완료.
			
			if(MainActivity.LogID != null)			
				makeVector(MainActivity.LogID);
			result = new HttpPostAsyncTask(nameValue,
					HttpPostAsyncTask.OUT_GPS_URL).execute().get().toString();
			if (result.equals("nobike")) {
//				Toast.makeText(getActivity(), "You don't have a Bike !",
//						Toast.LENGTH_LONG).show();
			} else {
				makeBigParsing(result);
				for (int i = 0; i < bigReceive.size(); i++) {
					if (i == (bigReceive.size() - 1)) {
						check = "end";
						makeParsing(bigReceive.get(i), check);
					} else {
						check = "not";
						makeParsing(bigReceive.get(i), check);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	// GPS 설정 체크
	private boolean chkGpsService() {

		String gps = android.provider.Settings.Secure.getString(getActivity()
				.getContentResolver(),
				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		Log.d(gps, "aaaa");

		if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

			// GPS OFF 일때 Dialog 표시
			AlertDialog.Builder gsDialog = new AlertDialog.Builder(
					getActivity());
			gsDialog.setTitle("위치 서비스 설정");
			gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
			gsDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// GPS설정 화면으로 이동
							Intent intent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							intent.addCategory(Intent.CATEGORY_DEFAULT);
							startActivity(intent);
						}
					})
					.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).create().show();
			return false;

		} else {
			return true;
		}
	}

}
