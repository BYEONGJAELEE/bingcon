package kr.ac.gnu.bingbingcon.Login;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import kr.ac.gnu.bingbingcon.Server.HttpPostAsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class LoginProcess {
	public static final int _LOGIN_SUCCESS = 300;
	public static final int _LOGIN_FAIL = 400;
	public static final int _PARAM_EMPTY = 500;
	private String url, user_id, user_pw, row;
	private StringTokenizer token;
	private List param;
	
	public int login(String user_id, String user_pw){
		if(user_id.equals("") ||  user_pw.equals("")) { //ID,PW 공백 일 때
			return _PARAM_EMPTY; //리턴-빈칸
		} else {
			this.user_id = user_id;
			this.user_pw = user_pw;
			
			 Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
//		      param = new ArrayList();
		      
		      
		      nameValue.add(new BasicNameValuePair("user_id", user_id));
		      nameValue.add(new BasicNameValuePair("user_pw", user_pw));
		      url = HttpPostAsyncTask.Login;
			
			try{
				row = new HttpPostAsyncTask(nameValue, url).execute().get();
				if(row.equals("id_fail")) { //계정을 찾지 못함ㅠ
					BingActivity.alert.setMessage("아이디를 확인해 주세요.").show();
					return _LOGIN_FAIL; //리턴-로그인실패
				}
				else if(row.equals("pw_fail")){
					BingActivity.alert.setMessage("비밀번호를 확인해 주세요.").show();
					return _LOGIN_FAIL;
				}
				else if(row.equals("success")){
					return _LOGIN_SUCCESS;
				}
				else
					return _LOGIN_FAIL;
				
			}catch(Exception e) {
				BingActivity.alert.setMessage(e.toString()).show();
				return _LOGIN_FAIL; //리턴-로그인실패

			}
		}
	}
}
	
