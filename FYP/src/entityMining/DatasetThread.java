/**
 * @author Hariprasaad
 *
 */

package entityMining;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class DatasetThread implements Runnable {

	int i=0;
	Thread t;
	public DatasetThread(int ii)
	{
		i=ii;
		t=new Thread(this);
		t.start();
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int[] categoryId = { 396545653, 396545138, 396546040, 396545012, 396545144, 396545013, 396545311,
				396545660, 396545014, 396545327, 396545015, 396545016,
				396545451, 396545433, 396545367, 396545019, 396545018,
				396545394, 396545401, 396545439, 396545443, 396545444,
				396546046, 396545122, 396545301, 396545454, 396545213,
				396545469, 396546089 };
		String[] categoryName = { "Arts and Humanities", "Beauty and Style",
				"Business and Finance", "Cars and Transportation",
				"Computers and Internet", "Consumer Electronics", "Dining Out",
				"Education and Reference", "Entertainment and Music",
				"Environment", "Family and Relationships", "Food and Drink",
				"Games and Recreation", "Health", "Home and Garden",
				"Local Businesses", "News and Events", "Pets",
				"Politics and Government", "Pregnancy and Parenting",
				"Science and Mathematics", "Social Science",
				"Society and Culture", "Sports", "Travel", "Yahoo Products" };
		HttpClient httpclient = new DefaultHttpClient();
		JsonParser parser = new JsonParser();
		
		BufferedWriter out=null;
		try {
			out = new BufferedWriter(new FileWriter("Dataset/"+categoryName[i]+".txt", true));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int n = categoryId.length;
		//int start = 0;
		int maxQ = 1000000;
		int maxResults=50;
		String service_url = "http://answers.yahooapis.com/AnswersService/V1/getByCategory?appid=VQDwmX58&type=resolved&output=json&category_id=";
		String start="&start=";
		String Results="&results=";

		//for (int i = 0; i < n; i++) {
			System.out.println(categoryName[i] + (i+1) + ":");

			for (int start_ = 0; start_ < maxQ; start_ += maxResults) {
				
				String url = service_url + categoryId[i] + start + start_ + Results+ maxResults;
				System.out.println(url);

				HttpResponse response=null;
				try {
					response = httpclient.execute(new HttpGet(url));
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JsonObject json_data=null;
				try {
					json_data = (JsonObject) parser.parse(EntityUtils
							.toString(response.getEntity()));
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				json_data=(JsonObject) json_data.get("all");
				JsonArray results = (JsonArray) json_data.get("questions");

				if (results != null) {
					for (Object planet : results) {
						Object res = ((JsonObject) planet).get("Subject");
						try {
							out.write(res.toString().replace("\"", "") + "\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.out.println(res.toString());
					}
				}
			}
		//}
		System.out.println("DONE!!!");
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
