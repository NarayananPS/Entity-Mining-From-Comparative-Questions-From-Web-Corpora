/**
 * @author Hariprasaad
 *
 */

package entityMining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
//
import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Dataset {

	void getSetA() throws Exception {
		//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		//int i=Integer.parseInt(br.readLine());

		int[] categoryId = {396545653, 396545657, 396545012, 396545144, 396545013, 396545311,
				396545660, 396545014, 396545327, 396545015, 396545016,
				396545451, 396545433, 396545367, 396545019, 396545018,
				396545394, 396545401, 396545439, 396545443, 396545444,
				396546046, 396545122, 396545301, 396545454, 396545213,
				396545469, 396546089 };
		String[] categoryName = { "Phones_new", "PDA_new", "Arts and Humanities", "Beauty and Style",
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
		
		//BufferedWriter out=new BufferedWriter(new FileWriter("Dataset/"+categoryName[i]+".txt", true));
		//BufferedWriter out=new BufferedWriter(new FileWriter("Dataset/Software_Dataset.txt", true));
		
		//h3z5tHvV34GAEDaCKL11OJ9nY3Tj4v6AsTep5T8NTwb.abfi5hCbn9vAWnHTmIUjQQ--
		//VQDwmX58
		
		int n = 2;//categoryId.length;
		//int start = 0;
		int start_i=100;
		int maxQ = 100000;//-start_i;
		int maxResults=50;
		String service_url = "http://answers.yahooapis.com/AnswersService/V1/getByCategory?appid=h3z5tHvV34GAEDaCKL11OJ9nY3Tj4v6AsTep5T8NTwb.abfi5hCbn9vAWnHTmIUjQQ--&type=resolved&output=json&category_id=";
		String start="&start=";
		String Results="&results=";

		for (int i = 0; i < n; i++) {
			System.out.println(categoryName[i] + (i+1) + ":");
			BufferedWriter out=new BufferedWriter(new FileWriter("Dataset/"+categoryName[i]+".txt", true));

			for (int start_ = start_i; start_ < maxQ; start_ += maxResults) {
				
				String url = service_url + categoryId[i] + start + start_ + Results+ maxResults;
				System.out.println(url);

				HttpResponse response = httpclient.execute(new HttpGet(url));
				JsonObject json_data = (JsonObject) parser.parse(EntityUtils
						.toString(response.getEntity()));
				json_data=(JsonObject) json_data.get("all");
				JsonArray results = (JsonArray) json_data.get("questions");

				if (results != null) {
					for (Object planet : results) {
						Object res = ((JsonObject) planet).get("Subject");
						out.write(res.toString().replace("\"", "") + "\n");
						//System.out.println(res.toString());
					}
				}
			}
			out.close();
		}
		System.out.println("DONE!!!");
		//out.close();
	}
	
	void getCQuestionsA() throws Exception
	{
		BufferedReader inSet=new BufferedReader(new FileReader("Dataset/Set-A.txt"));
		BufferedReader inLabel=new BufferedReader(new FileReader("Dataset/Labels-A.txt"));
		BufferedWriter out=new BufferedWriter(new FileWriter("Dataset/CQuestions-A.txt", true));
		String current=null;
		while((current=inSet.readLine())!=null)
		{
			if(inLabel.readLine().equalsIgnoreCase("C"))
			{
				out.write(current+"\n");
			}
		}
		out.close();
		System.out.println("DONE!!!");
	}
}
