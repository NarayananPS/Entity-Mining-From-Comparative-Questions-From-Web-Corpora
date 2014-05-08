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

import edu.stanford.nlp.util.Sets;

public class Semantics {
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
		//System.out.println(op.toString());
		return op;
	}
	
	Set<String> commonDomainFB(String str1, String str2) throws Exception
	{
		Set<String> domain1=getDomain(str1);
		Set<String> domain2=getDomain(str2);
		Set<String> common=Sets.intersection(domain1, domain2);
		return common;
	}
	
	Set<String> commonDomain(String str1, String str2) throws Exception
	{
		Set<String> domain1=getDomain(str1);
		Set<String> domain2=getDomain(str2);		
		Set<String> common=Sets.intersection(domain1, domain2);
		return null;
	}

}
