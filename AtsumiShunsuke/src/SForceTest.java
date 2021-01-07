import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SForceTest {
	public static void main(String[] args) {
		login();
	}
	static void login() {
		String url = "https://login.salesforce.com/services/oauth2/token";
		//String url = "https://test.salesforce.com/services/oauth2/token";//Sandbox環境の場合
		String method = "POST";
		String contentType = "application/x-www-form-urlencoded; charset=utf-8";

		StringBuilder sb =  new StringBuilder();

		//grant_typeはpassword
		sb.append("grant_type=password").append("&");

		//コンシューマ鍵
		sb.append("client_id=3MVG95mg0lk4batgswkDPV72bm1YRV2Q8S.Gh9jVHc1MOySg4UMDhT_lLQDkUdgcBSJKeoBN.rKyWsf2Df2M2").append("&");

		//コンシューマの秘密
		sb.append("client_secret=D18331BECCD3014C68D80FFCBAC391D1F0754E1FA31F7C8FEFF761CE9A45CD6D").append("&");

		//test@mail.co.jpなどSalesforceのユーザー名を指定
		sb.append("username=atsumi-2@a-cialdesign.com").append("&");

		//パスワード+セキュリティートークン
		sb.append("password=As-1305784zHsQFUBLGpTce3U9jCuXv6H");

		byte[] body = sb.toString().getBytes(StandardCharsets.UTF_8);
		byte[] result = executeRequest(url
			, method
			, contentType
			, null
			, body);

		if (result != null && result.length > 0) {
			System.out.println(new String(result,StandardCharsets.UTF_8));
		} else {
			System.out.println("レスポンスがないよ!!");
		}
	}

	static byte[] executeRequest(String url,String method,String contentType,String accessToken,byte[] body) {
		HttpURLConnection urlConn = null;
		try {
			// HTTP接続
			URL reqURL = new URL(url);
			urlConn = (HttpURLConnection) reqURL.openConnection();
			urlConn.setRequestMethod(method);
			if ( contentType != null ) urlConn.setRequestProperty("Content-Type", contentType);
			if ( accessToken != null ) urlConn.setRequestProperty("Authorization", "Bearer "+accessToken);
			urlConn.setDoOutput(true);

			urlConn.connect();

			// body送信
			if (body != null) {
				OutputStream out = urlConn.getOutputStream();
				try {
					out.write(body);
				} finally {
					out.close();
				}
			}

			//レスポンスの取得
			InputStream is = null;
			try {
				is = urlConn.getInputStream();
			}catch(Exception e) {
				is = urlConn.getErrorStream();
			}

			try (BufferedInputStream bis = new BufferedInputStream(is);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();){
				byte[] bbb = new byte[1024];
				while(true) {
					int r = bis.read(bbb);
					if ( r == -1 ) {
						break;
					}
					if ( r == 0 ) {
						Thread.sleep(100);
						continue;
					}
					baos.write(bbb,0,r);
				}
				return baos.toByteArray();
			}finally {
				if ( is != null ) {
					is.close();
				}
			}

		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (urlConn != null) {
				// HTTP切断
				urlConn.disconnect();
			}
		}
		return null;
	}
}