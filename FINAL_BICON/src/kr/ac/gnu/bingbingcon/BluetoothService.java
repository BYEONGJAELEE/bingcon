package kr.ac.gnu.bingbingcon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothService {
	// Debugging
	private static final String TAG = "BluetoothService";

	// Intent request code
	private static final int H_STATE_OFF = 1;
	private static final int H_STATE_ON = 2;
	private static final int REQUEST_ENABLE_BT = 2;

	private BluetoothAdapter btAdapter;

	private static Activity mActivity;
	private Handler mHandler;
	Handler mScanHandler = new Handler();

	Context mContext;
	Message msg;
	private Boolean mScanning;
	static BroadcastReceiver mReceiver;

	// Constructors
	public BluetoothService(Activity ac, Handler h) {
		mActivity = ac;
		mHandler = h;
		final BluetoothManager bluetoothManager = (BluetoothManager) mActivity
				.getSystemService(Context.BLUETOOTH_SERVICE);
		msg = mHandler.obtainMessage();
		// BluetoothAdapter ?ñªÍ∏?
		btAdapter = bluetoothManager.getAdapter();
		IntentFilter filter = new IntentFilter(
				BluetoothAdapter.ACTION_STATE_CHANGED);
		mActivity.registerReceiver(mReceiver, filter);

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();

				if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
					final int state = intent.getIntExtra(
							BluetoothAdapter.EXTRA_STATE,
							BluetoothAdapter.ERROR);
					switch (state) {
					case BluetoothAdapter.STATE_OFF:
						msg.what = H_STATE_OFF;
						mHandler.sendEmptyMessage(msg.what);

						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						break;
					case BluetoothAdapter.STATE_ON:
						// Î©îÏãúÏß? ID ?Ñ§?†ï
						msg.what = H_STATE_ON;
						mHandler.sendEmptyMessage(msg.what);
						break;
					case BluetoothAdapter.STATE_TURNING_ON:
						break;
					}
				}
			}
		};
	}

	/**
	 * ?ã®ÎßêÍ∏∞Í∞? Î∏îÎ£®?à¨?ä§Î•? Ïß??õê?ïò?äîÍ∞? Í≤??Ç¨
	 *
	 * @return boolean
	 */
	public boolean getDeviceState() {
		Log.i(TAG, "Check the Bluetooth support");

		if (btAdapter == null) {
			Log.d(TAG, "Bluetooth is not available");

			return false;

		} else {
			Log.d(TAG, "Bluetooth is available");

			return true;
		}
	}

	public void checkBluetooth() {
		if (btAdapter.isEnabled()) {
			msg.what = H_STATE_ON;
			mHandler.sendEmptyMessage(msg.what);

			// Next Step
		} else {
			msg.what = H_STATE_OFF;
			mHandler.sendEmptyMessage(msg.what);
		}
	}

	/**
	 * Î∏îÎ£®?à¨?ä§ ?ã§?ñâ
	 */
	public void enableBluetooth() {
		Log.i(TAG, "Check the enabled Bluetooth");

		if (btAdapter == null || !btAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			// scanLeDevice(true);
		}
	}

	// BLE ?ä§Ï∫?
	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mScanHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					btAdapter.stopLeScan(mLeScanCallback);
				}
			}, 1000);

			mScanning = true;
			btAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			btAdapter.stopLeScan(mLeScanCallback);
		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Log.d("check", device.getName());
				}
			});
		}
	};

	public static void DestroyReceiver() {
		if(mActivity.isChild())
			mActivity.unregisterReceiver(mReceiver);
	}
}