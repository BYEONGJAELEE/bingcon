package kr.ac.gnu.bingbingcon;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Use the {@link TextFragment#newInstance}
 * factory method to create an instance of this fragment.
 *
 */
public class UserSet extends Fragment {

	Button updateOkBtn;
	TextView idTv, nameTv, emailTv, modulTv;
	EditText emailEdit, modulEdit;
	Activity pActivity;
	// Main m;

	Fragment m;

	String userData_server = null;
	// 회원가입 값 수신 변수
	StringTokenizer tokens = null;
	String tId = "defaultID";// id
	String tName = "defaultNAME";// name
	String tPw = "defaultPW";// Pw
	String tEmail = "defaultEMAIL";// email
	String tModuleID = "defaultMODULEID";// ModuleID

	// bj

	// public static UserSet newInstance(Main m) {
	// UserSet fragment = new UserSet(m);
	// return fragment;
	// }
	// public UserSet(Main m) {
	// // Required empty public constructor
	// m = this.m;
	// }

	public static UserSet newInstance(Fragment m) {
		UserSet fragment = new UserSet(m);
		return fragment;
	}

	public UserSet(Fragment m) {
		// Required empty public constructor
		this.m = m;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.user_join_update, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		nameTv = (TextView) getActivity().findViewById(R.id.nameTextView);
		idTv = (TextView) getActivity().findViewById(R.id.idTextView);
		emailTv = (TextView) getActivity().findViewById(R.id.emailTextView);
		modulTv = (TextView) getActivity().findViewById(R.id.moduleIDTextView);
		updateOkBtn = (Button) getActivity().findViewById(R.id.updateOkBtn);
		emailEdit = (EditText) getActivity().findViewById(R.id.emailEditText);
		modulEdit = (EditText) getActivity()
				.findViewById(R.id.moduleIDEditText);

		// 서버에 ID전달하여 정보 수신
		Vector<NameValuePair> parkValue = new Vector<NameValuePair>();
		parkValue.add(new BasicNameValuePair("user_id", MainActivity.LogID));
		HttpPostAsyncTask async_park = new HttpPostAsyncTask(parkValue,
				HttpPostAsyncTask.userUpdateAddress);
		try {
			userData_server = async_park.execute().get();
			Log.i("UserSet.java", "user정보 수신: " + userData_server);
			// ID,name,PW,email,moduleID
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// userData_server에는 DB의 사용자 정보들이 돌아옴
			tokens = new StringTokenizer(userData_server);
			tId = tokens.nextToken(",");// id
			tName = tokens.nextToken(",");// name
			tPw = tokens.nextToken(",");// Pw
			tEmail = tokens.nextToken(",");// email
			tModuleID = tokens.nextToken(",");// ModuleID

			// UI에 정보 입력
			idTv.setText("ID    : " + tId);
			nameTv.setText("NAME  : " + tName);
			emailTv.setText("EMAIL : " + tEmail);
			modulTv.setText("MODULE: " + tModuleID);
		} catch (Exception e) {
			Log.e("UserSet.java", "사용자 입력값 오류");
		}
		updateOkBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (updateOkBtn.getText().toString().indexOf("수정") >= 0) {
					emailTv.setVisibility(View.GONE);
					modulTv.setVisibility(View.GONE);
					emailEdit.setText(tEmail);
					modulEdit.setText(tModuleID);

					emailEdit.setVisibility(View.VISIBLE);
					modulEdit.setVisibility(View.VISIBLE);
					updateOkBtn.setText("확인");
				} else {
					// 수정후
					emailEdit.setVisibility(View.GONE);
					modulEdit.setVisibility(View.GONE);
					emailTv.setVisibility(View.VISIBLE);
					modulTv.setVisibility(View.VISIBLE);
					updateOkBtn.setText("수정");

				}

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
