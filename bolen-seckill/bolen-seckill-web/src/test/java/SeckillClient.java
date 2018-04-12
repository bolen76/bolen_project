import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SeckillClient {

	// HttpClient.
	private static final int TIMES = 1000;

	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost("http://localhost:9901/seckill/shop");

		for (int i = 0; i < TIMES; i++) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("pid", "1"));
			formparams.add(new BasicNameValuePair("uid", 4000+i + ""));

			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			//System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
				System.out.println("--------------------------------------");
			}
			response.close();
		}
		httpClient.close();
	}
}
