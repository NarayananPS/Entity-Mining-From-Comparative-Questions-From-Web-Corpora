/**
 * @author Hariprasaad
 *
 */

package entityMining;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Freebase {
	Set<String> getDomain(String query)
			throws Exception {
		//String query_envelope = "{\"query\":" + query + "}";
		String service_url = "https://www.googleapis.com/freebase/v1/search";

		String url = service_url + "?query="
				+ URLEncoder.encode(query, "UTF-8");// + params + "&key=" + key;

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(new HttpGet(url));

		JsonParser parser = new JsonParser();
		JsonObject json_data = (JsonObject) parser.parse(EntityUtils
				.toString(response.getEntity()));
		JsonArray results = (JsonArray) json_data.get("result");

		Set<String> op=new HashSet<String>();
		
		if (results != null) {
			for (Object planet : results) {
				Object res = ((JsonObject) planet).get("notable");
				if (res != null) {
					Object t = ((JsonObject) res).get("name");
					op.add(t.toString().replace("\"", ""));
					//System.out.println(t);
				}
			}
		}
		//String[] ret=op.//new String[op.size()];
		System.out.println(op.toString());
		return op;
	}
	
	Set<String> getEntities(String domain)
			throws Exception {
		String service_url = "https://www.googleapis.com/freebase/v1/mqlread/";
		String script="[{\"name\": null,\"type\":[{\"name\": \""+domain+"\"}]}]";
		String url = service_url + "?query="
				+ URLEncoder.encode(script, "UTF-8");// + params + "&key=" + key;
		System.out.println(url);

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(new HttpGet(url));

		JsonParser parser = new JsonParser();
		JsonObject json_data = (JsonObject) parser.parse(EntityUtils
				.toString(response.getEntity()));
		JsonArray results = (JsonArray) json_data.get("result");

		Set<String> op=new HashSet<String>();
		
		if (results != null) {
			for (Object planet : results) {
				Object res = ((JsonObject) planet).get("name");
				if (res != null) {
					op.add(res.toString().replace("\"", ""));
				}
			}
		}
		System.out.println(op.toString());
		return op;
	}

}
