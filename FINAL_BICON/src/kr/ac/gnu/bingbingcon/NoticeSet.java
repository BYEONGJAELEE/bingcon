package kr.ac.gnu.bingbingcon;


import java.util.Vector;
import java.util.concurrent.ExecutionException;

import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.Toast;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TextFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NoticeSet extends Fragment implements OnSeekBarChangeListener {

	Switch soundSwitch, vibrateSwith;
	public static boolean soundFlag = true;		//알림음의 초기 default값은 ON이다.
	public static boolean vibrateFlag = true;		//진동의 초기 default값은 ON이다.
	Button gcmTestBtn;
	private Activity pActivity;
	
	public static NoticeSet newInstance() {
    	NoticeSet fragment = new NoticeSet();

    	return fragment;
    }

    public NoticeSet() {
        // Required empty public constructor
    }
    
    @Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	super.onAttach(activity);
    	pActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);
    	
    	// gcm push test btn
    	gcmTestBtn = (Button)getActivity().findViewById(R.id.gcmTestPushBtn);
    	gcmTestBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				Toast.makeText(pActivity, "GCM Push Test Btn Click", Toast.LENGTH_SHORT).show();
				Toast.makeText(pActivity, MainActivity.LogID, Toast.LENGTH_SHORT).show();

				
				try {
					Vector<NameValuePair> GcmTestValue = new Vector<NameValuePair>();
					GcmTestValue.add(new BasicNameValuePair("userid", MainActivity.LogID));
					HttpPostAsyncTask async_park = new HttpPostAsyncTask(GcmTestValue,
							HttpPostAsyncTask.GCMPushTestAddress);
					String tmp = async_park.execute().get();
					Toast.makeText(pActivity, tmp, Toast.LENGTH_SHORT).show();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		});
    	soundSwitch = (Switch)getActivity().findViewById(R.id.sound);
    	vibrateSwith = (Switch)getActivity().findViewById(R.id.vibrate);
    	
    	soundSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					Toast.makeText(pActivity, "음량 ON", Toast.LENGTH_SHORT).show();
					soundFlag = true;
				}
				else {
					Toast.makeText(pActivity, "음량 OFF", Toast.LENGTH_SHORT).show();
					soundFlag = false;
				}
				
			}
    	
    	});
    	
    	vibrateSwith.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					Toast.makeText(pActivity, "진동 ON", Toast.LENGTH_SHORT).show();
					vibrateFlag = true;
				}
				else {
					Toast.makeText(pActivity, "진동 OFF", Toast.LENGTH_SHORT).show();
					vibrateFlag = false;
				}
			}
    	
    	});
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

    	return inflater.inflate(R.layout.fragment_noticeset, container, false);
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}


}
