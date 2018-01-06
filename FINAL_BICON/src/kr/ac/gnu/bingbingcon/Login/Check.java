package kr.ac.gnu.bingbingcon.Login;

import kr.ac.gnu.bingbingcon.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Check extends Activity implements OnClickListener{
	Button btn_logout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.check_activity);
	    // TODO Auto-generated method stub
	    
	    btn_logout = (Button) findViewById(R.id.logout);
	   
	    btn_logout.setOnClickListener(this);
	    
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.logout:
			SharedPreferences pref = getSharedPreferences("loginfo", MODE_PRIVATE);
	        SharedPreferences.Editor editor = pref.edit();
	        editor.remove("user_id");
	        editor.remove("user_pw");
	        editor.commit();
	        Toast.makeText(this, "SharedPreferences ID,PW 데이터 삭제", Toast.LENGTH_LONG).show();
		}
	}

}


