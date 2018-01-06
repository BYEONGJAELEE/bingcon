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

		// bpch = new BackPressCloseHandler(this); // �ڷΰ����ڵ鷯 Ŭ���� ����

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
					// startActivity(new Intent(this, Joinform.class));//ȸ����������
					// ����
					join();
					overridePendingTransition(R.anim.abc_fade_in,
							R.anim.abc_fade_out);
					break;
				case R.id.btn_login:
					Log.i("BJ", "" + v.getId());
					// ��������� id�� pw �Է¹ޱ�
					setId("" + user_id.getText());
					setpassword("" + user_pw.getText());

					result = new LoginProcess().login(getId(), getpassword());

					/**/
					switch (result) {
					case LoginProcess._PARAM_EMPTY: // ��ĭ
						Log.i("BJ1", "" + v.getId());

						// Toast.makeText(LoginForm., ""+result,
						// Toast.LENGTH_SHORT).show();
						BingActivity.alert.setMessage("ID �� ��й�ȣ�� �Է��ϼ���")
								.show();
						break;
					case LoginProcess._LOGIN_FAIL: // �α��ν���
						Log.i("BJ1", "" + v.getId());
						user_pw.setText(null);
						user_pw.requestFocus();
						break;
					case LoginProcess._LOGIN_SUCCESS: // �α��μ���
						Log.i("BJ1", "" + v.getId());
						// sharedPreference �� �����ϴ� �κ�.
						// ������ ����
						pref = getSharedPreferences("loginfo",
								Activity.MODE_PRIVATE);
						editor = pref.edit();
						// ������ �Է�
						editor.putString("user_id", getId());
						editor.putString("user_pw", getpassword());
						editor.commit();
						// ���� ����
						setResult(LoginProcess._LOGIN_SUCCESS);
						endLogin();
						MainActivity.LogID = getId();// ���̵� �������� ����
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

		/* �˸�â ���� */
		BingActivity.alert = new AlertDialog.Builder(new ContextThemeWrapper(
				this, R.style.AppBaseTheme));
		BingActivity.alert.setPositiveButton("Ȯ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	public void join() {
		startActivity(new Intent(this, JoinActivity.class));// ȸ���������� ����
	}

	public void endLogin() {
		Toast.makeText(this, "�α��� ����!!", Toast.LENGTH_SHORT).show();
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
