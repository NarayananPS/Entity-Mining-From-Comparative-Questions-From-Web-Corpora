/**
 * @author Narayanan
 *
 */

package entityMining;

import org.jsoup.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;

public class Acro {
	public static List<String> getacronyms(String query) throws Exception {
		{
			List<String> result=new ArrayList<String>();
			String url="http://www.acronyma.com/search.jsp?language=en&st=a&btnG=Search&query=";
			Document doc = Jsoup.parse(new URL(url+query),10000);
			Elements links = doc.getAllElements();
			for (Element link : links) {
				if(link.nodeName().equals("table") && !link.text().matches(".*<.*") && !link.text().matches(".*Acronym.*") && !link.text().matches(".*Sort.*"))
				{
					String[] ans=link.text().split(query);
					for(int i=1;i<ans.length;i++)
						result.add(ans[i]);
					break;
				}
			}
			return result;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getacronyms("WHO").toString());
	}
}
