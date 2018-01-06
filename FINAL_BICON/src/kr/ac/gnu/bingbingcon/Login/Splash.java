package kr.ac.gnu.bingbingcon.Login;


import kr.ac.gnu.bingbingcon.MainActivity;
import kr.ac.gnu.bingbingcon.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

public class Splash extends Activity {
	static final int _NO_PRE = 100;
	static final int _FIND_PRE = 200;
	SharedPreferences pre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		Handler h = new Handler() {
			public void handleMessage(Message msg) {
				finish();
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out); 
			}
		};
		
		
		if(checkPreference()) { //preference�� ���� ��
			int temp = new LoginProcess().login(LoginForm.getId(), LoginForm.getpassword());//�α��� ����
			Log.i("BJ", ""+temp);
			if(temp==LoginProcess._LOGIN_SUCCESS){
				setResult(LoginProcess._LOGIN_SUCCESS);
			}else{
				setResult(LoginProcess._LOGIN_FAIL);
			}
		} else {
			setResult(_NO_PRE);
		}
		
		h.sendEmptyMessageDelayed(0, 2000);		
	}
	
	public boolean checkPreference() {
		pre = getSharedPreferences("loginfo", MODE_PRIVATE);
		if(pre.getString("user_id", "").equals("") || pre.getString("user_pw", "").equals("")) { //���̵�, ��� ���� ��
			Log.i("BJ", "No id & pw");
			return false;
		} else {//���̵�,���������
			Log.i("BJ", "we have id & pw");
			MainActivity.LogID = pre.getString("user_id", "");
			LoginForm.setId(MainActivity.LogID);
			LoginForm.setpassword(pre.getString("user_pw", ""));

			return true;
		}
	}
	
	/* �ڷΰ��� ��ư ������ */
	@Override
	public void onBackPressed() {
		//�ƹ��͵� ���Ԥ�
	}

}
