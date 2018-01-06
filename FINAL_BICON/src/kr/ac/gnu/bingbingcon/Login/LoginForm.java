package kr.ac.gnu.bingbingcon.Login;

import kr.ac.gnu.bingbingcon.JoinActivity;
import kr.ac.gnu.bingbingcon.MainActivity;
import kr.ac.gnu.bingbingcon.R;
import kr.ac.gnu.bingbingcon.UserSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginForm extends Activity {
	// private BackPressCloseHandler bpch;
	private static String id;
	public static String pw;
	Button btn_login, btn_join, btn_submit;
	EditText user_id, user_pw;
	int result;
	OnClickListener logClick;

	static SharedPreferences pref;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);

		// bpch = new BackPressCloseHandler(this); // 뒤로가기핸들러 클래스 생성

		btn_join = (Button) findViewById(R.id.btn_join);
		btn_login = (Button) findViewById(R.id.btn_login);
		user_id = (EditText) findViewById(R.id.user_id);
		user_pw = (EditText) findViewById(R.id.user_pw);
		logClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {

				case R.id.btn_join:
					Log.i("BJ", "" + v.getId());
					// startActivity(new Intent(this, Joinform.class));//회원가입으로
					// 가기
					join();
					overridePendingTransition(R.anim.abc_fade_in,
							R.anim.abc_fade_out);
					break;
				case R.id.btn_login:
					Log.i("BJ", "" + v.getId());
					// 사용자한테 id랑 pw 입력받기
					setId("" + user_id.getText());
					setpassword("" + user_pw.getText());

					result = new LoginProcess().login(getId(), getpassword());

					/**/
					switch (result) {
					case LoginProcess._PARAM_EMPTY: // 빈칸
						Log.i("BJ1", "" + v.getId());

						// Toast.makeText(LoginForm., ""+result,
						// Toast.LENGTH_SHORT).show();
						BingActivity.alert.setMessage("ID 와 비밀번호를 입력하세요")
								.show();
						break;
					case LoginProcess._LOGIN_FAIL: // 로그인실패
						Log.i("BJ1", "" + v.getId());
						user_pw.setText(null);
						user_pw.requestFocus();
						break;
					case LoginProcess._LOGIN_SUCCESS: // 로그인성공
						Log.i("BJ1", "" + v.getId());
						// sharedPreference 값 저장하는 부분.
						// 에디터 세팅
						pref = getSharedPreferences("loginfo",
								Activity.MODE_PRIVATE);
						editor = pref.edit();
						// 데이터 입력
						editor.putString("user_id", getId());
						editor.putString("user_pw", getpassword());
						editor.commit();
						// 편집 종료
						setResult(LoginProcess._LOGIN_SUCCESS);
						endLogin();
						MainActivity.LogID = getId();// 아이디 전역변수 저장
						MainActivity.pState = "2";
						break;
					}
					/**/

					break;
				}
			}
		};

		btn_login.setOnClickListener(logClick);
		btn_join.setOnClickListener(logClick);

		/* 알림창 세팅 */
		BingActivity.alert = new AlertDialog.Builder(new ContextThemeWrapper(
				this, R.style.AppBaseTheme));
		BingActivity.alert.setPositiveButton("확인",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	public void join() {
		startActivity(new Intent(this, JoinActivity.class));// 회원가입으로 가기
	}

	public void endLogin() {
		Toast.makeText(this, "로그인 성공!!", Toast.LENGTH_SHORT).show();
		finish();
	}

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		LoginForm.id = id;
	}

	public static String getpassword() {
		return pw;
	}

	public static void setpassword(String pw) {
		LoginForm.pw = pw;
	}

}
