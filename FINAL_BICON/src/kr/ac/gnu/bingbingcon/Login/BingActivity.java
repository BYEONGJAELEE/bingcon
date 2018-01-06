package kr.ac.gnu.bingbingcon.Login;

import kr.ac.gnu.bingbingcon.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;

public class BingActivity extends Activity{
	public static final int _ACTVTY_FINISH = 50;
	public static AlertDialog.Builder alert,done; //alert �˸�â, done Ȯ�� ������ ��Ƽ��Ƽ ����Ǵ� â
	SharedPreferences pref;
	Intent temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivityForResult(new Intent(this, Splash.class), 0);
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out); 
//		setContentView(R.layout.main);
		alert = new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AppBaseTheme));
		alert.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss(); 
		    }
		});
	}	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch(resultCode) {
		case LoginProcess._LOGIN_FAIL: //�α��� ������ ���
			startActivityForResult(new Intent(this,LoginForm.class), 2);	//�α��� â���� �̵�
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		case Splash._NO_PRE: //ID,PW ���� ���
			startActivityForResult(new Intent(this, LoginForm.class), 1);	//�α��� â���� �̵�
			break;
		case LoginProcess._LOGIN_SUCCESS: //�α��� ������ ���
				// �������� �̵�
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
				setResult(_ACTVTY_FINISH);
				finish();
			break;
		case _ACTVTY_FINISH:
		default:
			this.finish();
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
