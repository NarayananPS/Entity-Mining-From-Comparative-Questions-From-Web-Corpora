/**
 * @author Hariprasaad
 *
 */

package entityMining;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.String;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import javax.xml.parsers.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.xml.sax.ext.*;

public class NounChuncker extends DefaultHandler
{
	static MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
	boolean Subject = false;
	boolean Content = false;
	boolean Category = false;
	boolean ChosenAnswer = false;
	String POS="";
	String subject="";
	String content="";
	String category="";
	String chosenAns="";
	List<String> nouns=new ArrayList<String>();
	List<Integer> count=new ArrayList<Integer>();
	
	public void startDocument() throws SAXException
	{
	}

	public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException
	{
		if (raw.equalsIgnoreCase("Subject"))
		{
			Subject = true;
		}
		if (raw.equalsIgnoreCase("Content"))
		{
			Content = true;
		}
		if (raw.equalsIgnoreCase("Category"))
		{
			Category = true;
		}
		if (raw.equalsIgnoreCase("ChosenAnswer"))
		{
			ChosenAnswer = true;
		}
	}

	public void characters(char buf[], int offset, int len) throws SAXException
	{
		if (Subject)
		{
			subject+=new String(buf, offset, len)+"\n";
			//System.out.println(subject);
			POS = POS + new String(buf, offset, len);
			Subject = false;
		}
		if (Content)
		{
			content+=new String(buf, offset, len)+".\n";
			//System.out.println(new String(buf, offset, len));
			POS = POS + new String(buf, offset, len);
			Subject = false;
		}
		if (Category)
		{
			category+=new String(buf, offset, len)+".\n";
			//System.out.println(new String(buf, offset, len));
			POS = POS + new String(buf, offset, len);
			Subject = false;
		}
		if (ChosenAnswer)
		{
			chosenAns+=new String(buf, offset, len)+".\n";
			//System.out.println(new String(buf, offset, len));
			POS = POS + new String(buf, offset, len);
			Subject = false;
		}
		
	}

	public void endDocument() throws SAXException
	{
	}

	public void extractNN(String POS)
	{
		StringTokenizer tokens = new StringTokenizer(POS.toLowerCase());
		String current="";
		
		while(tokens.hasMoreTokens())
		{
			current=tokens.nextToken();
			String[] temp = current.split("_");
			if(temp[1].equalsIgnoreCase("NN") || temp[1].equalsIgnoreCase("NNP") || temp[1].equalsIgnoreCase("NNS"))
			{
				int index=nouns.indexOf(temp[1]);
				if(index==-1)
				{
					nouns.add(temp[0]);
					count.add(0);
				}
				else
				{
					count.add(index, count.get(index)+1);
				}
			}
		}
		//return nouns;
	}
	
	public void extract()
	{
		extractNN(tagger.tagString(POS));
		extractNN(tagger.tagString(subject));
		extractNN(tagger.tagString(content));
		extractNN(tagger.tagString(category));
		extractNN(tagger.tagString(chosenAns));
	}
	/*
	public void Instance()
	{
		try
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			parser.parse(EntityUtils.toString(response.getEntity()), this);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	*/
}

/*
public static void main(String args[]) throws Exception
{
	NounChuncker handler = new NounChuncker();
	try
	{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser parser = spf.newSAXParser();
		parser.parse("getByCategory.xml", handler);
		MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
		//System.out.println(tagger.tagString(subject));
		//System.out.println(extractNN(tagger.tagString(subject)).toString());
		//String tagged=tagger.tagString(POS);
		//System.out.println(tagged);
		//List<String> NN=extractNN(tagged);
		//System.out.println(NN);
	}
	catch (Exception e)
	{
		System.err.println(e.getMessage());
	}
}
*/